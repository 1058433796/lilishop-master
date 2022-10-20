package cn.lili.modules.item.Util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

public class HttpClient {
    private URL url;
    private int connectionTimeout;
    private int readTimeOut;
    private String result;
    private Logger loga = Logger.getLogger(String.valueOf(this.getClass()));

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HttpClient(String url, int connectionTimeout, int readTimeOut) {
        try {
            this.url = new URL(url);
            this.connectionTimeout = connectionTimeout;
            this.readTimeOut = readTimeOut;
        } catch (MalformedURLException var5) {
            var5.printStackTrace();
        }

    }

    public int send(Map<String, String> data, String encoding) throws Exception {
        try {
            LogUtil.writeLog("HttpClient创建联接");
            HttpURLConnection httpURLConnection = this.createConnection(encoding);
            if (httpURLConnection == null) {
                LogUtil.writeLog("HttpClient创建联接失败");
                this.loga.info("创建联接失败");
                throw new Exception("创建联接失败");
            } else {
                String sendData = this.getRequestParamString(data, encoding);
                this.loga.info("请求报文:[" + sendData + "]");
                LogUtil.writeLog("请求报文:[" + sendData + "]");
                this.requestServer(httpURLConnection, sendData, encoding);
                this.result = this.response(httpURLConnection, encoding);
                this.loga.info("同步返回报文:[" + this.result + "]");
                LogUtil.writeLog("同步返回报文:[" + this.result + "]");
                return httpURLConnection.getResponseCode();
            }
        } catch (Exception var5) {
            this.loga.info(var5.getMessage());
            throw var5;
        }
    }

    public int sendGet(String encoding) throws Exception {
        try {
            HttpURLConnection httpURLConnection = this.createConnectionGet(encoding);
            if (httpURLConnection == null) {
                throw new Exception("创建联接失败");
            } else {
                this.result = this.response(httpURLConnection, encoding);
                LogUtil.writeLog("同步返回报文:[" + this.result + "]");
                return httpURLConnection.getResponseCode();
            }
        } catch (Exception var3) {
            throw var3;
        }
    }

    private void requestServer(URLConnection connection, String message, String encoder) throws Exception {
        PrintStream out = null;

        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoder);
            out.print(message);
            out.flush();
        } catch (Exception var9) {
            throw var9;
        } finally {
            if (out != null) {
                out.close();
            }

        }

    }

    private String response(HttpURLConnection connection, String encoding) throws URISyntaxException, IOException, Exception {
        InputStream in = null;
        StringBuilder sb = new StringBuilder(1024);
        BufferedReader br = null;

        String var8;
        try {
            if (200 == connection.getResponseCode()) {
                in = connection.getInputStream();
                LogUtil.writeLog("返回状态:=======" + connection.getResponseCode());
                sb.append(new String(read(in), encoding));
            } else {
                in = connection.getErrorStream();
                LogUtil.writeLog("返回状态:=======" + connection.getResponseCode());
                sb.append(new String(read(in), encoding));
            }

            LogUtil.writeLog("HTTP Return Status-Code:[" + connection.getResponseCode() + "]");
            var8 = sb.toString();
        } catch (Exception var11) {
            throw var11;
        } finally {
            if (br != null) {
                ((BufferedReader)br).close();
            }

            if (in != null) {
                in.close();
            }

            if (connection != null) {
                connection.disconnect();
            }

        }

        return var8;
    }

    public static byte[] read(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
//        int lengthgth = false;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int length;
        while((length = in.read(buf, 0, buf.length)) > 0) {
            bout.write(buf, 0, length);
        }

        bout.flush();
        return bout.toByteArray();
    }

    private HttpURLConnection createConnection(String encoding) throws ProtocolException {
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection)this.url.openConnection();
        } catch (IOException var4) {
            var4.printStackTrace();
            return null;
        }

        httpURLConnection.setConnectTimeout(this.connectionTimeout);
        httpURLConnection.setReadTimeout(this.readTimeOut);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
        httpURLConnection.setRequestMethod("POST");
        if ("https".equalsIgnoreCase(this.url.getProtocol())) {
            HttpsURLConnection husn = (HttpsURLConnection)httpURLConnection;
            husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            husn.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());
            return husn;
        } else {
            return httpURLConnection;
        }
    }

    private HttpURLConnection createConnectionGet(String encoding) throws ProtocolException {
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection)this.url.openConnection();
        } catch (IOException var4) {
            var4.printStackTrace();
            return null;
        }

        httpURLConnection.setConnectTimeout(this.connectionTimeout);
        httpURLConnection.setReadTimeout(this.readTimeOut);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
        httpURLConnection.setRequestMethod("GET");
        if ("https".equalsIgnoreCase(this.url.getProtocol())) {
            HttpsURLConnection husn = (HttpsURLConnection)httpURLConnection;
            husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            husn.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());
            return husn;
        } else {
            return httpURLConnection;
        }
    }

    private String getRequestParamString(Map<String, String> requestParam, String coder) {
        if (coder == null || "".equals(coder)) {
            coder = "UTF-8";
        }

        StringBuffer sf = new StringBuffer("");
        String reqstr = "";
        if (requestParam != null && requestParam.size() != 0) {
            Iterator var6 = requestParam.entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, String> en = (Map.Entry)var6.next();

                try {
                    sf.append((String)en.getKey() + "=" + (en.getValue() != null && !"".equals(en.getValue()) ? URLEncoder.encode((String)en.getValue(), coder) : "") + "&");
                } catch (UnsupportedEncodingException var8) {
                    var8.printStackTrace();
                    return "";
                }
            }

            reqstr = sf.substring(0, sf.length() - 1);
        }

        LogUtil.writeLog("请求报文(已做过URLEncode编码):[" + reqstr + "]");
        return reqstr;
    }
}
