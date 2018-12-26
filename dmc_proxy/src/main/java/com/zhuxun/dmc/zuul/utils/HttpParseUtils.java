package com.zhuxun.dmc.zuul.utils;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import io.netty.util.CharsetUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作用：HttpServlet的请求参数解析工具类
 *
 * <p>时间：18-7-9 上午10:10
 *
 * <p>位置：com.zhuxun.dmc.zuul.utils
 *
 * @author Yan - tao
 */
public class HttpParseUtils {

    /**
     * 获取查询参数
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException URLDecoder 解码的时候可能出现的异常
     */
    public static Map<String, String> parserQueryString(HttpServletRequest request) throws UnsupportedEncodingException {
        String queryString = request.getQueryString();
        return parserString(queryString);
    }

    /**
     * 获取请求头参数
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException URLDecoder 解码的时候可能出现的异常
     */
    public static Map<String, String> parseHeaders(HttpServletRequest request) throws UnsupportedEncodingException {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder queryAndHeader = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerKey = headerNames.nextElement();
            String header = request.getHeader(headerKey);
            queryAndHeader.append("&").append(headerKey).append("=").append(header);
        }
        return parserString(queryAndHeader.toString());
    }


    /**
     * 解析字符串参数信息
     *
     * @param queryString
     * @return
     * @throws UnsupportedEncodingException URLDecoder 解码的时候可能出现的异常
     */
    public static Map<String, String> parserString(String queryString) throws UnsupportedEncodingException {
        if (Strings.isNullOrEmpty(queryString)) {
            return new HashMap<String, String>();
        }
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = queryString.split("&");
        JsonArray jsonArray = new JsonArray();
        for (String pair : pairs) {
            // 防止一些测试工具提交的空字符串数据导致的异常
            if (Strings.isNullOrEmpty(pair) || !pair.contains("=")){
                continue;
            }
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), CharsetUtil.UTF_8.displayName()),
                    URLDecoder.decode(pair.substring(idx + 1), CharsetUtil.UTF_8.displayName()));
        }
        JsonArray array = new JsonArray();
        array.add(new JsonArray());
        return query_pairs;
    }


    /**
     * 从HttpServletRequest中获取请求体
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String parseRequestBody(HttpServletRequest request) throws IOException {
        if (request.getReader() == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = request.getReader()) {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }


}
