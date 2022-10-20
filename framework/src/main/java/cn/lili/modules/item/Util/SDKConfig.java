package cn.lili.modules.item.Util;

import cn.lili.common.utils.StringUtils;

import java.io.*;
import java.util.Properties;

public class SDKConfig {
    public static final String FILE_NAME = "acp_sdk.properties";
    public static final String SDK_ROOTCERT_PATH = "acpsdk.rootCert.path";
    public static final String SDK_MIDDLECERT_PATH = "acpsdk.middleCert.path";
    private String frontRequestUrl;
    private String backRequestUrl;
    private String singleQueryUrl;
    private String batchQueryUrl;
    private String batchTransUrl;
    private String fileTransUrl;
    private String signCertPath;
    private String signCertPwd;
    private String signCertType;
    private String encryptCertPath;
    private String validateCertDir;
    private String signCertDir;
    private String encryptTrackCertPath;
    private String encryptTrackKeyModulus;
    private String encryptTrackKeyExponent;
    private String cardRequestUrl;
    private String appRequestUrl;
    private String singleMode;
    private String middleCertPath;
    private String rootCertPath;
    private boolean ifValidateCNName = false;
    private boolean ifValidateRemoteCert = false;
    private String signMethod = "01";
    private String jfFrontRequestUrl;
    private String jfBackRequestUrl;
    private String jfSingleQueryUrl;
    private String jfCardRequestUrl;
    private String jfAppRequestUrl;
    public static final String SDK_FRONT_URL = "acpsdk.frontTransUrl";
    public static final String SDK_BACK_URL = "acpsdk.backTransUrl";
    public static final String SDK_SIGNQ_URL = "acpsdk.singleQueryUrl";
    public static final String SDK_BATQ_URL = "acpsdk.batchQueryUrl";
    public static final String SDK_BATTRANS_URL = "acpsdk.batchTransUrl";
    public static final String SDK_FILETRANS_URL = "acpsdk.fileTransUrl";
    public static final String SDK_CARD_URL = "acpsdk.cardTransUrl";
    public static final String SDK_APP_URL = "acpsdk.appTransUrl";
    public static final String JF_SDK_FRONT_TRANS_URL = "acpsdk.jfFrontTransUrl";
    public static final String JF_SDK_BACK_TRANS_URL = "acpsdk.jfBackTransUrl";
    public static final String JF_SDK_SINGLE_QUERY_URL = "acpsdk.jfSingleQueryUrl";
    public static final String JF_SDK_CARD_TRANS_URL = "acpsdk.jfCardTransUrl";
    public static final String JF_SDK_APP_TRANS_URL = "acpsdk.jfAppTransUrl";
    public static final String SDK_SIGNCERT_PATH = "acpsdk.signCert.path";
    public static final String SDK_SIGNCERT_PWD = "acpsdk.signCert.pwd";
    public static final String SDK_SIGNCERT_TYPE = "acpsdk.signCert.type";
    public static final String SDK_ENCRYPTCERT_PATH = "acpsdk.encryptCert.path";
    public static final String SDK_ENCRYPTTRACKCERT_PATH = "acpsdk.encryptTrackCert.path";
    public static final String SDK_ENCRYPTTRACKKEY_MODULUS = "acpsdk.encryptTrackKey.modulus";
    public static final String SDK_ENCRYPTTRACKKEY_EXPONENT = "acpsdk.encryptTrackKey.exponent";
    public static final String SDK_VALIDATECERT_DIR = "acpsdk.validateCert.dir";
    public static final String SDK_CVN_ENC = "acpsdk.cvn2.enc";
    public static final String SDK_DATE_ENC = "acpsdk.date.enc";
    public static final String SDK_PAN_ENC = "acpsdk.pan.enc";
    public static final String SDK_SINGLEMODE = "acpsdk.singleMode";
    private static SDKConfig config;
    private Properties properties;

    public static SDKConfig getConfig() {
        if (config == null) {
            config = new SDKConfig();
        }

        return config;
    }

    public String getMiddleCertPath() {
        return this.middleCertPath;
    }

    public String getRootCertPath() {
        return this.rootCertPath;
    }

    public boolean isIfValidateCNName() {
        return this.ifValidateCNName;
    }

