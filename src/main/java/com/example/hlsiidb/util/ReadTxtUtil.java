package com.example.hlsiidb.util;

import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取properties文件
 * @author chenxi
 */
public class ReadTxtUtil {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 读取txt文件中的数组值，存放在String数组中
	 * @param filename
	 * @return String数组
	 */
	public static String[] readArray(String filename){
		   StringBuffer stb =new StringBuffer();
		try {   
		        String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
			        File file = new File(filename);   
			         if (file.isFile() && file.exists()) {   //判断文件是否是一个正常文件，而不是一个目录，并且判断文件是否存在
		                   InputStreamReader read = new InputStreamReader( new FileInputStream(file), encoding);   
			               BufferedReader bufferedReader = new BufferedReader(read);   
			               //将BufferedReader与InputStreamReader连接起来，然后BufferedReader就能按行读取InputStreamReader指向的文件
			               String lineTXT = null;   
			              while ((lineTXT = bufferedReader.readLine()) != null) {   
			                 //    System.out.println(lineTXT.toString().trim());  
			                     stb.append(lineTXT.toString().trim());
			               }   
			                read.close();   
			           }else{
			             //error
			            }   
			        } catch (Exception e) {   
			            //System.out.println("读取文件内容操作出错");   
			            e.printStackTrace();   
			       }   
			        String[] array = stb.substring(1,stb.length()-1).split(",");   //取出文本中的每一个值，分别存储在字符串数组中(不包括数值两边的单引号)
			        return array;
	}

	/**
	 * 读取python脚本写入的其他参数值
	 * @param path
	 * @return
	 */
	public static Map<String,String> getOtherStatus(String path){
		StringBuffer stb =new StringBuffer();
		Map<String,String> map = new HashMap<>();
		try {
			String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
			File file = new File(path);
			if (file.isFile() && file.exists()) {   //判断文件是否是一个正常文件，而不是一个目录，并且判断文件是否存在
				InputStreamReader read = new InputStreamReader( new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				//将BufferedReader与InputStreamReader连接起来，然后BufferedReader就能按行读取InputStreamReader指向的文件
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					//    System.out.println(lineTXT.toString().trim());
					stb.append(lineTXT.toString().trim());
					map.put(lineTXT.split("=")[0],lineTXT.split("=")[1]);
				}
				read.close();
			}else{
				//System.out.println("找不到指定的文件！");
			}
		} catch (Exception e) {
			//System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
		return map;
	}
}
