package com.example.demo;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//
//import com.itrus.util.sign.RSAWithSoftware;

import decrypt.RSAWithSoftware;
//import decrypt.RSAWithSoftware;
import gzh.http.HttpClientUtils;
import gzh.util.DateUtil;
import gzh.util.StringUtil;

@RestController
@PropertySource(value = "classpath:pay.properties") 
public class Example{
	private static final Logger logger = LogManager.getLogger("Example");
	
//	@Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(Example.class);
//    }
	
	@Value("${merchant_code}")
	private String merchantCode;
	
	@Value("${merchant_private_key}")
	private String merchantPrivateKey;
	
	@Value("${url}")
	private String url;

	// 读取配置文件
		
		
//		static String merchant_code = PropertiesUtil.get("merchant_code", "com/example/demo/pay.properties");
//		static String merchant_private_key = PropertiesUtil.get("merchant_private_key", "com/example/demo/pay.properties");
//		static String url = PropertiesUtil.get("url", "com/example/demo/pay.properties");
	/**
	 * 
	 * <pre>
	 * 支付
	 * &#64;return
	 * &#64;author gzh
	 * </pre>
	 */
	@RequestMapping("/doPay")
	public String doPay(@RequestParam(name="code", required=false, defaultValue="123456") String code) {
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
		String product_name = "智慧停车";
		String pub_code = code; //app授权code

//		String merchant_code = "4000059955"; //商家号
//		String merchant_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOqAUbYudrW0sfiG"
//				+ "gYUmsbQqfOP7x7lt7RwvZrwKF5vaUbiCwJBmgBiH8NX5J1c+mVQozYotaWCBO7R+"
//				+ "45slPrekltbqae3mwi34GpoaM7LUU95TClgAt7qPpuQ6erooZVxRWjI2SuUZRUIx"
//				+ "UyUCrah5YZThTOPXfhE/4pTyEF35AgMBAAECgYBh2R0/WW/rLfS88NMGjjjEJp5q"
//				+ "OtsBwp6Xjifd+pATViuXQ+e52StGESMrBYWm39X2yffJ2l0ICaSyEehDCm16Qtc/"
//				+ "dY5IiQdEmCkgmWHpuY5PmHgOD9Xne0AYA11iiamnfD7dbr27zy2ItjBW3l6qvxFK"
//				+ "Hu4Gpk/5gNmbtOuSiQJBAPZjjLARzZuTXjyVqpjoZpM51Uv8iqJCzjiyChe3nwcb"
//				+ "7jNmPgSYrAIrpgJ2qklRkVYDlASlUbh8flHvIZQAusMCQQDzpg8vyW/kSG2XeuSV"
//				+ "souGPtpJnhh8nq6UjyfN0kXFctWxmXfu3N5xidZDtXgrzJr4MQnKb4lknM7t2NJA"
//				+ "NGCTAkBpGQ+i7wUoLpVM/H53mPJgLJQqRIASNLLohjE96qpgCu7xZ9Ree40ro9i9"
//				+ "Rkbe3XdEHGSgErCoJBpx8rH9As6nAkEAiytdHVSYHvLn9lBx5LfZTlL0aHxvTC9v"
//				+ "VNf4Sm5DACc5vHoGsV9jh8LNqlsrSwlRs1Z/WywedGPFJsJkRdwlFQJAIPOUm4qT"
//				+ "TKolWg993ZjUQxX85FDwU8GPCHWoP5QNAy6dUQIpu9qyxvlBDFLuCq4CiF3/GVq8" + "GVVuSUGg2h4ytw=="; //商家私钥
//		String url = "https://api.dindinsz.com/gateway/api/pubpay"; //通道请求地址

		// 计算签名
		// 拼接签名字符串
		StringBuilder sb = new StringBuilder();
		sb.append("service_type").append(service_type).append("merchant_code").append(merchantCode)
				.append("input_charset").append(input_charset).append("notify_url").append(notify_url)
				.append("return_url").append(return_url).append("interface_version").append(interface_version)
				.append("merchant_name").append(merchant_name).append("merchant_order_id").append(merchant_order_id)
				.append("order_time").append(order_time).append("order_amount").append(order_amount)
				.append("product_name").append(product_name).append("key").append(merchantPrivateKey);
		// 排序
		Map<String, Object> map = new TreeMap<String, Object>(); // 拼接
		map.put("merchant_code", merchantCode);
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
			System.out.println("signStr:" + signStr);
			signValue = RSAWithSoftware.signByPrivateKey(signStr, merchantPrivateKey);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sign exception:",e);
			System.out.println("sign exception:" + e);
		}

		// 调通道
		map.put("sign_type", "RSA-S");
		map.put("sign", signValue);
		logger.info("map:" + map);
		System.out.println("map:" + map);
		
		logger.info("url:" + url);
		System.out.println("url:" + url);
		String result = HttpClientUtils.getInstance().sendPostRequest(url, map, "UTF-8");
		logger.info("result:" + result);
		System.out.println("result:" + result);
		
		//解析返回数据
		Map<String,String> map1 = StringUtil.xmlStrToMap(result);
		String json = map1.get("prepay_id");
		String merchant_order_id2 = map1.get("order_no");

		// 输出数据
		String status = "0";
		String invokeCurrentPage="t";
		StringBuilder sb11 = new StringBuilder();
		sb11.append("{\"status\":").append("\"").append(status).append("\","). //状态
		append("\"invokeCurrentPage\":").append("\"").append(invokeCurrentPage).append("\","). //是否在当前页面调用
		append("\"merchant_order_id\":").append("\"").append(merchant_order_id2).append("\","). //商家订单号
		append("\"json\":").append("\"").append(json).append("\"}"); //调app支付密码框需要的参数
		logger.info("sb11:" + sb11);
		System.out.println("sb11:" + sb11);
		
