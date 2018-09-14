package com.yunkouan.wms.modules.send.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>
 * BASE64编码解码工具包
 * </p>
 * <p>
 * 依赖javabase64-1.3.1.jar
 * </p>
 * 
 * @author IceWee
 * @date 2012-5-19
 * @version 1.0
 */
public class Base64Utils {
	
	private static Logger log = Logger.getLogger(Base64Utils.class); //日志;

	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 1024;

	/**
	 * <p>
	 * BASE64字符串解码为二进制数据
	 * </p>
	 * 
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(String base64) throws Exception {
		return new BASE64Decoder().decodeBuffer(base64);
		// return Base64.decode(base64.getBytes());
	}

	/**
	 * <p>
	 * 二进制数据编码为BASE64字符串
	 * </p>
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encode(byte[] bytes) throws Exception {
		return new BASE64Encoder().encode(bytes).replace("\n", "").replace(
				"\r", "");
		// return new String(Base64.encode(bytes));
	}

	/**
	 * <p>
	 * 将文件编码为BASE64字符串
	 * </p>
	 * <p>
	 * 大文件慎用，可能会导致内存溢出
	 * </p>
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static String encodeFile(String filePath) throws Exception {
	 * byte[] bytes = fileToByte(filePath); return encode(bytes); }
	 */

	/**
	 * <p>
	 * BASE64字符串转回文件
	 * </p>
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @param base64
	 *            编码字符串
	 * @throws Exception
	 */
	/*
	 * public static void decodeToFile(String filePath, String base64) throws
	 * Exception { byte[] bytes = decode(base64); byteArrayToFile(bytes,
	 * filePath); }
	 */

	/**
	 * <p>
	 * 文件转换为二进制数组
	 * </p>
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByte(String filePath) throws Exception {
		byte[] data = new byte[0];
		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
			byte[] cache = new byte[CACHE_SIZE];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				out.write(cache, 0, nRead);
				out.flush();
			}
			out.close();
			in.close();
			data = out.toByteArray();
		}
		return data;
	}

	/**
	 * <p>
	 * 二进制数据写文件
	 * </p>
	 * 
	 * @param bytes
	 *            二进制数据
	 * @param filePath
	 *            文件生成目录
	 */
	public static void byteArrayToFile(byte[] bytes, String filePath)
			throws Exception {
		InputStream in = new ByteArrayInputStream(bytes);
		File destFile = new File(filePath);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		destFile.createNewFile();
		OutputStream out = new FileOutputStream(destFile);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = in.read(cache)) != -1) {
			out.write(cache, 0, nRead);
			out.flush();
		}
		out.close();
		in.close();
	}
	
	// 加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//解码+解密
	public static String getDecodeAndBase64(String str){
		return getFromBase64(java.net.URLDecoder.decode(str));
	}
	
	//加密+编码
	@SuppressWarnings("deprecation")
	public static String getEncodeAndBase64(String str){
		return java.net.URLEncoder.encode(getBase64(str));
	}
    
}
