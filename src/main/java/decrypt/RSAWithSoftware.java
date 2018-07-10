package decrypt;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;


/**
 * RSA签名
 * @author gzh-t184
 *
 */
public class RSAWithSoftware {
	/**
	 * 计算签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String signByPrivateKey(String data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateK);
		signature.update(data.getBytes("utf-8"));
		return Base64.encode(signature.sign()).replaceAll("\n", "").replaceAll("\r\n", "").replaceAll("\r", "");
	}
}
