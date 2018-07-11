package com.example.demo;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itrus.util.sign.RSAWithSoftware;

import gzh.http.HttpClientUtils;

/**
 * 支付
 * @author gzh-t184
 *
 */
@RestController
@EnableAutoConfiguration
public class PayController {
	@RequestMapping("/doPay")
	String doPay() {
		//输入数据
		
		//支付
		//拼接参数
		String service_type = "MerchantBusiPay";
		String merchant_code = "4000059955";
		String input_charset= "UTF-8";
		String notify_url= "www.baidu.com";
		String return_url= "www.baidu.com";
		String interface_version= "V1.0";
		
		String sign_type= "MerchantBusiPay";
		String sign= "MerchantBusiPay";
		
		String merchant_name= "智付物联";
		String merchant_order_id= new Date().toString();
		String order_time= new Date().toString();
		String order_amount= "0.01";
		String product_name= "停车费用";
		
		String merchant_private_key= 
				"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOqAUbYudrW0sfiG" + 
				"gYUmsbQqfOP7x7lt7RwvZrwKF5vaUbiCwJBmgBiH8NX5J1c+mVQozYotaWCBO7R+" + 
				"45slPrekltbqae3mwi34GpoaM7LUU95TClgAt7qPpuQ6erooZVxRWjI2SuUZRUIx" + 
				"UyUCrah5YZThTOPXfhE/4pTyEF35AgMBAAECgYBh2R0/WW/rLfS88NMGjjjEJp5q" + 
				"OtsBwp6Xjifd+pATViuXQ+e52StGESMrBYWm39X2yffJ2l0ICaSyEehDCm16Qtc/" + 
				"dY5IiQdEmCkgmWHpuY5PmHgOD9Xne0AYA11iiamnfD7dbr27zy2ItjBW3l6qvxFK" + 
				"Hu4Gpk/5gNmbtOuSiQJBAPZjjLARzZuTXjyVqpjoZpM51Uv8iqJCzjiyChe3nwcb" + 
				"7jNmPgSYrAIrpgJ2qklRkVYDlASlUbh8flHvIZQAusMCQQDzpg8vyW/kSG2XeuSV" + 
				"souGPtpJnhh8nq6UjyfN0kXFctWxmXfu3N5xidZDtXgrzJr4MQnKb4lknM7t2NJA" + 
				"NGCTAkBpGQ+i7wUoLpVM/H53mPJgLJQqRIASNLLohjE96qpgCu7xZ9Ree40ro9i9" + 
				"Rkbe3XdEHGSgErCoJBpx8rH9As6nAkEAiytdHVSYHvLn9lBx5LfZTlL0aHxvTC9v" + 
				"VNf4Sm5DACc5vHoGsV9jh8LNqlsrSwlRs1Z/WywedGPFJsJkRdwlFQJAIPOUm4qT" + 
				"TKolWg993ZjUQxX85FDwU8GPCHWoP5QNAy6dUQIpu9qyxvlBDFLuCq4CiF3/GVq8" + 
				"GVVuSUGg2h4ytw==";
		String channel_public_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTLA7IiZomz6LqIo8ehLMRU5ebBDRzhOq+uS/RJ8rAXbQDaQ7jb/kKwVY2kusbP777RMyszeUnlRnYCyZEcriAxeCzp3nF1df+JhHcUbpH60VIbJaaQU/dDdksgVKqm09PG5/bzMPahGmqbvdM1MT+/PqUGeUl9wxgcv1RMu3rvQIDAQAB";
//		String url="https://www.c.api.dinpay.com/makeMerchantPayOrder";
		String url="https://api.dindinsz.com/makeMerchantPayOrder";
		
		
		//计算签名
		//拼接签名字符串
		StringBuilder sb = new StringBuilder();
		sb.append("service_type").append(service_type)
		.append("merchant_code").append(merchant_code)
		.append("input_charset").append(input_charset)
		.append("notify_url").append(notify_url)
		.append("return_url").append(return_url)
		.append("interface_version").append(interface_version)
		.append("merchant_name").append(merchant_name)
		.append("merchant_order_id").append(merchant_order_id)
		.append("order_time").append(order_time)
		.append("order_amount").append(order_amount)
		.append("product_name").append(product_name)
		.append("key").append(merchant_private_key);
		//排序
		Map<String,Object> map = new TreeMap<String,Object>(); //拼接
		map.put("service_type", service_type);
		map.put("merchant_code", merchant_code);
		map.put("input_charset", input_charset);
		map.put("notify_url", notify_url);
		map.put("return_url", return_url);
		map.put("interface_version", interface_version);
		map.put("merchant_name", merchant_name);
		map.put("merchant_order_id", merchant_order_id);
		map.put("order_time", order_time);
		map.put("order_amount", order_amount);
		map.put("product_name", product_name);
		map.put("order_amount", order_amount);
		Set<String> keys = map.keySet(); //遍历映射
		StringBuilder sb1 = new StringBuilder();
		for (String key1 : keys) {
			sb1.append(key1).append("=").append(map.get(key1)).append("&");
		}
		String signStr = sb1.append("key").append(merchant_private_key).toString();
//		signStr = signStr.substring(0, signStr.length()-1);
		
		
		//计算签名
		try {
			String signValue = RSAWithSoftware.signByPrivateKey(signStr, merchant_private_key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		//调通道
		System.out.println(map);
		String result = HttpClientUtils.getInstance().sendPostRequest(url, map,"UTF-8");
		System.out.println(result);
		
	
		
		//输出数据
		String orderJson = null;
		return orderJson;
	}
}
