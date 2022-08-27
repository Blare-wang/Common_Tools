package com.itblare.itools.security;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.security
 * ClassName:   RSAProcessor
 * Author:   Blare
 * Date:     Created in 2021/4/17 23:48
 * Description:    非对称加密
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/17 23:48    1.0.0             非对称加密
 */

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * 非对称加密
 *
 * @author Blare
 * @create 2021/4/17 23:48
 * @since 1.0.0
 */
public class RSAProcessor {

    /**
     * 默认公钥的持久化文件存放位置
     */
    private static final String PUBLIC_KEY_FILE = "rsa/publicKey_rsa_1024.pub";

    /**
     * 默认私钥的持久化文件存放位置
     */
    private static final String PRIVATE_KEY_FILE = "rsa/privateKey_rsa_1024";

    private static final String KEY_ALGORITHM = "RSA";

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
     *
     * @param publicKeyBytes 公钥key
     * @return {@link PublicKey}
     * @method restorePublicKey
     * @date 2021/4/17 23:55
     */
    public static PublicKey restorePublicKey(byte[] publicKeyBytes) {
        //Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
     *
     * @param privateKeyBytes 私钥key
     * @return {@link PrivateKey}
     * @method restorePrivateKey
     * @date 2021/4/17 23:56
     */
    public static PrivateKey restorePrivateKey(byte[] privateKeyBytes) {
        //Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA private keys
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
     *
     * @param publicKeyBytes 公钥key
     * @return {@link PublicKey}
     * @method restorePublicKey
     * @date 2021/4/17 23:55
     */
    public static String restoreStringPublicKey(byte[] publicKeyBytes) {
        //Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            final PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return Base64Processor.encode(publicKey.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
     *
     * @param privateKeyBytes 私钥key
     * @return {@link PrivateKey}
     * @method restorePrivateKey
     * @date 2021/4/17 23:56
     */
    public static String restoreStringPrivateKey(byte[] privateKeyBytes) {
        //Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA private keys
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            final PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return Base64Processor.encode(privateKey.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成密钥对
     *
     * @param keySize key大小，默认1024
     * @return {@link KeyPair}
     * @method generateKeyPair
     * @date 2021/4/18 0:01
     */
    public static KeyPair generateKeyPair(Integer keySize) {

        try {
            /*
             * <br>
             * 〈
             *   KeyPairGenerator类用于生成公钥和私钥对。 密钥对生成器使用getInstance工厂方法（返回给定类的实例的静态方法）构造。
             *   用于特定算法的密钥对生成器创建可以与该算法一起使用的公钥/私钥对。 它还将算法特定的参数与生成的每个密钥相关联。
             *   生成密钥对的方法有两种：以算法无关的方式，并以算法特定的方式。 两者之间的唯一区别是对象的初始化。
             *   Java平台的每个实现都需要支持以下标准KeyPairGenerator算法，并在括号中键入：
             *   DiffieHellman （1024）
             *   DSA （1024）
             *   RSA （ RSA ）
             *  〉
             */
            if (Objects.isNull(keySize)) {
                keySize = 1024;
            }
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取公钥
     *
     * @param keyPair 密钥对
     * @return {@link String}
     * @method getPublicKey
     * @date 2021/4/18 0:36
     */
    public static String getPublicKey(KeyPair keyPair) {
        try {
            final PublicKey publicKey = keyPair.getPublic();
            return Base64Processor.encode(publicKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取公钥文件失败");
    }

    /**
     * 获取密钥
     *
     * @param keyPair 密钥对
     * @return {@link String}
     * @method getPrivateKey
     * @date 2021/4/18 0:34
     */
    public static String getPrivateKey(KeyPair keyPair) {
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            return Base64Processor.encode(privateKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取密钥文件失败");
    }

    // ------------ 私钥签名， 公钥认证 （常用） ------------ //

    /**
     * 生成数字签名，私钥签名
     *
     * @param data        加密数据
     * @param private_key 私钥
     * @return {@link String}
     * @method sign
     * @date 2021/4/18 0:33
     */
    public static String sign(String data, String private_key) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = Base64Processor.decode(private_key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data.getBytes());

        return Base64Processor.encode(signature.sign());
    }

    /**
     * 校验数字签名，公钥认证
     *
     * @param data       被签名数据，明文
     * @param sign       数字签名
     * @param public_key 公钥
     * @return {@link boolean}
     * @method verify
     * @date 2021/4/18 0:32
     */
    public static boolean verify(String data, String sign, String public_key) throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = Base64Processor.decode(public_key);

        // 取得公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        // 用公钥对信息认证
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data.getBytes());

        // 验证签名是否正常
        return signature.verify(Base64Processor.decode(sign));
    }

    // ------------ 私钥加密，公钥解密 ------------ //

    /**
     * 私钥加密
     *
     * @param data        待加密信息
     * @param private_key 私钥
     * @return {@link String}
     * @method encryptByPrivateKey
     * @date 2021/4/18 0:31
     */
    public static String encryptByPrivateKey(String data, String private_key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Processor.decode(private_key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] secret = cipher.doFinal(data.getBytes());
        return Base64Processor.encode(secret);
    }

    /**
     * 公钥解密
     *
     * @param secret     密文
     * @param public_key 公钥
     * @return {@link String}
     * @method decryptByPublicKey
     * @date 2021/4/18 0:31
     */
    public static String decryptByPublicKey(String secret, String public_key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Processor.decode(public_key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] proclaimed = cipher.doFinal(Base64Processor.decode(secret));
        return new String(proclaimed);
    }

    /**
     * 公钥加密
     *
     * @param data       待加密信息
     * @param public_key 公钥
     * @return {@link String}
     * @method encryptByPublicKey
     * @date 2021/4/18 0:30
     */
    public static String encryptByPublicKey(String data, String public_key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Processor.decode(public_key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] secret = cipher.doFinal(data.getBytes());
        return Base64Processor.encode(secret);
    }

    /**
     * 私钥解密
     *
     * @param secret      密文
     * @param private_key 私钥
     * @return {@link String}
     * @method decryptByPrivateKey
     * @date 2021/4/18 0:29
     */
    public static String decryptByPrivateKey(String secret, String private_key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Processor.decode(private_key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] proclaimed = cipher.doFinal(Base64Processor.decode(secret));
        return new String(proclaimed);
    }

    public static void main(String[] args) throws Exception {
        final KeyPair keyPair = generateKeyPair(1024);
        final String publicKey = getPublicKey(keyPair);
        final String privateKey = getPrivateKey(keyPair);
        String orig = "我是你大爷。。。。。";
        System.out.println("使用私钥加密，公钥解密");
        final String s = encryptByPrivateKey(orig, privateKey);
        System.out.println(String.format("字符串{ %s } 加密结果：{ %s }", orig, s));
        final String s1 = decryptByPublicKey(s, publicKey);
        System.out.println(String.format("字符串{ %s } 加密结果：{ %s }", s, s1));

        System.out.println("使用公钥加密，私钥解密");
        final String s2 = encryptByPublicKey(orig, publicKey);
        System.out.println(String.format("字符串{ %s } 加密结果：{ %s }", orig, s2));
        final String s3 = decryptByPrivateKey(s2, privateKey);
        System.out.println(String.format("字符串{ %s } 加密结果：{ %s }", s2, s3));

        System.out.println("使用私钥签名，公钥认证");
        final String sign = sign(orig, privateKey);
        System.out.println(String.format("私钥签名结果：{%s}", sign));
        final boolean verify = verify(orig, sign, publicKey);
        System.out.println(String.format("公钥验证结果：%s", verify));
    }
}
