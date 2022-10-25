package cn.lili.modules.item.Util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcpService {
    public AcpService() {
    }

    public static Map<String, String> sign(Map<String, String> reqData, String encoding) {
        Map<String, String> submitData = SDKUtil.filterBlank(reqData);
        SDKUtil.sign(submitData, encoding);
        return submitData;
    }

    public static Map<String, String> sign(Map<String, String> reqData, String certPath, String certPwd, String encoding) {
        Map<String, String> submitData = SDKUtil.filterBlank(reqData);
        SDKUtil.signByCertInfo(submitData, certPath, certPwd, encoding);
        return submitData;
    }

    public static Map<String, String> signWithAlgorithms(Map<String, String> reqData, String certPath, String certPwd, String encoding, String algorithms) {
        Map<String, String> submitData = SDKUtil.filterBlank(reqData);
        SDKUtil.signByCertInfo(submitData, certPath, certPwd, encoding, algorithms);
        return submitData;
    }

    public static boolean validate(Map<String, String> rspData, String encoding) {
        LogUtil.writeLog("验签处理开始");
        if (SDKUtil.isEmpty(encoding)) {
            encoding = "UTF-8";
        }

        String stringSign = (String)rspData.get("signature");
        String certId = (String)rspData.get("certId");
        LogUtil.writeLog("对返回报文串验签使用的验签公钥序列号：[" + certId + "]");
        String stringData = SDKUtil.coverMap2String(rspData);
        LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");

        try {
            return SecureUtil.validateSignBySoft(CUPACPCertUtil.getValidateKey(certId), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData, encoding));
        } catch (UnsupportedEncodingException var6) {
            LogUtil.writeErrorLog(var6.getMessage(), var6);
        } catch (Exception var7) {
            LogUtil.writeErrorLog(var7.getMessage(), var7);
        }

        return false;
    }

    public static boolean validateAppResponse(String jsonData, String encoding) {
        LogUtil.writeLog("控件应答信息验签处理开始：[" + jsonData + "]");
        if (SDKUtil.isEmpty(encoding)) {
            encoding = "UTF-8";
        }

        Pattern p = Pattern.compile("\\s*\"sign\"\\s*:\\s*\"([^\"]*)\"\\s*");
        Matcher m = p.matcher(jsonData);
        if (!m.find()) {
            return false;
        } else {
            String sign = m.group(1);
            p = Pattern.compile("\\s*\"data\"\\s*:\\s*\"([^\"]*)\"\\s*");
            m = p.matcher(jsonData);
            if (!m.find()) {
                return false;
            } else {
                String data = m.group(1);
                p = Pattern.compile("cert_id=(\\d*)");
                m = p.matcher(jsonData);
                if (!m.find()) {
                    return false;
                } else {
                    String certId = m.group(1);

                    try {
                        return SecureUtil.validateSignBySoft(CUPACPCertUtil.getValidateKey(certId), SecureUtil.base64Decode(sign.getBytes(encoding)), SecureUtil.sha1X16(data, encoding));
                    } catch (UnsupportedEncodingException var8) {
                        LogUtil.writeErrorLog(var8.getMessage(), var8);
                    } catch (Exception var9) {
                        LogUtil.writeErrorLog(var9.getMessage(), var9);
                    }

                    return false;
                }
            }
        }
    }

    public static Map<String, String> post(Map<String, String> reqData, String reqUrl, String encoding) {
        Map<String, String> rspData = new HashMap();
        LogUtil.writeLog("请求银联地址:" + reqUrl);
        HttpClient hc = new HttpClient(reqUrl, 30000, 30000);

        try {
            LogUtil.writeLog("HttpClient -----send开始");
            int status = hc.send(reqData, encoding);
            if (200 == status) {
                String resultString = hc.getResult();
                LogUtil.writeLog("post请求返回内容:[" + resultString + "]");
                if (resultString != null && !"".equals(resultString)) {
                    Map<String, String> tmpRspData = SDKUtil.convertResultStringToMap(resultString);
                    rspData.putAll(tmpRspData);
                }
            } else {
                LogUtil.writeLog("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
            }
        } catch (Exception var8) {
            LogUtil.writeLog(var8.getMessage());
            System.out.println(var8.getMessage());
            var8.printStackTrace();
        }

        return rspData;
    }

    public static String get(String reqUrl, String encoding) {
        LogUtil.writeLog("请求银联地址:" + reqUrl);
        HttpClient hc = new HttpClient(reqUrl, 30000, 30000);

        try {
            int status = hc.sendGet(encoding);
            if (200 == status) {
                String resultString = hc.getResult();
                if (resultString != null && !"".equals(resultString)) {
                    return resultString;
                }
            } else {
                LogUtil.writeLog("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return null;
    }

    public static String createAutoFormHtml(String reqUrl, Map<String, String> hiddens, String encoding) {
        StringBuffer sf = new StringBuffer();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"" + reqUrl + "\" method=\"post\">");
        if (hiddens != null && hiddens.size() != 0) {
            Set<Map.Entry<String, String>> set = hiddens.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();

            while(it.hasNext()) {
                Map.Entry<String, String> ey = (Map.Entry)it.next();
                String key = (String)ey.getKey();
                String value = (String)ey.getValue();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + value + "\"/>");
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

    public static String enCodeFileContent(String filePath, String encoding) {
        String baseFileContent = "";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var17) {
                var17.printStackTrace();
            }
        }

        InputStream in = null;

        try {
            in = new FileInputStream(file);
            int fl = in.available();
            if (in != null) {
                byte[] s = new byte[fl];
                in.read(s, 0, fl);
                baseFileContent = new String(SecureUtil.base64Encode(SecureUtil.deflater(s)), encoding);
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

        }

        return baseFileContent;
    }

    public static void deCodeFileContent(Map<String, String> resData, String fileDirectory, String encoding) {
        String fileContent = (String)resData.get("fileContent");
        if (fileContent != null && !"".equals(fileContent)) {
            try {
                byte[] fileArray = SecureUtil.inflater(SecureUtil.base64Decode(fileContent.getBytes(encoding)));
                String filePath = null;
                if (SDKUtil.isEmpty((String)resData.get("fileName"))) {
                    filePath = fileDirectory + File.separator + (String)resData.get("merId") + "_" + (String)resData.get("batchNo") + "_" + (String)resData.get("txnTime") + ".txt";
                } else {
                    filePath = fileDirectory + File.separator + (String)resData.get("fileName");
                }

                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }

                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                out.write(fileArray, 0, fileArray.length);
                out.flush();
                out.close();
            } catch (UnsupportedEncodingException var8) {
                var8.printStackTrace();
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

    }

    public static String getFileContent(String fileContent, String encoding) {
        String fc = "";

        try {
            fc = new String(SecureUtil.inflater(SecureUtil.base64Decode(fileContent.getBytes())), encoding);
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return fc;
    }

    public static String getCustomerInfo(Map<String, String> customerInfoMap, String accNo, String encoding) {
        if (customerInfoMap.isEmpty()) {
            return "{}";
        } else {
            StringBuffer sf = new StringBuffer("{");
            Iterator<String> it = customerInfoMap.keySet().iterator();

            while(it.hasNext()) {
                String key = (String)it.next();
                String value = (String)customerInfoMap.get(key);
                if (key.equals("pin")) {
                    if (accNo == null || "".equals(accNo.trim())) {
                        LogUtil.writeLog("送了密码（PIN），必须在getCustomerInfoWithEncrypt参数中上传卡号");
                        return "{}";
                    }

                    value = encryptPin(accNo, value, encoding);
                }

                sf.append(key).append("=").append(value);
                if (it.hasNext()) {
                    sf.append("&");
                }
            }

            String customerInfo = sf.append("}").toString();
            LogUtil.writeLog("组装的customerInfo明文：" + customerInfo);

            try {
                return new String(SecureUtil.base64Encode(sf.toString().getBytes(encoding)), encoding);
            } catch (UnsupportedEncodingException var7) {
                var7.printStackTrace();
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            return customerInfo;
        }
    }

    public static String getCustomerInfoWithEncrypt(Map<String, String> customerInfoMap, String accNo, String encoding) {
        if (customerInfoMap.isEmpty()) {
            return "{}";
        } else {
            StringBuffer sf = new StringBuffer("{");
            StringBuffer encryptedInfoSb = new StringBuffer("");
            Iterator<String> it = customerInfoMap.keySet().iterator();

            while(true) {
                while(it.hasNext()) {
                    String key = (String)it.next();
                    String value = (String)customerInfoMap.get(key);
                    if (!key.equals("phoneNo") && !key.equals("cvn2") && !key.equals("expired")) {
                        if (key.equals("pin")) {
                            if (accNo == null || "".equals(accNo.trim())) {
                                LogUtil.writeLog("送了密码（PIN），必须在getCustomerInfoWithEncrypt参数中上传卡号");
                                return "{}";
                            }

                            value = encryptPin(accNo, value, encoding);
                        }

                        sf.append(key).append("=").append(value).append("&");
                    } else {
                        encryptedInfoSb.append(key).append("=").append(value).append("&");
                    }
                }

                if (!encryptedInfoSb.toString().equals("")) {
                    encryptedInfoSb.setLength(encryptedInfoSb.length() - 1);
                    LogUtil.writeLog("组装的customerInfo encryptedInfo明文：" + encryptedInfoSb.toString());
                    sf.append("encryptedInfo").append("=").append(encryptData(encryptedInfoSb.toString(), encoding));
                } else {
                    sf.setLength(sf.length() - 1);
                }

                String customerInfo = sf.append("}").toString();
                LogUtil.writeLog("组装的customerInfo明文：" + customerInfo);

                try {
                    return new String(SecureUtil.base64Encode(sf.toString().getBytes(encoding)), encoding);
                } catch (UnsupportedEncodingException var8) {
                    var8.printStackTrace();
                } catch (IOException var9) {
                    var9.printStackTrace();
                }

                return customerInfo;
            }
        }
    }

    public static String getCustomerInfoWithEncrypt(Map<String, String> customerInfoMap, String accNo, String encoding, String certpath) {
        if (customerInfoMap.isEmpty()) {
            return "{}";
        } else {
            StringBuffer sf = new StringBuffer("{");
            StringBuffer encryptedInfoSb = new StringBuffer("");
            Iterator<String> it = customerInfoMap.keySet().iterator();

            while(true) {
                while(it.hasNext()) {
                    String key = (String)it.next();
                    String value = (String)customerInfoMap.get(key);
                    if (!key.equals("phoneNo") && !key.equals("cvn2") && !key.equals("expired")) {
                        if (key.equals("pin")) {
                            if (accNo == null || "".equals(accNo.trim())) {
                                LogUtil.writeLog("送了密码（PIN），必须在getCustomerInfoWithEncrypt参数中上传卡号");
                                return "{}";
                            }

                            value = encryptPin(accNo, value, encoding);
                        }

                        sf.append(key).append("=").append(value).append("&");
                    } else {
                        encryptedInfoSb.append(key).append("=").append(value).append("&");
                    }
                }

                if (!encryptedInfoSb.toString().equals("")) {
                    encryptedInfoSb.setLength(encryptedInfoSb.length() - 1);
                    LogUtil.writeLog("组装的customerInfo encryptedInfo明文：" + encryptedInfoSb.toString());
                    sf.append("encryptedInfo").append("=").append(encryptData(encryptedInfoSb.toString(), encoding, certpath));
                } else {
                    sf.setLength(sf.length() - 1);
                }

                String customerInfo = sf.append("}").toString();
                LogUtil.writeLog("组装的customerInfo明文：" + customerInfo);

                try {
                    return new String(SecureUtil.base64Encode(sf.toString().getBytes(encoding)), encoding);
                } catch (UnsupportedEncodingException var9) {
                    var9.printStackTrace();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }

                return customerInfo;
            }
        }
    }

    public static Map<String, String> parseCustomerInfo(String customerInfo, String encoding) {
        Map<String, String> customerInfoMap = null;

        try {
            byte[] b = SecureUtil.base64Decode(customerInfo.getBytes(encoding));
            String customerInfoNoBase64 = new String(b, encoding);
            LogUtil.writeLog("解base64后===>" + customerInfoNoBase64);
            customerInfoNoBase64 = customerInfoNoBase64.substring(1, customerInfoNoBase64.length() - 1);
            customerInfoMap = SDKUtil.parseQString(customerInfoNoBase64);
            if (customerInfoMap.containsKey("encryptedInfo")) {
                String encInfoStr = (String)customerInfoMap.get("encryptedInfo");
                customerInfoMap.remove("encryptedInfo");
                String encryptedInfoStr = decryptData(encInfoStr, encoding);
                Map<String, String> encryptedInfoMap = SDKUtil.parseQString(encryptedInfoStr);
                customerInfoMap.putAll(encryptedInfoMap);
            }
        } catch (UnsupportedEncodingException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return customerInfoMap;
    }

    public static String getCardTransData(Map<String, ?> contentData, String encoding) {
        StringBuffer cardTransDataBuffer = new StringBuffer();
        String ICCardData = "uduiadniodaiooxnnxnnada";
        String ICCardSeqNumber = "123";
        String track2Data = "testtrack2Datauidanidnaidiadiada231";
        String track3Data = "testtrack3Datadadaiiuiduiauiduia312117831";
        String transSendMode = "b";
        StringBuffer track2Buffer = new StringBuffer();
        track2Buffer.append(contentData.get("merId")).append("|").append(contentData.get("orderId")).append("|").append(contentData.get("txnTime")).append("|").append(contentData.get("txnAmt")).append("|").append(track2Data);
        String encryptedTrack2 = encryptTrack(track2Buffer.toString(), encoding);
        StringBuffer track3Buffer = new StringBuffer();
        track3Buffer.append(contentData.get("merId")).append("|").append(contentData.get("orderId")).append("|").append(contentData.get("txnTime")).append("|").append(contentData.get("txnAmt")).append("|").append(track3Data);
        String encryptedTrack3 = encryptTrack(track3Buffer.toString(), encoding);
        Map<String, String> cardTransDataMap = new HashMap();
        cardTransDataMap.put("ICCardData", ICCardData);
        cardTransDataMap.put("ICCardSeqNumber", ICCardSeqNumber);
        cardTransDataMap.put("track2Data", encryptedTrack2);
        cardTransDataMap.put("track3Data", encryptedTrack3);
        cardTransDataMap.put("transSendMode", transSendMode);
        return cardTransDataBuffer.append("{").append(SDKUtil.coverMap2String(cardTransDataMap)).append("}").toString();
    }

    public static String encryptPin(String accNo, String pwd, String encoding) {
        return SecureUtil.EncryptPin(pwd, accNo, encoding, CUPACPCertUtil.getEncryptCertPublicKey());
    }

    public static String encryptData(String data, String encoding) {
        return SecureUtil.EncryptData(data, encoding, CUPACPCertUtil.getEncryptCertPublicKey());
    }

    public static String encryptData(String data, String encoding, String certpath) {
        return SecureUtil.EncryptDataNew(data, encoding, CUPACPCertUtil.getEncryptCertPublicKeyByPath(certpath));
    }

    public static String decryptData(String base64EncryptedInfo, String encoding) {
        return SecureUtil.DecryptedData(base64EncryptedInfo, encoding, CUPACPCertUtil.getSignCertPrivateKey());
    }

    public static String encryptTrack(String trackData, String encoding) {
        return SecureUtil.EncryptData(trackData, encoding, CUPACPCertUtil.getEncryptTrackPublicKey());
    }

    public static String encryptTrack(String trackData, String encoding, String modulus, String exponent) {
        return SecureUtil.EncryptData(trackData, encoding, CUPACPCertUtil.getEncryptTrackCertPublicKey(modulus, exponent));
    }

    public static String getEncryptCertId() {
        return CUPACPCertUtil.getEncryptCertId();
    }

    public static String base64Encode(String rawStr, String encoding) throws IOException {
        byte[] rawByte = rawStr.getBytes(encoding);
        return new String(SecureUtil.base64Encode(rawByte), encoding);
    }

    public static String base64Decode(String base64Str, String encoding) throws IOException {
        byte[] rawByte = base64Str.getBytes(encoding);
        return new String(SecureUtil.base64Decode(rawByte), encoding);
    }

    public static boolean validateByBySoft256(Map<String, String> resData, String encoding) {
        LogUtil.writeLog("验签处理开始");
        String strCert = (String)resData.get("signPubKeyCert");
        X509Certificate x509Cert = CertUtil.genCertificateByStr(strCert);
        if (x509Cert == null) {
            LogUtil.writeErrorLog("convert signPubKeyCert failed");
            return false;
        } else if (!CertUtil.verifyCertificate(x509Cert)) {
            LogUtil.writeErrorLog("验证公钥证书失败，证书信息：[" + strCert + "]");
            return false;
        } else {
            String stringSign = (String)resData.get("signature");
            LogUtil.writeLog("签名原文：[" + stringSign + "]");
            String stringData = coverMap2String(resData);
            LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");

            try {
                boolean result = SecureUtil.validateSignBySoft256(x509Cert.getPublicKey(), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha256X16(stringData, encoding));
                LogUtil.writeLog("验证签名" + (result ? "成功" : "失败"));
                return result;
            } catch (UnsupportedEncodingException var7) {
                LogUtil.writeErrorLog(var7.getMessage(), var7);
            } catch (Exception var8) {
                LogUtil.writeErrorLog(var8.getMessage(), var8);
            }

            return false;
        }
    }

    public static String coverMap2String(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap();
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, String> en = (Map.Entry)it.next();
            if (!"signature".equals(((String)en.getKey()).trim())) {
                tree.put((String)en.getKey(), (String)en.getValue());
            }
        }

        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();

        while(it.hasNext()) {
            Map.Entry<String, String> en = (Map.Entry)it.next();
            sf.append((String)en.getKey() + "=" + (String)en.getValue() + "&");
        }

        return sf.substring(0, sf.length() - 1);
    }
}
