package hwkang.study.auth.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class Aes256Util {
    private static volatile Aes256Util INSTANCE;

    final static String secretKey = "oingisprettyintheworld1234567890"; // 32byte
    static String IV = "asasasasasasasas"; // 16byte

    public static Aes256Util getInstance() {
        if (INSTANCE == null) {
            synchronized (Aes256Util.class) {
                if (INSTANCE == null)
                    INSTANCE = new Aes256Util();
            }
        }
        return INSTANCE;
    }

    private Aes256Util() {
        IV = secretKey.substring(0, 16);
    }

    // 암호화
    public static String encode(String str) {
        try {
            byte[] keyData = secretKey.getBytes();

            SecretKey secureKey = new SecretKeySpec(keyData, "AES");

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));

            byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
            //String enStr = new String(Base64.getUrlEncoder().encode(encrypted));
            String enStr = Base64.getUrlEncoder().encodeToString(encrypted);

            return enStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // 복호화
    public static String decode(String str) {
        try {
            byte[] keyData = secretKey.getBytes();
            SecretKey secureKey = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

            byte[] byteStr = Base64.getUrlDecoder().decode(str.getBytes());

            return new String(c.doFinal(byteStr), "UTF-8");
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
