package com.example.demo;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import com.itrus.util.sign.RSAWithSoftware;

import gzh.http.HttpClientUtils;
import gzh.util.DateUtil;
import gzh.util.StringUtil;

@RestController
@EnableAutoConfiguration
public class Example {
	private static final Logger logger = LogManager.getLogger("Example");
	
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	
	@RequestMapping("/index")
	String index() {
		return "/index.html";
	}
	
	/**
	 * 
	 * <pre>
	 * 支付
	 * &#64;return
	 * &#64;author gzh
	 * </pre>
	 */
	@RequestMapping("/doPay")
	static String doPay() {
		// 输入数据

		// 支付
		// 拼接参数
		String service_type = "zhb_pay"; //业务类型
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

		String merchant_code = "4000059955"; //商家号
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
				+ "TKolWg993ZjUQxX85FDwU8GPCHWoP5QNAy6dUQIpu9qyxvlBDFLuCq4CiF3/GVq8" + "GVVuSUGg2h4ytw=="; //商家私钥
		String url = "https://api.dindinsz.com/gateway/api/pubpay"; //通道请求地址

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
			logger.info("signStr:" + signStr);
			signValue = RSAWithSoftware.signByPrivateKey(signStr, merchant_private_key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 调通道
		map.put("sign_type", "RSA-S");
		map.put("sign", signValue);
		logger.info("map:" + map);
		String result = HttpClientUtils.getInstance().sendPostRequest(url, map, "UTF-8");
		logger.info("result:" + result);
		
		//解析返回数据
		Map<String,String> map1 = StringUtil.xmlStrToMap(result);
		String json = map1.get("prepay_id");

		// 输出数据
		String status = "0";
		String invokeCurrentPage="t";
		StringBuilder sb11 = new StringBuilder();
		sb11.append("status").append(status). //状态
		append("invokeCurrentPage").append(invokeCurrentPage). //是否在当前页面调用
		append("json").append(json); //调app支付密码框需要的参数
		
		return sb11.toString();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Example.class, args);
	}
	
	

}
