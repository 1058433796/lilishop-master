package cn.lili.modules.item.Util;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CliperInstance {
    private static ThreadLocal<Cipher> cipherTL = new ThreadLocal<Cipher>() {
        protected Cipher initialValue() {
            throw new Error("Unresolved compilation problem: \n\tThe method getInstance(String, String) in the type Cipher is not applicable for the arguments (String, BouncyCastleProvider)\n");
        }
    };

    public CliperInstance() {
    }

    public static Cipher getInstance() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return (Cipher)cipherTL.get();
    }
}