		return sb11.toString();
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
	public String doPay2(@RequestParam(name="code", required=false, defaultValue="123456") String code) {
		// 输入数据

		// 支付
		// 拼接参数
		String service_type = "wxpub_pay"; //业务类型
		String input_charset = "UTF-8";
		String notify_url = "www.baidu.com";
		String return_url = "www.baidu.com";
		String interface_version = "V3.1";

		String merchant_name = "智付物联";
		String merchant_order_id = Long.toString(System.currentTimeMillis());
		String order_time = DateUtil.getDateStr(new Date());
		String order_amount = "0.01";
		String product_name = "智慧停车";
		String pub_code = code; //app授权code

//		String merchant_code = "4000059955"; //商家号
//		String merchant_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOqAUbYudrW0sfiG"
//				+ "gYUmsbQqfOP7x7lt7RwvZrwKF5vaUbiCwJBmgBiH8NX5J1c+mVQozYotaWCBO7R+"
//				+ "45slPrekltbqae3mwi34GpoaM7LUU95TClgAt7qPpuQ6erooZVxRWjI2SuUZRUIx"
//				+ "UyUCrah5YZThTOPXfhE/4pTyEF35AgMBAAECgYBh2R0/WW/rLfS88NMGjjjEJp5q"
//				+ "OtsBwp6Xjifd+pATViuXQ+e52StGESMrBYWm39X2yffJ2l0ICaSyEehDCm16Qtc/"
//				+ "dY5IiQdEmCkgmWHpuY5PmHgOD9Xne0AYA11iiamnfD7dbr27zy2ItjBW3l6qvxFK"
//				+ "Hu4Gpk/5gNmbtOuSiQJBAPZjjLARzZuTXjyVqpjoZpM51Uv8iqJCzjiyChe3nwcb"
//				+ "7jNmPgSYrAIrpgJ2qklRkVYDlASlUbh8flHvIZQAusMCQQDzpg8vyW/kSG2XeuSV"
//				+ "souGPtpJnhh8nq6UjyfN0kXFctWxmXfu3N5xidZDtXgrzJr4MQnKb4lknM7t2NJA"
//				+ "NGCTAkBpGQ+i7wUoLpVM/H53mPJgLJQqRIASNLLohjE96qpgCu7xZ9Ree40ro9i9"
//				+ "Rkbe3XdEHGSgErCoJBpx8rH9As6nAkEAiytdHVSYHvLn9lBx5LfZTlL0aHxvTC9v"
//				+ "VNf4Sm5DACc5vHoGsV9jh8LNqlsrSwlRs1Z/WywedGPFJsJkRdwlFQJAIPOUm4qT"
//				+ "TKolWg993ZjUQxX85FDwU8GPCHWoP5QNAy6dUQIpu9qyxvlBDFLuCq4CiF3/GVq8" + "GVVuSUGg2h4ytw=="; //商家私钥
//		String url = "https://api.dindinsz.com/gateway/api/pubpay"; //通道请求地址

		// 计算签名
		// 拼接签名字符串
		StringBuilder sb = new StringBuilder();
		sb.append("service_type").append(service_type).append("merchant_code").append(merchantCode)
				.append("input_charset").append(input_charset).append("notify_url").append(notify_url)
				.append("return_url").append(return_url).append("interface_version").append(interface_version)
				.append("merchant_name").append(merchant_name).append("merchant_order_id").append(merchant_order_id)
				.append("order_time").append(order_time).append("order_amount").append(order_amount)
				.append("product_name").append(product_name).append("key").append(merchantPrivateKey);
		// 排序
		Map<String, Object> map = new TreeMap<String, Object>(); // 拼接
		map.put("merchant_code", merchantCode);
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
			System.out.println("signStr:" + signStr);
			signValue = RSAWithSoftware.signByPrivateKey(signStr, merchantPrivateKey);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sign exception:",e);
			System.out.println("sign exception:" + e);
		}

		// 调通道
		map.put("sign_type", "RSA-S");
		map.put("sign", signValue);
		logger.info("map:" + map);
		System.out.println("map:" + map);
		
		logger.info("url:" + url);
		System.out.println("url:" + url);
		String result = HttpClientUtils.getInstance().sendPostRequest(url, map, "UTF-8");
		logger.info("result:" + result);
		System.out.println("result:" + result);
		
		//解析返回数据
		Map<String,String> map1 = StringUtil.xmlStrToMap(result);
		String json = map1.get("prepay_id");
		String merchant_order_id2 = map1.get("order_no");

		// 输出数据
		String status = "0";
		String invokeCurrentPage="t";
		StringBuilder sb11 = new StringBuilder();
		sb11.append("{\"status\":").append("\"").append(status).append("\","). //状态
		append("\"invokeCurrentPage\":").append("\"").append(invokeCurrentPage).append("\","). //是否在当前页面调用
		append("\"merchant_order_id\":").append("\"").append(merchant_order_id2).append("\","). //商家订单号
		append("\"json\":").append("\"").append(json).append("\"}"); //调app支付密码框需要的参数
		logger.info("sb11:" + sb11);
		System.out.println("sb11:" + sb11);
		
		return sb11.toString();
	}
	
	
	@RequestMapping("/getWXCode1")
	  void getWXCode1(HttpServletResponse response) throws IOException {
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8a3e35da512ccd93&redirect_uri=https://www.zfiot.net/getWXCode2&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	    response.sendRedirect(url);
	  }
	

}