    public boolean isIfValidateRemoteCert() {
        return this.ifValidateRemoteCert;
    }

    public String getSignMethod() {
        return this.signMethod;
    }

    public void loadPropertiesFromPath(String rootPath) {
        if (StringUtils.isNotBlank(rootPath)) {
            File file = new File(rootPath + File.separator + "acp_sdk.properties");
            InputStream in = null;
            if (file.exists()) {
                try {
                    in = new FileInputStream(file);
                    BufferedReader bf = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    this.properties = new Properties();
                    this.properties.load(bf);
                    this.loadProperties(this.properties);
                } catch (FileNotFoundException var15) {
                    var15.printStackTrace();
                } catch (IOException var16) {
                    var16.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException var14) {
                            var14.printStackTrace();
                        }
                    }

                }
            } else {
                System.out.println(rootPath + "acp_sdk.properties" + "不存在,加载参数失败");
            }
        } else {
            this.loadPropertiesFromSrc();
        }

    }

    public void loadPropertiesFromSrc() {
        InputStream in = null;

        try {
            LogUtil.writeLog("位置SDKConfig-loadPropertiesFromSrc: 从classpath: " + SDKConfig.class.getClassLoader().getResource("").getPath() + " 获取属性文件" + "acp_sdk.properties");
            in = SDKConfig.class.getClassLoader().getResourceAsStream("acp_sdk.properties");
            if (in != null) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(in, "utf-8"));
                this.properties = new Properties();

                try {
                    this.properties.load(bf);
                } catch (IOException var14) {
                    throw var14;
                }

                this.loadProperties(this.properties);
                return;
            }

            LogUtil.writeErrorLog("acp_sdk.properties属性文件未能在classpath指定的目录下 " + SDKConfig.class.getClassLoader().getResource("").getPath() + " 找到!");
        } catch (IOException var15) {
            var15.printStackTrace();
            return;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

        }

    }

    public void loadProperties(Properties pro) {
        LogUtil.writeLog("位置SDKConfig-loadProperties: 开始从属性文件中加载配置项");
        String value = null;
        value = pro.getProperty("acpsdk.singleMode");
        LogUtil.writeLog("位置SDKConfig-loadProperties: 证书模式：true-单证书模式，false-多证书模式：" + value);
        if (!SDKUtil.isEmpty(value) && !"true".equals(value)) {
            this.singleMode = "false";
            LogUtil.writeLog("位置SDKConfig-loadProperties: 多证书模式，不需要加载配置文件中配置的私钥签名证书，SingleMode:[" + this.singleMode + "]");
        } else {
            this.singleMode = "true";
            LogUtil.writeLog("位置SDKConfig-loadProperties: 单证书模式，使用配置文件配置的私钥签名证书，SingleCertMode:[" + this.singleMode + "]");
            value = pro.getProperty("acpsdk.signCert.path");
            if (!SDKUtil.isEmpty(value)) {
                this.signCertPath = value.trim();
                LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：私钥签名证书路径==>acpsdk.signCert.path==>" + value + " 已加载");
            }

            value = pro.getProperty("acpsdk.signCert.pwd");
            if (!SDKUtil.isEmpty(value)) {
                this.signCertPwd = value.trim();
                LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：私钥签名证书密码==>acpsdk.signCert.pwd 已加载");
            }

            value = pro.getProperty("acpsdk.signCert.type");
            if (!SDKUtil.isEmpty(value)) {
                this.signCertType = value.trim();
                LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：私钥签名证书类型==>acpsdk.signCert.type==>" + value + " 已加载");
            }
        }

        value = pro.getProperty("acpsdk.encryptCert.path");
        if (!SDKUtil.isEmpty(value)) {
            this.encryptCertPath = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：敏感信息加密证书==>acpsdk.encryptCert.path==>" + value + " 已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：无敏感信息加密证书【acpsdk.encryptCert.path】");
        }

        value = pro.getProperty("acpsdk.validateCert.dir");
        if (!SDKUtil.isEmpty(value)) {
            this.validateCertDir = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：验证签名证书路径(这里配置的是目录，不要指定到公钥文件)==>acpsdk.validateCert.dir==>" + value + " 已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：无验证签名证书路径【acpsdk.validateCert.dir】(这里配置的是目录，不要指定到公钥文件)");
        }

        value = pro.getProperty("acpsdk.rootCert.path");
        if (!SDKUtil.isEmpty(value)) {
            this.rootCertPath = value.trim();
        }

        value = pro.getProperty("acpsdk.middleCert.path");
        if (!SDKUtil.isEmpty(value)) {
            this.middleCertPath = value.trim();
        }

        value = pro.getProperty("acpsdk.frontTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.frontRequestUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：前台URL【acpsdk.frontTransUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：前台URL【acpsdk.frontTransUrl】未加载");
        }

        value = pro.getProperty("acpsdk.backTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.backRequestUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：后台URL【acpsdk.backTransUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：后台URL【acpsdk.backTransUrl】未加载");
        }

        value = pro.getProperty("acpsdk.batchQueryUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.batchQueryUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：批量交易查询URL【acpsdk.batchQueryUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：批量交易查询URL【acpsdk.batchQueryUrl】未加载");
        }

        value = pro.getProperty("acpsdk.batchTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.batchTransUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：批量交易URL【acpsdk.batchTransUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：批量交易URL【acpsdk.batchTransUrl】未加载");
        }

        value = pro.getProperty("acpsdk.fileTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.fileTransUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：文件类交易URL【acpsdk.fileTransUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：文件类交易URL【acpsdk.fileTransUrl】未加载");
        }

        value = pro.getProperty("acpsdk.singleQueryUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.singleQueryUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：单笔交易查询URL【acpsdk.singleQueryUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：单笔交易查询URL【acpsdk.singleQueryUrl】未加载");
        }

        value = pro.getProperty("acpsdk.cardTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.cardRequestUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：有卡交易URL【acpsdk.cardTransUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：有卡交易URL【acpsdk.cardTransUrl】未加载");
        }

        value = pro.getProperty("acpsdk.appTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.appRequestUrl = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：app交易URL【acpsdk.appTransUrl】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：app交易URL【acpsdk.appTransUrl】未加载");
        }

        value = pro.getProperty("acpsdk.encryptTrackCert.path");
        if (!SDKUtil.isEmpty(value)) {
            this.encryptTrackCertPath = value.trim();
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：磁道加密证书路径【acpsdk.encryptTrackCert.path】==>" + value.trim() + "已加载");
        } else {
            LogUtil.writeLog("位置SDKConfig-loadProperties: 配置项：磁道加密证书路径【acpsdk.encryptTrackCert.path】未加载");
        }

        value = pro.getProperty("acpsdk.jfFrontTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.jfFrontRequestUrl = value.trim();
        }

        value = pro.getProperty("acpsdk.jfBackTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.jfBackRequestUrl = value.trim();
        }

        value = pro.getProperty("acpsdk.jfSingleQueryUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.jfSingleQueryUrl = value.trim();
        }

        value = pro.getProperty("acpsdk.jfCardTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.jfCardRequestUrl = value.trim();
        }

        value = pro.getProperty("acpsdk.jfAppTransUrl");
        if (!SDKUtil.isEmpty(value)) {
            this.jfAppRequestUrl = value.trim();
        }

        value = pro.getProperty("acpsdk.encryptTrackKey.exponent");
        if (!SDKUtil.isEmpty(value)) {
            this.encryptTrackKeyExponent = value.trim();
        }

        value = pro.getProperty("acpsdk.encryptTrackKey.modulus");
        if (!SDKUtil.isEmpty(value)) {
            this.encryptTrackKeyModulus = value.trim();
        }

    }

    public String getFrontRequestUrl() {
        return this.frontRequestUrl;
    }

    public void setFrontRequestUrl(String frontRequestUrl) {
        this.frontRequestUrl = frontRequestUrl;
    }

    public String getBackRequestUrl() {
        return this.backRequestUrl;
    }

    public void setBackRequestUrl(String backRequestUrl) {
        this.backRequestUrl = backRequestUrl;
    }

    public String getSignCertPath() {
        return this.signCertPath;
    }

    public void setSignCertPath(String signCertPath) {
        this.signCertPath = signCertPath;
    }

    public String getSignCertPwd() {
        return this.signCertPwd;
    }

    public void setSignCertPwd(String signCertPwd) {
        this.signCertPwd = signCertPwd;
    }

    public String getSignCertType() {
        return this.signCertType;
    }

    public void setSignCertType(String signCertType) {
        this.signCertType = signCertType;
    }

    public String getEncryptCertPath() {
        return this.encryptCertPath;
    }

    public void setEncryptCertPath(String encryptCertPath) {
        this.encryptCertPath = encryptCertPath;
    }

    public String getValidateCertDir() {
        return this.validateCertDir;
    }

    public void setValidateCertDir(String validateCertDir) {
        this.validateCertDir = validateCertDir;
    }

    public String getSingleQueryUrl() {
        return this.singleQueryUrl;
    }

    public void setSingleQueryUrl(String singleQueryUrl) {
        this.singleQueryUrl = singleQueryUrl;
    }

    public String getBatchQueryUrl() {
        return this.batchQueryUrl;
    }

    public void setBatchQueryUrl(String batchQueryUrl) {
        this.batchQueryUrl = batchQueryUrl;
    }

    public String getBatchTransUrl() {
        return this.batchTransUrl;
    }

    public void setBatchTransUrl(String batchTransUrl) {
        this.batchTransUrl = batchTransUrl;
    }

    public String getFileTransUrl() {
        return this.fileTransUrl;
    }

    public void setFileTransUrl(String fileTransUrl) {
        this.fileTransUrl = fileTransUrl;
    }

    public String getSignCertDir() {
        return this.signCertDir;
    }

    public void setSignCertDir(String signCertDir) {
        this.signCertDir = signCertDir;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getCardRequestUrl() {
        return this.cardRequestUrl;
    }

    public void setCardRequestUrl(String cardRequestUrl) {
        this.cardRequestUrl = cardRequestUrl;
    }

    public String getAppRequestUrl() {
        return this.appRequestUrl;
    }

    public void setAppRequestUrl(String appRequestUrl) {
        this.appRequestUrl = appRequestUrl;
    }

    public String getEncryptTrackCertPath() {
        return this.encryptTrackCertPath;
    }

    public void setEncryptTrackCertPath(String encryptTrackCertPath) {
        this.encryptTrackCertPath = encryptTrackCertPath;
    }

    public String getJfFrontRequestUrl() {
        return this.jfFrontRequestUrl;
    }

    public void setJfFrontRequestUrl(String jfFrontRequestUrl) {
        this.jfFrontRequestUrl = jfFrontRequestUrl;
    }

    public String getJfBackRequestUrl() {
        return this.jfBackRequestUrl;
    }

    public void setJfBackRequestUrl(String jfBackRequestUrl) {
        this.jfBackRequestUrl = jfBackRequestUrl;
    }

    public String getJfSingleQueryUrl() {
        return this.jfSingleQueryUrl;
    }

    public void setJfSingleQueryUrl(String jfSingleQueryUrl) {
        this.jfSingleQueryUrl = jfSingleQueryUrl;
    }

    public String getJfCardRequestUrl() {
        return this.jfCardRequestUrl;
    }

    public void setJfCardRequestUrl(String jfCardRequestUrl) {
        this.jfCardRequestUrl = jfCardRequestUrl;
    }

    public String getJfAppRequestUrl() {
        return this.jfAppRequestUrl;
    }

    public void setJfAppRequestUrl(String jfAppRequestUrl) {
        this.jfAppRequestUrl = jfAppRequestUrl;
    }

    public String getSingleMode() {
        return this.singleMode;
    }

    public void setSingleMode(String singleMode) {
        this.singleMode = singleMode;
    }

    public SDKConfig() {
    }

    public String getEncryptTrackKeyExponent() {
        return this.encryptTrackKeyExponent;
    }

    public void setEncryptTrackKeyExponent(String encryptTrackKeyExponent) {
        this.encryptTrackKeyExponent = encryptTrackKeyExponent;
    }

    public String getEncryptTrackKeyModulus() {
        return this.encryptTrackKeyModulus;
    }

    public void setEncryptTrackKeyModulus(String encryptTrackKeyModulus) {
        this.encryptTrackKeyModulus = encryptTrackKeyModulus;
    }
}
