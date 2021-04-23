package com.itblare.itools.http;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http
 * ClassName:   RequestResponseUtil
 * Author:   Blare
 * Date:     Created in 2021/4/13 17:04
 * Description:    HTTP 请求响应工具
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 17:04    1.0.0             HTTP 请求响应工具
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 一句话功能简述:HTTP 请求响应工具
 *
 * @author Blare
 * @create 2021/4/13 17:04
 * @since 1.0.0
 */
public class RequestResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponse.class);
    private static final String STR_BODY = "body";

    /**
     * 功能描述: 获取HTTP所有请求参数
     *
     * @param request HTTP请求
     * @return {@link Map}
     * @method getRequestParameters
     * @author Blare
     * @date 2021/4/13 17:33
     * @updator Blare
     */
    public static Map<String, String> getRequestParameters(ServletRequest request) {
        Map<String, String> dataMap = new HashMap<>(16);
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paraName = enums.nextElement();
            String paraValue = RequestResponse.getRequest(request).getParameter(paraName);
            if (null != paraValue && !paraValue.isBlank()) {
                dataMap.put(paraName, paraValue);
            }
        }
        return dataMap;
    }

    /**
     * 功能描述: 获取HTTP请求体参数
     *
     * @param request HTTP请求
     * @return {@link Map}
     * @method getRequestBodyMap
     * @author Blare
     * @date 2021/4/13 17:31
     * @updator Blare
     */
    public static Map<String, String> getRequestBodyMap(ServletRequest request) {
        Map<String, String> dataMap = new HashMap<>(16);
        // 判断是否已经将 inputStream 流中的 body 数据读出放入 attribute
        if (request.getAttribute(STR_BODY) != null) {
            // 已经读出则返回attribute中的body
            return (Map<String, String>) request.getAttribute(STR_BODY);
        } else {
            try {
                Map<String, String> maps = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                //Map<String,String > maps = JSON.parseObject(request.getInputStream(),Map.class);
                dataMap.putAll(maps);
                request.setAttribute(STR_BODY, dataMap);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return dataMap;
        }
    }

    /**
     * 功能描述: 获取HTTP指定请求参数
     *
     * @param request HTTP请求
     * @param key     参数键值
     * @return {@link String}
     * @method getParameter
     * @author Blare
     * @date 2021/4/13 17:30
     * @updator Blare
     */
    public static String getParameter(ServletRequest request, String key) {
        return RequestResponse.getRequest(request).getParameter(key);
    }

    /**
     * 功能描述: 获取HTTP指定头信息
     *
     * @param request HTTP 请求
     * @param key     请求头键值
     * @return {@link String}
     * @method getHeader
     * @author Blare
     * @date 2021/4/13 17:28
     * @updator Blare
     */
    public static String getHeader(ServletRequest request, String key) {
        return RequestResponse.getRequest(request).getHeader(key);
    }

    /**
     * 功能描述: 获取HTTP所有请求头信息
     *
     * @param request HTTP 请求
     * @return {@link Map}
     * @method getRequestHeaders
     * @author Blare
     * @date 2021/4/13 17:27
     * @updator Blare
     */
    public static Map<String, String> getRequestHeaders(ServletRequest request) {
        Map<String, String> headerMap = new HashMap<>(16);
        Enumeration<String> enums = RequestResponse.getRequest(request).getHeaderNames();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            String value = RequestResponse.getRequest(request).getHeader(name);
            if (null != value && !value.isBlank()) {
                headerMap.put(name, value);
            }
        }
        return headerMap;
    }

    /**
     * 功能描述: 获取HTTP请求封装对象->自定义封装
     *
     * @param request HTTP请求
     * @return {@link HttpServletRequest}
     * @method getRequest
     * @author Blare
     * @date 2021/4/13 17:26
     * @updator Blare
     */
    public static HttpServletRequest getRequest(ServletRequest request) {
        return new HttpServletRequestWrapper((HttpServletRequest) request);
    }


    /**
     * 功能描述: 封装response  统一json返回
     *
     * @param outStr   输出结果
     * @param response HTTP响应对象
     * @method responseWrite
     * @author Blare
     * @date 2021/4/13 17:24
     * @updator Blare
     */
    public static void responseWrite(String outStr, ServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter = null;
        try {
            printWriter = ((HttpServletResponse) response).getWriter();
            printWriter.write(outStr);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
