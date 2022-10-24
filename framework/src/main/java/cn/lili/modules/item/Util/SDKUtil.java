package cn.lili.modules.item.Util;
import cn.lili.common.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

//import cn.hutool.crypto.SecureUtil;
//import org.apache.commons.lang.StringUtils;
public class SDKUtil {
    public SDKUtil() {
    }

    public static boolean sign(Map<String, String> data, String encoding) {
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }

        data.put("certId", CUPACPCertUtil.getSignCertId());
        String stringData = coverMap2String(data);
        LogUtil.writeLog("待签名请求报文串:[" + stringData + "]");
//        byte[] byteSign = null;
        String stringSign = null;

        try {
            byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
            byte[] byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CUPACPCertUtil.getSignCertPrivateKey(), signDigest));
            stringSign = new String(byteSign);
            data.put("signature", stringSign);
            return true;
        } catch (Exception var6) {
            LogUtil.writeErrorLog("签名异常", var6);
            return false;
        }
    }

    public static boolean signByCertInfo(Map<String, String> data, String certPath, String certPwd, String encoding) {
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }

        if (!isEmpty(certPath) && !isEmpty(certPwd)) {
            LogUtil.writeLog("位置：SDKUtil-signByCertInfo==>设置签名证书序列号====开始");
            data.put("certId", CUPACPCertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
            LogUtil.writeLog("位置：SDKUtil-signByCertInfo==>设置签名证书序列号====结束");
            String stringData = coverMap2String(data);
//            byte[] byteSign = null;
            String stringSign = null;

            try {
                byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
                byte[] byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CUPACPCertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
                stringSign = new String(byteSign);
                data.put("signature", stringSign);
                return true;
            } catch (Exception var8) {
                LogUtil.writeErrorLog("签名异常", var8);
                return false;
            }
        } else {
            LogUtil.writeLog("Invalid Parameter:CertPath=[" + certPath + "],CertPwd=[" + certPwd + "]");
            return false;
        }
    }

    public static boolean signByCertInfo(Map<String, String> data, String certPath, String certPwd, String encoding, String signMethod) {
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }

        if (!isEmpty(certPath) && !isEmpty(certPwd)) {
            LogUtil.writeLog("位置：SDKUtil-signByCertInfo==>设置签名证书序列号====开始");
            data.put("certId", CUPACPCertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
            LogUtil.writeLog("位置：SDKUtil-signByCertInfo==>设置签名证书序列号====结束");
            String stringData = coverMap2String(data);
//            byte[] byteSign = null;
            String stringSign = null;
//            byte[] signDigest = null;

            try {
                byte[] byteSign;
                byte[] signDigest;
                if (signMethod.equalsIgnoreCase("SHA256")) {
                    signDigest = SecureUtil.sha256X16(stringData, encoding);
                    byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft256(CUPACPCertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
                } else {
                    if (!signMethod.equalsIgnoreCase("SHA1")) {
                        LogUtil.writeErrorLog("signMethod不合法，合法值为【SHA256,SHA1】");
                        return false;
                    }

                    signDigest = SecureUtil.sha1X16(stringData, encoding);
                    byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CUPACPCertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
                }

                stringSign = new String(byteSign);
                data.put("signature", stringSign);
                return true;
            } catch (Exception var10) {
                LogUtil.writeErrorLog("签名异常", var10);
                return false;
            }
        } else {
            LogUtil.writeLog("Invalid Parameter:CertPath=[" + certPath + "],CertPwd=[" + certPwd + "]");
            return false;
        }
    }

    public static boolean validate(Map<String, String> resData, String encoding) {
        LogUtil.writeLog("验签处理开始.");
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }

        String stringSign = (String)resData.get("signature");
        String certId = (String)resData.get("certId");
        String stringData = coverMap2String(resData);

        try {
            return SecureUtil.validateSignBySoft(CUPACPCertUtil.getValidateKey(certId), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData, encoding));
        } catch (UnsupportedEncodingException var6) {
            LogUtil.writeErrorLog(var6.getMessage(), var6);
        } catch (Exception var7) {
            LogUtil.writeErrorLog(var7.getMessage(), var7);
        }

        return false;
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

    public static Map<String, String> coverResultString2Map(String result) {
        return convertResultStringToMap(result);
    }

    public static Map<String, String> convertResultStringToMap(String result) {
        Map<String, String> map = null;

        try {
            if (StringUtils.isNotBlank(result)) {
                if (result.startsWith("{") && result.endsWith("}")) {
                    System.out.println(result.length());
                    result = result.substring(1, result.length() - 1);
                }

                map = parseQString(result);
            }
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        return map;
    }

    public static Map<String, String> parseQString(String str) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;
        char openName = 0;
        if (len > 0) {
            for(int i = 0; i < len; ++i) {
                char curChar = str.charAt(i);
                if (isKey) {
                    if (curChar == '=') {
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else {
                    if (isOpen) {
                        if (curChar == openName) {
                            isOpen = false;
                        }
                    } else {
                        if (curChar == '{') {
                            isOpen = true;
                            openName = 125;
                        }

                        if (curChar == '[') {
                            isOpen = true;
                            openName = 93;
                        }
                    }

                    if (curChar == '&' && !isOpen) {
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }
            }

            putKeyValueToMap(temp, isKey, key, map);
        }

        return map;
    }

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) throws UnsupportedEncodingException {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }

            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }

            map.put(key, temp.toString());
        }

    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static Map<String, String> filterBlank(Map<String, String> contentData) {
        LogUtil.writeLog("打印请求报文域 :");
        Map<String, String> submitFromData = new HashMap();
        Set<String> keyset = contentData.keySet();
        Iterator var4 = keyset.iterator();

        while(var4.hasNext()) {
            String key = (String)var4.next();
            String value = (String)contentData.get(key);
            if (StringUtils.isNotBlank(value)) {
                submitFromData.put(key, value.trim());
                LogUtil.writeLog(key + "-->" + value);
            }
        }

        return submitFromData;
    }
}
