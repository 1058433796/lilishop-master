package cn.lili.modules.item.Util;


import cn.lili.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;


//import com.dareway.framework.exception.BusinessException;


    public class CUPACPUtil {
        public final static String encoding = "UTF-8";
        public final static String version = "5.0.0";
        private static Logger loga = Logger.getLogger(String.valueOf(CUPACPUtil.class.getClass()));

        public static String createHtml(String action, Map<String, String> hiddens) {
            StringBuffer sf = new StringBuffer();
            sf
                    .append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>");
            sf.append("<form id = \"pay_form\" action=\"" + action
                    + "\" method=\"post\">");
            if (null != hiddens && 0 != hiddens.size()) {
                Set<Entry<String, String>> set = hiddens.entrySet();
                Iterator<Entry<String, String>> it = set.iterator();
                while (it.hasNext()) {
                    Entry<String, String> ey = it.next();
                    String key = ey.getKey();
                    String value = ey.getValue();
                    sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
                            + key + "\" value=\"" + value + "\"/>");
                }
            }
            sf.append("</form>");
            sf.append("</body>");
            sf.append("<script type=\"text/javascript\">");
            sf.append("document.all.pay_form.submit();");
            sf.append("</script>");
            sf.append("</html>");
            return sf.toString();
        }

        /**
         * java main方法 数据提交 　　 对数据进行签名
         *
         * @param contentData
         * @return　签名后的map对象
         */
        @SuppressWarnings("unchecked")
        public static Map<String, String> signData(Map<String, ?> contentData,
                                                   String certPath, String certPwd) {
            Entry<String, String> obj = null;
            Map<String, String> submitFromData = new HashMap<String, String>();
            for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
                obj = (Entry<String, String>) it.next();
                String value = obj.getValue();
                if (StringUtils.isNotBlank(value)) {
                    // 对value值进行去除前后空处理
                    submitFromData.put(obj.getKey(), value.trim());
                    LogUtil.writeLog(obj.getKey() + "-->" + String.valueOf(value));
                }
            }
            /**
             * 签名
             */
            LogUtil.writeLog("对数据进行签名！开始");
            boolean sign_falg = SDKUtil
                    .signByCertInfo(submitFromData, certPath, certPwd, encoding);
            LogUtil.writeLog("对数据进行签名！结束=== " + sign_falg);
            return submitFromData;
        }
        public static Map<String, String> signData(Map<String, ?> contentData,
                                                   String certPath, String certPwd,String signmethod) {
            Entry<String, String> obj = null;
            Map<String, String> submitFromData = new HashMap<String, String>();
            for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
                obj = (Entry<String, String>) it.next();
                String value = obj.getValue();
                if (StringUtils.isNotBlank(value)) {
                    // 对value值进行去除前后空处理
                    submitFromData.put(obj.getKey(), value.trim());
                    loga.info(obj.getKey() + "-->" + String.valueOf(value));
                }
            }
            /**
             * 签名
             */
            loga.info("对数据进行签名！开始");
            boolean sign_falg =SDKUtil
                    .signByCertInfo(submitFromData, certPath, certPwd, encoding,signmethod);
            loga.info("对数据进行签名！结束=== " + sign_falg);
            return submitFromData;
        }
        @SuppressWarnings("unchecked")
        public static Map<String, String> signData(Map<String, ?> contentData) {
            Entry<String, String> obj = null;
            Map<String, String> submitFromData = new HashMap<String, String>();
            for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
                obj = (Entry<String, String>) it.next();
                String value = obj.getValue();
                if (StringUtils.isNotBlank(value)) {
                    // 对value值进行去除前后空处理
                    submitFromData.put(obj.getKey(), value.trim());
                    loga.info(obj.getKey() + "-->" + String.valueOf(value));
                }
            }
            /**
             * 签名
             */
            SDKUtil.sign(submitFromData,
                    encoding);

            return submitFromData;
        }

        /**
         * java main方法 数据提交 提交到后台
         *
         *
         * @return 返回报文 map
         * @throws Exception
         */
        public static Map<String, String> submitUrl(
                Map<String, String> submitFromData, String requestUrl) throws Exception {
            String resultString = "";
            LogUtil.writeLog("requestUrl====" + requestUrl);
            LogUtil.writeLog("submitFromData====" + submitFromData.toString());
            /**
             * 发送
             */
            LogUtil.writeLog("HttpClient连接开始====");
            HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
            try {
                int status = hc.send(submitFromData, encoding);
                LogUtil.writeLog("HttpClient连接status====" + status);
                if (200 == status) {
                    resultString = hc.getResult();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("HttpClient连接失败：" + e.getMessage());
            }
            LogUtil.writeLog("HttpClient连接结束====");
            Map<String, String> resData = new HashMap<String, String>();
            /**
             * 验证签名
             */
            LogUtil.writeLog("验证签名====");
            if (null != resultString && !"".equals(resultString)) {
                // 将返回结果转换为map
                resData = SDKUtil
                        .convertResultStringToMap(resultString);
                if (SDKUtil.validate(resData,
                        encoding)) {
                    LogUtil.writeLog("验证签名成功");
                    loga.info("验证签名成功");
                } else {
                    LogUtil.writeLog("验证签名失败");
                    loga.info("验证签名失败");
                }
                // 打印返回报文
                LogUtil.writeLog("打印返回报文：" + resultString);
                loga.info("打印返回报文：" + resultString);
            }
            return resData;
        }

        public static Map<String, String> submitUrl(
                Map<String, String> submitFromData, String requestUrl,String signmethod) {
            String resultString = "";
            loga.info("requestUrl====" + requestUrl);
            loga.info("submitFromData====" + submitFromData.toString());
            /**
             * 发送
             */
            HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
            try {
                int status = hc.send(submitFromData, encoding);
                if (200 == status) {
                    resultString = hc.getResult();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, String> resData = new HashMap<String, String>();
            /**
             * 验证签名
             */
            if (null != resultString && !"".equals(resultString)) {
                // 将返回结果转换为map
                resData = SDKUtil
                        .convertResultStringToMap(resultString);
                if (signmethod.equals("SHA256")) {
                    if (AcpService.validateByBySoft256(resData, encoding)) {
                        loga.info("验证签名成功");
                    } else {
                        loga.info("验证签名失败");
                    }
                }else if (signmethod.equals("SHA1")) {
                    if (SDKUtil.validate(resData,
                            encoding)) {
                        loga.info("验证签名成功");
                    } else {
                        loga.info("验证签名失败");
                    }
                }else {

                }


                // 打印返回报文
                loga.info("打印返回报文：" + resultString);
            }
            return resData;
        }

        /**
         * java main方法 数据提交　 数据组装进行提交 包含签名
         *
         * @param contentData
         * @return 返回报文 map
         * @throws Exception
         */
        public static Map<String, String> submitDate(Map<String, ?> contentData,
                                                     String requestUrl, String certPath, String certPwd) throws Exception {

            Map<String, String> submitFromData = (Map<String, String>) signData(
                    contentData, certPath, certPwd);

            return submitUrl(submitFromData, requestUrl);
        }

        public static Map<String, String> submitDate(Map<String, ?> contentData,
                                                     String requestUrl, String certPath, String certPwd, String signmethod) {

            Map<String, String> submitFromData = (Map<String, String>) signData(
                    contentData, certPath, certPwd,signmethod);

            return submitUrl(submitFromData, requestUrl,signmethod);
        }

}
