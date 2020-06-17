package com.im.phone.server.system;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequestUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    private static final int connectTimeout = 1000 * 120;    // 连接超时时间
    private static final int socketTimeout = 1000 * 180;    // 读取数据超时时间

    /**
     * 向指定 URL 发送 POST请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数，格式 name1=value1&name2=value2
     * @return 远程资源的响应结果
     */
    public static String sendPost(String strUrl, String requestParams) {
        logger.info("sendPost strUrl:" + strUrl);
        logger.info("sendPost requestParams:" + requestParams);

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Accept", "application/json");    // 设置接收数据的格式
            httpURLConnection.setRequestProperty("Content-Type", "application/json");  // 设置发送数据的格式
            httpURLConnection.connect();    // 建立连接
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    httpURLConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.append(requestParams);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            // 使用BufferedReader输入流来读取URL的响应
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String strLine = "";
            while ((strLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(strLine);
            }
            bufferedReader.close();
            String responseParams = stringBuffer.toString();

            logger.info("sendPost responseParams:" + responseParams);

            return responseParams;
        } catch (IOException e) {
            logger.info("sendPost IOException:" + e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * HttpClientPost 方式，向指定 URL 发送 POST请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数
     * @return 远程资源的响应结果
     */
    public static String doPost(String strUrl, List<BasicNameValuePair> requestParams) {
        logger.info("doPost strUrl:" + strUrl);
        logger.info("doPost requestParams:" + requestParams);

        String responseParams = "";
        StringBuffer stringBuffer = new StringBuffer();
        long startTime = 0, endTime = 0;

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();    // 设置请求和传输超时时间

        HttpPost httpPost = new HttpPost(strUrl);
        httpPost.setConfig(requestConfig);
        HttpEntity httpEntity;

        try {
            if (requestParams != null) {
                // 设置相关参数
                httpEntity = new UrlEncodedFormEntity(requestParams, "UTF-8");
                httpPost.setEntity(httpEntity);

                logger.info("doPost requestParams:" + EntityUtils.toString(httpEntity));
            }
            startTime = System.nanoTime();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            int code = closeableHttpResponse.getStatusLine().getStatusCode();

            logger.info("doPost 状态码:" + code);

            if (code == 200 || code == 500) {
                try {
                    httpEntity = closeableHttpResponse.getEntity();
                    if (httpEntity != null) {
                        long length = httpEntity.getContentLength();
                        // 当返回值长度较小的时候，使用工具类读取
                        if (length != -1 && length < 2048) {
                            stringBuffer.append(EntityUtils.toString(httpEntity));
                        } else {    // 否则使用IO流来读取
                            BufferedReader bufferedReader = new BufferedReader(
                                    new InputStreamReader(httpEntity.getContent(), "UTF-8"));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                stringBuffer.append(line);
                            }
                            bufferedReader.close();
                            responseParams = stringBuffer.toString();
                        }
                        endTime = System.nanoTime();
                    }
                } catch (Exception e) {
                    endTime = System.nanoTime();

                    logger.info("doPost Exception（通讯错误）:" + e.getMessage());
                } finally {
                    closeableHttpResponse.close();
                }
            } else {
                endTime = System.nanoTime();
                httpPost.abort();

                logger.info("doPost 错误请求，状态码:" + code);
            }
        } catch (IOException e) {
            endTime = System.nanoTime();

            logger.info("doPost IOException:" + e.getMessage());
        } finally {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
            }
        }

        logger.info("doPost 用时（毫秒）:" + (endTime - startTime) / 1000000L);
        logger.info("doPost responseParams:" + responseParams);

        return responseParams;
    }

    /**
     * 向指定 URL 发送 GET请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数
     * @return 远程资源的响应结果
     */
    public static String sendGet(String strUrl, String requestParams) {
        logger.info("sendGet strUrl:" + strUrl);
        logger.info("sendGet requestParams:" + requestParams);

        String responseParams = "";
        BufferedReader bufferedReader = null;
        try {
            String strRequestUrl = strUrl + "?" + requestParams;
            URL url = new URL(strRequestUrl);
            URLConnection urlConnection = url.openConnection();    // 打开与 URL 之间的连接

            // 设置通用的请求属性
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            urlConnection.connect();    // 建立连接

            Map<String, List<String>> map = urlConnection.getHeaderFields();    // 获取所有响应头字段

            // 使用BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                responseParams += strLine;
            }
        } catch (Exception e) {
            logger.info("sendGet Exception:" + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        logger.info("sendPost responseParams:" + responseParams);

        return responseParams;
    }
}
