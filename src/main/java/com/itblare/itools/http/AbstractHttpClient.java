package com.itblare.itools.http;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http
 * ClassName:   AbstractHttpClient
 * Author:   Blare
 * Date:     Created in 2021/4/22 15:10
 * Description:    HTTP 请求
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/22 15:10    1.0.0         HTTP 请求
 */

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * HTTP 请求
 *
 * @author Blare
 * @create 2021/4/22 15:10
 * @since 1.0.0
 */
public abstract class AbstractHttpClient {

    /**
     * HTTP GET 请求
     *
     * @param url     请求URL
     * @param params  请求参数
     * @param headers 请求头
     * @return {@link String}
     * @method doGet
     * @date 2021/4/22 16:12
     */
    abstract String doGet(String url, Map<String, String> params, Map<String, String> headers);

    /**
     * HTTP POST 表单请求
     *
     * @param url     请求URL
     * @param params  请求参数
     * @param headers 请求头
     * @return {@link String}
     * @method doFromPost
     * @date 2021dd/4/22 16:13
     */
    abstract String doFromPost(String url, Map<String, String> params, Map<String, String> headers);

    /**
     * HTTP POST JSON请求
     *
     * @param url        请求URL
     * @param jsonParams 请求参数
     * @param headers    请求头
     * @return {@link String}
     * @method doJsonPost
     * @date 2021/4/22 16:13
     */
    abstract String doJsonPost(String url, String jsonParams, Map<String, String> headers);

    /**
     * 功能描述:
     *
     * @param url     请求URL
     * @param xml     XML请求参数
     * @param headers 请求头
     * @return {@link String}
     * @method doXmlPost
     * @date 2021/4/22 16:13
     */
    abstract String doXmlPost(String url, String xml, Map<String, String> headers);

    /**
     * 功能描述:
     *
     * @param url     请求URL
     * @param params  请求参数
     * @param file    文件
     * @param headers 请求头
     * @return {@link String}
     * @method doMultipartPost
     * @date 2021/4/22 16:13
     */
    abstract String doMultipartPost(String url, Map<String, String> params, File file, Map<String, String> headers);

    /**
     * 功能描述:
     *
     * @param url     请求URL
     * @param params  请求参数
     * @param stream  流
     * @param headers 请求头
     * @return {@link String}
     * @method doBinaryPost
     * @date 2021/4/22 16:13
     */
    abstract String doBinaryPost(String url, Map<String, String> params, InputStream stream, Map<String, String> headers);
}