package com.example.demo;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itrus.util.sign.RSAWithSoftware;

import gzh.http.HttpClientUtils;
import gzh.util.DateUtil;
import gzh.util.PropertiesUtil;
import gzh.util.StringUtil;

/**
 * 支付
 * 
 * @author gzh-t184
 *
 */
@RestController
@EnableAutoConfiguration
public class PayController2 {
	static Logger logger = LogManager.getLogger("PayController2");

	// 读取配置文件
	static String merchant_code = PropertiesUtil.get("merchant_code", "pay.properties");
	static String merchant_private_key = PropertiesUtil.get("merchant_private_key", "pay.properties");
	static String url = PropertiesUtil.get("url", "pay.properties");

	public static void main(String[] args) {
		// doPay();

		// query();

		// testParse();
		
		//jdk properties
//		logger.info("merchant_code:" + merchant_code);
//		logger.info("merchant_private_key:" + merchant_private_key);
//		logger.info("url:" + url);
		
		//apache common properties
		Configuration config = null;
		try {
			config = new PropertiesConfiguration("pay.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String merchant_code = config.getString("merchant_code");
		logger.info("merchant_code:" + merchant_code);
	}

	/**
	 * 
	 * <pre>
	 * 测试-xml格式字符串转map
	 * &#64;author gzh
	 * </pre>
	 */
	public static void testParse() {
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><dinpay><response><retCode>SUCCESS</retCode><retMsg></retMsg><is_success>T</is_success><merchant_code>4000059955</merchant_code><order_amount>0.01</order_amount><order_no>1531190748971</order_no><order_time>2018-07-10 10:45:48</order_time><prepay_id>ddbillmerpay/9f8c8cd6d3d6d1a2</prepay_id><sign>URyVD4OhzX/mrmkYhiLRRpn8JMVjRcIN7VO/DdDNmNWK5ajjoH7qGYPKJJe5pfM/ispXWf3KIR0JOt+iTzC9u5G5cGDa2S72ItXdKX77St2aM9qXdN1wiyyh5kvKPSJQoRVGRJV8svnPt7giP8DIbr3prL9CK9syyA9Fa8Qz/sE=</sign><sign_type>RSA-S</sign_type><trade_time>2018-07-10 10:45:49</trade_time></response></dinpay>";
		Map<String, String> resMap = null;
		try {
			// resMap = StringUtil.xmlStrToMap(result, false, null);
			resMap = StringUtil.xmlStrToMap(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(resMap);
	}

	/**
	 * 
	 * <pre>
	 * 查询
	 * &#64;return
	 * &#64;author gzh
	 * </pre>
	 */
	static String query() {
		// 输入数据

		// 支付
		// 拼接参数
		String service_type = "single_trade_query";
		String merchant_code = "4000059955";
		String input_charset = "UTF-8";
		String notify_url = "www.baidu.com";
		String return_url = "www.baidu.com";
		String interface_version = "V3.0";

		String merchant_name = "智付物联";
		String merchant_order_id = "1531190748971";
		String order_time = DateUtil.getDateStr(new Date());
		String order_amount = "0.01";
		String product_name = "停车费用";
		String pub_code = "11111111";

		String merchant_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOqAUbYudrW0sfiG"
				+ "gYUmsbQqfOP7x7lt7RwvZrwKF5vaUbiCwJBmgBiH8NX5J1c+mVQozYotaWCBO7R+"
				+ "45slPrekltbqae3mwi34GpoaM7LUU95TClgAt7qPpuQ6erooZVxRWjI2SuUZRUIx"
				+ "UyUCrah5YZThTOPXfhE/4pTyEF35AgMBAAECgYBh2R0/WW/rLfS88NMGjjjEJp5q"
				+ "OtsBwp6Xjifd+pATViuXQ+e52StGESMrBYWm39X2yffJ2l0ICaSyEehDCm16Qtc/"
				+ "dY5IiQdEmCkgmWHpuY5PmHgOD9Xne0AYA11iiamnfD7dbr27zy2ItjBW3l6qvxFK"
				+ "Hu4Gpk/5gNmbtOuSiQJBAPZjjLARzZuTXjyVqpjoZpM51Uv8iqJCzjiyChe3nwcb"
				+ "7jNmPgSYrAIrpgJ2qklRkVYDlASlUbh8flHvIZQAusMCQQDzpg8vyW/kSG2XeuSV"
				+ "souGPtpJnhh8nq6UjyfN0kXFctWxmXfu3N5xidZDtXgrzJr4MQnKb4lknM7t2NJA"
				+ "NGCTAkBpGQ+i7wUoLpVM/H53mPJgLJQqRIASNLLohjE96qpgCu7xZ9Ree40ro9i9"
				+ "Rkbe3XdEHGSgErCoJBpx8rH9As6nAkEAiytdHVSYHvLn9lBx5LfZTlL0aHxvTC9v"
				+ "VNf4Sm5DACc5vHoGsV9jh8LNqlsrSwlRs1Z/WywedGPFJsJkRdwlFQJAIPOUm4qT"
				+ "TKolWg993ZjUQxX85FDwU8GPCHWoP5QNAy6dUQIpu9qyxvlBDFLuCq4CiF3/GVq8" + "GVVuSUGg2h4ytw==";

		String url = "https://query.dindinsz.com/query";

		// 计算签名
		// 拼接签名字符串
		Map<String, Object> map = new TreeMap<String, Object>(); // 拼接
		map.put("merchant_code", merchant_code);
		// map.put("notify_url", notify_url);
		map.put("interface_version", interface_version);
		// map.put("input_charset", input_charset);
		map.put("order_no", merchant_order_id);
		// map.put("order_time", order_time);
		// map.put("order_amount", order_amount);
		map.put("service_type", service_type);
		// map.put("pub_code", pub_code);
		// map.put("client_ip", "192.168.1.1");
		// map.put("product_name", product_name);

		Set<String> keys = map.keySet(); // 遍历映射
		StringBuilder sb1 = new StringBuilder();
		for (String key1 : keys) {
			sb1.append(key1).append("=").append(map.get(key1)).append("&");
		}
		// String signStr =
		// sb1.append("key").append("=").append(merchant_private_key).toString();
		String signStr = sb1.substring(0, sb1.length() - 1);

		// 计算签名
		String signValue = null;
		try {
			System.out.println(signStr);
			signValue = RSAWithSoftware.signByPrivateKey(signStr, merchant_private_key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 调通道
		map.put("sign_type", "RSA-S");
		map.put("sign", signValue);
		System.out.println(map);
		String result = HttpClientUtils.getInstance().sendPostRequest(url, map, "UTF-8");
		System.out.println(result);

		// 输出数据
		String orderJson = null;
		return orderJson;
	}

	/**
	 * 
	 * <pre>
	 * 支付
	 * &#64;return
	 * &#64;author gzh
	 * </pre>
	 */
	@RequestMapping("/doPay2")
	static String doPay2() {
		// 输入数据

		// 支付
		// 拼接参数
		String service_type = "zhb_pay";
		String merchant_code = "4000059955";
		String input_charset = "UTF-8";
		String notify_url = "www.baidu.com";
		String return_url = "www.baidu.com";
		String interface_version = "V3.1";

		String merchant_name = "智付物联";
		String merchant_order_id = Long.toString(System.currentTimeMillis());
		String order_time = DateUtil.getDateStr(new Date());
		String order_amount = "0.01";
		String product_name = "停车费用";
		String pub_code = "11111111";

		String merchant_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOqAUbYudrW0sfiG"
				+ "gYUmsbQqfOP7x7lt7RwvZrwKF5vaUbiCwJBmgBiH8NX5J1c+mVQozYotaWCBO7R+"
				+ "45slPrekltbqae3mwi34GpoaM7LUU95TClgAt7qPpuQ6erooZVxRWjI2SuUZRUIx"
				+ "UyUCrah5YZThTOPXfhE/4pTyEF35AgMBAAECgYBh2R0/WW/rLfS88NMGjjjEJp5q"
				+ "OtsBwp6Xjifd+pATViuXQ+e52StGESMrBYWm39X2yffJ2l0ICaSyEehDCm16Qtc/"
				+ "dY5IiQdEmCkgmWHpuY5PmHgOD9Xne0AYA11iiamnfD7dbr27zy2ItjBW3l6qvxFK"
				+ "Hu4Gpk/5gNmbtOuSiQJBAPZjjLARzZuTXjyVqpjoZpM51Uv8iqJCzjiyChe3nwcb"
				+ "7jNmPgSYrAIrpgJ2qklRkVYDlASlUbh8flHvIZQAusMCQQDzpg8vyW/kSG2XeuSV"
				+ "souGPtpJnhh8nq6UjyfN0kXFctWxmXfu3N5xidZDtXgrzJr4MQnKb4lknM7t2NJA"
				+ "NGCTAkBpGQ+i7wUoLpVM/H53mPJgLJQqRIASNLLohjE96qpgCu7xZ9Ree40ro9i9"
				+ "Rkbe3XdEHGSgErCoJBpx8rH9As6nAkEAiytdHVSYHvLn9lBx5LfZTlL0aHxvTC9v"
				+ "VNf4Sm5DACc5vHoGsV9jh8LNqlsrSwlRs1Z/WywedGPFJsJkRdwlFQJAIPOUm4qT"
				+ "TKolWg993ZjUQxX85FDwU8GPCHWoP5QNAy6dUQIpu9qyxvlBDFLuCq4CiF3/GVq8" + "GVVuSUGg2h4ytw==";

		String url = "https://api.dindinsz.com/gateway/api/pubpay";

		// 计算签名
		// 拼接签名字符串
		StringBuilder sb = new StringBuilder();
		sb.append("service_type").append(service_type).append("merchant_code").append(merchant_code)
				.append("input_charset").append(input_charset).append("notify_url").append(notify_url)
				.append("return_url").append(return_url).append("interface_version").append(interface_version)
				.append("merchant_name").append(merchant_name).append("merchant_order_id").append(merchant_order_id)
				.append("order_time").append(order_time).append("order_amount").append(order_amount)
				.append("product_name").append(product_name).append("key").append(merchant_private_key);
		// 排序
		Map<String, Object> map = new TreeMap<String, Object>(); // 拼接
		map.put("merchant_code", merchant_code);
		map.put("notify_url", notify_url);
		map.put("interface_version", interface_version);
		map.put("input_charset", input_charset);
		map.put("order_no", merchant_order_id);
		map.put("order_time", order_time);
		map.put("order_amount", order_amount);
		map.put("service_type", service_type);
		map.put("pub_code", pub_code);
		map.put("client_ip", "192.168.1.1");
		map.put("product_name", product_name);

		Set<String> keys = map.keySet(); // 遍历映射
		StringBuilder sb1 = new StringBuilder();
		for (String key1 : keys) {
			sb1.append(key1).append("=").append(map.get(key1)).append("&");
		}
		// String signStr =
		// sb1.append("key").append("=").append(merchant_private_key).toString();
		String signStr = sb1.substring(0, sb1.length() - 1);

		// 计算签名
		String signValue = null;
		try {
			System.out.println(signStr);
			signValue = RSAWithSoftware.signByPrivateKey(signStr, merchant_private_key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 调通道
		map.put("sign_type", "RSA-S");
		map.put("sign", signValue);
		System.out.println(map);
		String result = HttpClientUtils.getInstance().sendPostRequest(url, map, "UTF-8");
		System.out.println(result);

		// 输出数据
		String orderJson = null;
		return orderJson;
	}
}
