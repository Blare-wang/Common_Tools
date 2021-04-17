package com.itblare.itools.http;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http
 * ClassName:   OkHttpClient
 * Author:   Blare
 * Date:     Created in 2021/4/15 17:12
 * Description:    OK HTTP 客户端
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/15 17:12    1.0.0             OK HTTP 客户端
 */

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 一句话功能简述：OK HTTP 客户端
 *
 * @author Blare
 * @create 2021/4/15 17:12
 * @since 1.0.0
 */
public class OkHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpClient.class);

    /**
     * 默认 http 客户端
     */
    private static final okhttp3.OkHttpClient okHttpClient;
    /**
     * 连接超时时间 单位秒(默认10s)
     */
    private static final int CONNECT_TIMEOUT = 10;
    /**
     * 回调超时
     */
    private static final int CALL_TIMEOUT = 10;
    /**
     * 回复超时时间 单位秒(默认30s)
     */
    private static final int READ_TIMEOUT = 30;
    /**
     * 底层HTTP库所有的并发执行的请求数量
     */
    private static final int DISPATCHER_MAX_REQUESTS = 64;
    /**
     * 底层HTTP库对每个独立的Host进行并发请求的数量
     */
    private static final int DISPATCHER_MAX_REQUESTS_PER_HOST = 16;
    /**
     * 底层HTTP库中复用连接对象的最大空闲数量
     */
    private static final int CONNECTION_POOL_MAX_IDLE_COUNT = 32;
    /**
     * 底层HTTP库中复用连接对象的回收周期（单位分钟）
     */
    private static final int CONNECTION_POOL_MAX_IDLE_MINUTES = 5;

    static {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(DISPATCHER_MAX_REQUESTS);
        dispatcher.setMaxRequestsPerHost(DISPATCHER_MAX_REQUESTS_PER_HOST);

        ConnectionPool pool = new ConnectionPool(CONNECTION_POOL_MAX_IDLE_COUNT,
            CONNECTION_POOL_MAX_IDLE_MINUTES, TimeUnit.MINUTES);

        okHttpClient = new okhttp3.OkHttpClient.Builder()
            .sslSocketFactory(Objects.requireNonNull(sslSocketFactory()), x509TrustManager())
            .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .dispatcher(dispatcher)
            .connectionPool(pool)
            .build();
    }

    private static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory sslSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");//信任任何链接
            sslContext.init(null, new TrustManager[] {x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get
     *
     * @param url    请求的url
     * @param params 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return str 响应结果
     */
    public static String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        final Request.Builder builder = new Request.Builder()
            .url(sb.toString());
        if (Objects.nonNull(headers) && headers.size() > 0) {
            headers.forEach(builder::addHeader);
        }
        return respStr(builder.build());
    }


    public static String doFromPost(String url, Map<String, String> params, Map<String, String> headers) {
        FormBody.Builder formBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                formBuilder.add(key, params.get(key));
            }
        }
        final Request.Builder builder = new Request.Builder()
            .url(url)
            .post(formBuilder.build());
        if (Objects.nonNull(headers) && headers.size() > 0) {
            headers.forEach(builder::addHeader);
        }
        return respStr(builder.build());
    }


    public static String doJsonPost(String url, String jsonParams, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        final Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (Objects.nonNull(headers) && headers.size() > 0) {
            headers.forEach(builder::addHeader);
        }
        return respStr(builder.build());
    }

    /**
     * 功能描述: xml 信息发送
     *
     * @param url     请求url
     * @param xml     xml信息
     * @param headers 请求头信息，可为空
     * @return {@link String}
     * @method doXmlPost
     * @author Blare
     * @date 2021/4/1515:03
     * @updator Blare
     */
    public static String doXmlPost(String url, String xml, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        final Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (Objects.nonNull(headers) && headers.size() > 0) {
            headers.forEach(builder::addHeader);
        }
        return respStr(builder.build());
    }

    /**
     * 功能描述: 执行请求，返回结果
     *
     * @param request 请求对象
     * @return {@link String}
     * @method respStr
     * @author Blare
     * @date 2021/4/1514:52
     * @updator Blare
     */
    private static String respStr(Request request) {
        try (
            Response response = okHttpClient.newCall(request).execute()
        ) {
            if (response.isSuccessful()) {
                return Objects.requireNonNull(response.body()).string();
            }
            LOGGER.info("okHttp3 request 失败 -> {}", response.body());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("okHttp3 request error >> ex -> {}", e.getMessage());
        }
        return null;
    }

    /**
     * 功能描述: Okhttp3 头信息封装
     *
     * @param headersParams 请求header
     * @return {@link Headers}
     * @method setHeaders
     * @author Blare
     * @date 2021/4/1514:50
     * @updator Blare
     */
    private static Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        final Headers.Builder builder = new Headers.Builder();
        if (Objects.nonNull(headersParams) && headersParams.size() > 0) {
            headersParams.forEach(builder::add);
            headers = builder.build();
        }
        return headers;
    }
}
