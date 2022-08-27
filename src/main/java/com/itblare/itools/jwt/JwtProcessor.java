package com.itblare.itools.jwt;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.jwt
 * ClassName:   JwtUtil
 * Author:   Blare
 * Date:     Created in 2021/4/15 16:53
 * Description:    Json Web Token 处理器
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/15 16:53    1.0.0         Json Web Token 处理器
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.lang.Assert;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.*;

/**
 * Json Web Token 处理器
 *
 * @author Blare
 * @create 2021/4/15 16:53
 * @since 1.0.0
 */
public class JwtProcessor {
    //常量定义
    public static final String SECRET_KEY = "?::4343fdf4fdf6cvf):";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int COUNT_2 = 2;//jwt字符串的"."数目
    private static final CompressionCodecResolver codecResolver = new DefaultCompressionCodecResolver();

    /**
     * <br>
     * 〈生成JWT字符串〉
     *
     * @param id                 令牌ID
     * @param subject            用户ID
     * @param issuer             签发人
     * @param period             有效时间(毫秒)
     * @param roles              访问主张-角色
     * @param permissions        访问主张-权限
     * @param signatureAlgorithm 加密算法 推荐选择SHA-256
     * @return java.lang.String
     * @date 2020/11/22
     */
    public static String generateJWT(String id, String subject, String issuer, Long period, String roles, String permissions, SignatureAlgorithm signatureAlgorithm) {

        //获取当前系统时间
        long nowTimeMillis = System.currentTimeMillis();
        Date now = new Date(nowTimeMillis);
        //将BASE64SECRET常量字符串使用base64解码成字节数组
        byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        //添加构成JWT的参数
        JwtBuilder jwtBuilder = Jwts.builder();
        //Header（头部） { "alg": "HS256", "typ": "JWT" }
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("alg", SignatureAlgorithm.HS256.getValue());
        headMap.put("typ", "JWT");
        jwtBuilder.setHeader(headMap);
        //Payload（负载）
        // {iss (issuer)：签发人,exp (expiration time)：过期时间,sub (subject)：主题,aud (audience)：受众,nbf (Not Before)：生效时间,iat (Issued At)：签发时间,jti (JWT ID)：编号}
        // 设置编号
        if (!(null == id || id.isBlank())) {
            jwtBuilder.setId(id);
        }
        // 设置主题
        if (!(null == subject || subject.isBlank())) {
            jwtBuilder.setSubject(subject);
        }
        // 设置签发人
        if (!(null == issuer || issuer.isBlank())) {
            jwtBuilder.setIssuer(issuer);
        }
        // 设置签发时间
        jwtBuilder.setIssuedAt(new Date(nowTimeMillis));
        // 设置到期时间
        if (null != period) {
            jwtBuilder.setExpiration(new Date(nowTimeMillis + period * 1000));
        }
        //自定义角色
        if (!(null == roles || roles.isBlank())) {
            jwtBuilder.claim("roles", roles);
        }
        //自定义权限
        if (!(null == permissions || permissions.isBlank())) {
            jwtBuilder.claim("perms", permissions);
        }
        //Signature（签名）首先，需要指定一个密钥（secret）。然后，使用 Header 里面指定的签名算法（默认是 HMAC SHA256），按照下面的公式产生签名：HMACSHA256(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
        // 压缩，可选GZIP
        jwtBuilder.compressWith(CompressionCodecs.DEFLATE);
        // 加密设置
        jwtBuilder.signWith(signatureAlgorithm, secreKeyBytes);

        return jwtBuilder.compact();
    }

    /**
     * <br>
     * 〈解析JWT的Payload〉
     *
     * @param jwt token字符串
     * @return java.lang.String
     * @date 2020/11/22
     */
    public static String parseJwtPayload(String jwt) {
        Assert.hasText(jwt, "JWT String argument cannot be null or empty!");
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwt.toCharArray()) {
            if (c == '.') {
                CharSequence tokenSeq = io.jsonwebtoken.lang.Strings.clean(sb);//去两边空格=>trim()
                String token = tokenSeq != null ? tokenSeq.toString() : null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        //JWT格式错误1, 非 Header.Payload.Signature 格式
        if (delimiterCount != COUNT_2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        //JWT格式错误2 -无Body
        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }

        /*JWT头部 Header*/
        Header<?> header;
        CompressionCodec compressionCodec = null;
        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
            Map<String, Object> m = readValue(origValue);
            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader<>(m);
            }
            compressionCodec = codecResolver.resolveCompressionCodec(header);
        }
        /*JWT负载 Payload*/
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, io.jsonwebtoken.lang.Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }
        return payload;

    }


    /**
     * <br>
     * 〈验签JWT〉
     *
     * @param jwt    json web token
     * @param appKey 密钥key
     * @return com.wondersgroup.archetype.vo.general.JwtAccount
     * @date 2020/11/22
     */
    public static JwtAccount parseJwt(String jwt, String appKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        Claims claims = Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(appKey))
            .parseClaimsJws(jwt)
            .getBody();
        JwtAccount jwtAccount = new JwtAccount();
        //令牌ID
        jwtAccount.setTokenId(claims.getId());
        // 客户标识
        jwtAccount.setAppId(claims.getSubject());
        // 签发者
        jwtAccount.setIssuer(claims.getIssuer());
        // 签发时间
        jwtAccount.setIssuedAt(claims.getIssuedAt());
        // 接收方
        jwtAccount.setAudience(claims.getAudience());
        // 访问主张-角色
        jwtAccount.setRoles(claims.get("roles", String.class));
        // 访问主张-权限
        jwtAccount.setPerms(claims.get("perms", String.class));
        return jwtAccount;
    }


    /**
     * <br>
     * 〈从json数据中读取格式化map〉
     *
     * @param val json字符串
     * @return java.util.Map
     * @date 2020/11/22
     */
    public static Map<String, Object> readValue(String val) {
        try {
            //noinspection unchecked
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }

    /**
     * <br>
     * 〈分割字符串进SET〉
     *
     * @param str 带分割字符串
     * @date 2020/11/22
     */
    public static Set<String> split(String str) {

        Set<String> set = new HashSet<>();
        if (null == str || str.isBlank()) {
            return set;
        }
        set.addAll(Arrays.asList(str.split(",")));
        return set;
    }
}