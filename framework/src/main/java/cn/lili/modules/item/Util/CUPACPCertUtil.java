package cn.lili.modules.item.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import cn.hutool.crypto.SecureUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CUPACPCertUtil {

    private static KeyStore keyStore = null;
    private static X509Certificate encryptCert = null;
    private static PublicKey encryptTrackKey = null;
    private static X509Certificate validateCert = null;
    private static Map<String, X509Certificate> certMap = new HashMap();
    private static final ThreadLocal<KeyStore> certKeyStoreLocal = new ThreadLocal();
    private static final Map<String, KeyStore> certKeyStoreMap = new ConcurrentHashMap();

    static {
        init();
    }

    public CUPACPCertUtil() {
    }

    public static void init() {
        LogUtil.writeLog("初始化类：CUPACPCertUtil===开始");
        if ("true".equals(SDKConfig.getConfig().getSingleMode())) {
            initSignCert();
        }

        LogUtil.writeLog("初始化加密公钥证书");
        initEncryptCert();
        LogUtil.writeLog("加载磁道公钥");
        initTrackKey();
        LogUtil.writeLog("初始化所有的验签证书");
        initValidateCertFromDir();
        LogUtil.writeLog("初始化类：CUPACPCertUtil===开始");
    }

    public static void initSignCert() {
        if (keyStore != null) {
            keyStore = null;
        }

        try {
            keyStore = getKeyInfo(SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd(), SDKConfig.getConfig().getSignCertType());
            LogUtil.writeLog("InitSignCert Successful. CertId=[" + getSignCertId() + "]");
        } catch (IOException var1) {
            LogUtil.writeErrorLog("InitSignCert Error", var1);
        }

    }

    /** @deprecated */
    public static void initSignCert(String certFilePath, String certPwd) {
        LogUtil.writeLog("加载证书文件[" + certFilePath + "]和证书密码[" + certPwd + "]的签名证书开始.");
        certKeyStoreLocal.remove();
        File files = new File(certFilePath);
        if (!files.exists()) {
            LogUtil.writeLog("证书文件不存在,初始化签名证书失败.");
        } else {
            try {
                certKeyStoreLocal.set(getKeyInfo(certFilePath, certPwd, "PKCS12"));
            } catch (IOException var4) {
                LogUtil.writeErrorLog("加载签名证书失败", var4);
            }

            LogUtil.writeLog("加载证书文件[" + certFilePath + "]和证书密码[" + certPwd + "]的签名证书结束.");
        }
    }

    public static void loadRsaCert(String certFilePath, String certPwd) {
        KeyStore keyStore = null;

        try {
            keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
            certKeyStoreMap.put(certFilePath, keyStore);
            LogUtil.writeLog("LoadRsaCert Successful");
        } catch (IOException var4) {
            LogUtil.writeErrorLog("LoadRsaCert Error", var4);
        }

    }

    private static void initEncryptCert() {
        LogUtil.writeLog("加载敏感信息加密证书==>" + SDKConfig.getConfig().getEncryptCertPath());
        if (!SDKUtil.isEmpty(SDKConfig.getConfig().getEncryptCertPath())) {
            encryptCert = initCert(SDKConfig.getConfig().getEncryptCertPath());
            LogUtil.writeLog("LoadEncryptCert Successful");
        } else {
            LogUtil.writeLog("WARN: acpsdk.encryptCert.path is empty");
        }

    }

    private static void initTrackKey() {
        if (!SDKUtil.isEmpty(SDKConfig.getConfig().getEncryptTrackKeyModulus()) && !SDKUtil.isEmpty(SDKConfig.getConfig().getEncryptTrackKeyExponent())) {
            encryptTrackKey = SecureUtil.getPublicKey(SDKConfig.getConfig().getEncryptTrackKeyModulus(), SDKConfig.getConfig().getEncryptTrackKeyExponent());
            LogUtil.writeLog("LoadEncryptTrackKey Successful");
        } else {
            LogUtil.writeLog("WARN: acpsdk.encryptTrackKey.modulus or acpsdk.encryptTrackKey.exponent is empty");
        }

    }

    private static X509Certificate initCert(String path) {
        X509Certificate encryptCertTemp = null;
        CertificateFactory cf = null;
        FileInputStream in = null;

        try {
            cf = CertificateFactory.getInstance("X.509");
            in = new FileInputStream(path);
            encryptCertTemp = (X509Certificate)cf.generateCertificate(in);
            LogUtil.writeLog("[" + path + "][CertId=" + encryptCertTemp.getSerialNumber().toString() + "]");
        } catch (CertificateException var15) {
            LogUtil.writeErrorLog("InitCert Error", var15);
        } catch (FileNotFoundException var16) {
            LogUtil.writeErrorLog("InitCert Error File Not Found", var16);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var14) {
                    LogUtil.writeErrorLog(var14.toString());
                }
            }

        }

        return encryptCertTemp;
    }

    private static void initValidateCertFromDir() {
        LogUtil.writeLog("位置：CUPACPCertUtil-initValidateCertFromDir：从指定目录下加载验证签名证书");
        certMap.clear();
        String dir = SDKConfig.getConfig().getValidateCertDir();
        LogUtil.writeLog("位置：CUPACPCertUtil-initValidateCertFromDir：加载验证签名证书目录==>" + dir);
        if (SDKUtil.isEmpty(dir)) {
            LogUtil.writeLog("位置：CUPACPCertUtil-initValidateCertFromDir：验证签名证书目录为空==> ERROR: acpsdk.validateCert.dir is empty");
        } else {
            CertificateFactory cf = null;
            FileInputStream in = null;

            try {
                cf = CertificateFactory.getInstance("X.509");
                File fileDir = new File(dir);
                File[] files = fileDir.listFiles(new CerFilter());

                for(int i = 0; i < files.length; ++i) {
                    File file = files[i];
                    in = new FileInputStream(file.getAbsolutePath());
                    validateCert = (X509Certificate)cf.generateCertificate(in);
                    certMap.put(validateCert.getSerialNumber().toString(), validateCert);
                    LogUtil.writeLog("位置：CUPACPCertUtil-initValidateCertFromDir：证书加载信息[" + file.getAbsolutePath() + "][CertId=" + validateCert.getSerialNumber().toString() + "]");
                }

                LogUtil.writeLog("位置：CUPACPCertUtil-initValidateCertFromDir：验证签名证书加载成功==> LoadVerifyCert Successful");
            } catch (CertificateException var17) {
                LogUtil.writeErrorLog("LoadVerifyCert Error", var17);
            } catch (FileNotFoundException var18) {
                LogUtil.writeErrorLog("LoadVerifyCert Error File Not Found", var18);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException var16) {
                        LogUtil.writeErrorLog(var16.toString());
                    }
                }

            }

        }
    }

    public static PrivateKey getSignCertPrivateKey() {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = (String)aliasenum.nextElement();
            }

            PrivateKey privateKey = (PrivateKey)keyStore.getKey(keyAlias, SDKConfig.getConfig().getSignCertPwd().toCharArray());
            return privateKey;
        } catch (KeyStoreException var3) {
            LogUtil.writeErrorLog("getSignCertPrivateKey Error", var3);
            return null;
        } catch (UnrecoverableKeyException var4) {
            LogUtil.writeErrorLog("getSignCertPrivateKey Error", var4);
            return null;
        } catch (NoSuchAlgorithmException var5) {
            LogUtil.writeErrorLog("getSignCertPrivateKey Error", var5);
            return null;
        }
    }

    /** @deprecated */
    public static PrivateKey getSignCertPrivateKeyByThreadLocal(String certPath, String certPwd) {
        if (certKeyStoreLocal.get() == null) {
            initSignCert(certPath, certPwd);
        }

        try {
            Enumeration<String> aliasenum = ((KeyStore)certKeyStoreLocal.get()).aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = (String)aliasenum.nextElement();
            }

            PrivateKey privateKey = (PrivateKey)((KeyStore)certKeyStoreLocal.get()).getKey(keyAlias, certPwd.toCharArray());
            return privateKey;
        } catch (Exception var5) {
            LogUtil.writeErrorLog("获取[" + certPath + "]的签名证书的私钥失败", var5);
            return null;
        }
    }

    public static PrivateKey getSignCertPrivateKeyByStoreMap(String certPath, String certPwd) {
        if (!certKeyStoreMap.containsKey(certPath)) {
            loadRsaCert(certPath, certPwd);
        }

        try {
            Enumeration<String> aliasenum = ((KeyStore)certKeyStoreMap.get(certPath)).aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = (String)aliasenum.nextElement();
            }

            PrivateKey privateKey = (PrivateKey)((KeyStore)certKeyStoreMap.get(certPath)).getKey(keyAlias, certPwd.toCharArray());
            return privateKey;
        } catch (KeyStoreException var5) {
            LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", var5);
            return null;
        } catch (UnrecoverableKeyException var6) {
            LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", var6);
            return null;
        } catch (NoSuchAlgorithmException var7) {
            LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", var7);
            return null;
        }
    }

    public static PublicKey getEncryptCertPublicKey() {
        if (encryptCert == null) {
            String path = SDKConfig.getConfig().getEncryptCertPath();
            if (!SDKUtil.isEmpty(path)) {
                encryptCert = initCert(path);
                return encryptCert.getPublicKey();
            } else {
                LogUtil.writeLog("ERROR: acpsdk.encryptCert.path is empty");
                return null;
            }
        } else {
            return encryptCert.getPublicKey();
        }
    }

    public static PublicKey getEncryptCertPublicKeyByPath(String path) {
        if (encryptCert == null) {
            if (!SDKUtil.isEmpty(path)) {
                encryptCert = initCert(path);
                return encryptCert.getPublicKey();
            } else {
                LogUtil.writeLog("ERROR: acpsdk.encryptCert.path is empty");
                return null;
            }
        } else {
            return encryptCert.getPublicKey();
        }
    }

    public static PublicKey getEncryptTrackPublicKey() {
        if (encryptTrackKey == null) {
            initTrackKey();
        }

        return encryptTrackKey;
    }

    public static PublicKey getValidateKey() {
        return validateCert == null ? null : validateCert.getPublicKey();
    }

    public static PublicKey getValidateKey(String certId) {
        X509Certificate cf = null;
        if (certMap.containsKey(certId)) {
            cf = (X509Certificate)certMap.get(certId);
            return cf.getPublicKey();
        } else {
            initValidateCertFromDir();
            if (certMap.containsKey(certId)) {
                cf = (X509Certificate)certMap.get(certId);
                return cf.getPublicKey();
            } else {
                LogUtil.writeErrorLog("缺少certId=[" + certId + "]对应的验签证书.");
                return null;
            }
        }
    }

    public static String getSignCertId() {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = (String)aliasenum.nextElement();
            }

            X509Certificate cert = (X509Certificate)keyStore.getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (Exception var3) {
            LogUtil.writeErrorLog("getSignCertId Error", var3);
            return null;
        }
    }

    public static String getEncryptCertId() {
        if (encryptCert == null) {
            String path = SDKConfig.getConfig().getEncryptCertPath();
            if (!SDKUtil.isEmpty(path)) {
                encryptCert = initCert(path);
                return encryptCert.getSerialNumber().toString();
            } else {
                LogUtil.writeLog("ERROR: acpsdk.encryptCert.path is empty");
                return null;
            }
        } else {
            return encryptCert.getSerialNumber().toString();
        }
    }

    /** @deprecated */
    public static String getEncryptTrackCertId() {
        return "";
    }

    public static PublicKey getSignPublicKey() {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = (String)aliasenum.nextElement();
            }

            Certificate cert = keyStore.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();
            return pubkey;
        } catch (Exception var4) {
            LogUtil.writeErrorLog(var4.toString());
            return null;
        }
    }

    public static KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type) throws IOException {
        LogUtil.writeLog("加载签名证书==>" + pfxkeyfile);
        FileInputStream fis = null;

        try {
            KeyStore ks = null;
            String jdkVendor;
            if ("JKS".equals(type)) {
                ks = KeyStore.getInstance(type);
            } else if ("PKCS12".equals(type)) {
                jdkVendor = System.getProperty("java.vm.vendor");
                String javaVersion = System.getProperty("java.version");
                LogUtil.writeLog("java.vm.vendor=[" + jdkVendor + "]");
                LogUtil.writeLog("java.version=[" + javaVersion + "]");
                if (jdkVendor != null && jdkVendor.startsWith("IBM")) {
                    Security.insertProviderAt(new BouncyCastleProvider(), 1);
                    printSysInfo();
                } else {
                    Security.addProvider(new BouncyCastleProvider());
                }

                ks = KeyStore.getInstance(type);
            }

            LogUtil.writeLog("Load RSA CertPath=[" + pfxkeyfile + "],Pwd=[" + keypwd + "]");
            fis = new FileInputStream(pfxkeyfile);
            jdkVendor = null;
            char[] nPassword = keypwd != null && !"".equals(keypwd.trim()) ? keypwd.toCharArray() : null;
            if (ks != null) {
                ks.load(fis, nPassword);
            }

            KeyStore var8 = ks;
            return var8;
        } catch (Exception var11) {
            if (Security.getProvider("BC") == null) {
                LogUtil.writeLog("BC Provider not installed.");
            }

            LogUtil.writeErrorLog("getKeyInfo Error", var11);
            if (var11 instanceof KeyStoreException && "PKCS12".equals(type)) {
                Security.removeProvider("BC");
            }
        } finally {
            if (fis != null) {
                fis.close();
            }

        }

        return null;
    }

    public static void printSysInfo() {
        LogUtil.writeLog("================= SYS INFO begin====================");
        LogUtil.writeLog("os_name:" + System.getProperty("os.name"));
        LogUtil.writeLog("os_arch:" + System.getProperty("os.arch"));
        LogUtil.writeLog("os_version:" + System.getProperty("os.version"));
        LogUtil.writeLog("java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));
        LogUtil.writeLog("java_vm_specification_vendor:" + System.getProperty("java.vm.specification.vendor"));
        LogUtil.writeLog("java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
        LogUtil.writeLog("java_vm_version:" + System.getProperty("java.vm.version"));
        LogUtil.writeLog("java_vm_name:" + System.getProperty("java.vm.name"));
        LogUtil.writeLog("java.version:" + System.getProperty("java.version"));
        printProviders();
        LogUtil.writeLog("================= SYS INFO end=====================");
    }

    public static void printProviders() {
        LogUtil.writeLog("Providers List:");
        Provider[] providers = Security.getProviders();

        for(int i = 0; i < providers.length; ++i) {
            LogUtil.writeLog(i + 1 + "." + providers[i].getName());
        }

    }

    /** @deprecated */
    public static String getCertIdByThreadLocal(String certPath, String certPwd) {
        initSignCert(certPath, certPwd);

        try {
            Enumeration<String> aliasenum = ((KeyStore)certKeyStoreLocal.get()).aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = (String)aliasenum.nextElement();
            }

            X509Certificate cert = (X509Certificate)((KeyStore)certKeyStoreLocal.get()).getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (Exception var5) {
            LogUtil.writeErrorLog("获取签名证书的序列号失败", var5);
            return "";
        }
    }

    public static String getCertIdByKeyStoreMap(String certPath, String certPwd) {
        if (!certKeyStoreMap.containsKey(certPath)) {
            LogUtil.writeLog("位置：CUPACPCertUtil-getCertIdByKeyStoreMap：获取签名证书序列号,缓存中未查询到,则加载RSA证书");
            loadRsaCert(certPath, certPwd);
        }

        return getCertIdIdByStore((KeyStore)certKeyStoreMap.get(certPath));
    }

    private static String getCertIdIdByStore(KeyStore keyStore) {
        Enumeration<String> aliasenum = null;

        try {
            aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = (String)aliasenum.nextElement();
            }

            X509Certificate cert = (X509Certificate)keyStore.getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (KeyStoreException var4) {
            LogUtil.writeErrorLog("getCertIdIdByStore Error", var4);
            return null;
        }
    }

    public static Map<String, X509Certificate> getCertMap() {
        return certMap;
    }

    public static void setCertMap(Map<String, X509Certificate> certMap) {
        CUPACPCertUtil.certMap = certMap;
    }

    public static PublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception var6) {
            LogUtil.writeErrorLog("构造RSA公钥失败：" + var6);
            return null;
        }
    }

    public static PublicKey getEncryptTrackCertPublicKey(String modulus, String exponent) {
        if (!SDKUtil.isEmpty(modulus) && !SDKUtil.isEmpty(exponent)) {
            return getPublicKey(modulus, exponent);
        } else {
            LogUtil.writeErrorLog("[modulus] OR [exponent] invalid");
            return null;
        }
    }

    static class CerFilter implements FilenameFilter {
        CerFilter() {
        }

        public boolean isCer(String name) {
            return name.toLowerCase().endsWith(".cer");
        }

        public boolean accept(File dir, String name) {
            return this.isCer(name);
        }
    }
}
