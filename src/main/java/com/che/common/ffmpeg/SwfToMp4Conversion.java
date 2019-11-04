/**
 * 
 */
package com.che.common.ffmpeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;
import com.che.common.web.GlobalInfo;

/**
 * @author karlhell
 *
 */
public class SwfToMp4Conversion {
	private static final Logger logger = LogManager.getLogger(SwfToMp4Conversion.class);
	
	private static String swftomp4OutFolder = GlobalInfo.getInstance().swftomp4OutFolder;
	private static String swftomp4Image = GlobalInfo.getInstance().swftomp4Image;
	private static String swftomp4Ffmpeg = GlobalInfo.getInstance().swftomp4Ffmpeg;
//	private static String swftomp4OutFolder = "/src/main/resources/bin/";
//	private static String swftomp4Image = "http://192.168.1.13/img";
//	private static String swftomp4Ffmpeg = "/src/main/resources/bin/ffmpeg.exe";
	

	/**
	 * swf转mp4
	 * @param model 驾照类型 ： a,b,c
	 * @param infile 这个就传从聚合获得到的URL
	 * @return 返回新的mp4的url地址
	 */
	public static String processSwfToMp4(String infile) {
		if(StringUtils.isEmpty(infile)){
			return null;
		}
		
		String fileName = infile.substring(infile.lastIndexOf("/")+1);
		fileName = fileName.substring(0,fileName.indexOf("."))+".mp4";
        List<String> commend = Lists.newArrayListWithCapacity(4);
        
        if(System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0){
        	commend.add(System.getProperty("user.dir")+swftomp4Ffmpeg);  
        	commend.add(System.getProperty("user.dir")+swftomp4OutFolder+fileName);
        }else{
        	commend.add(swftomp4Ffmpeg);  
        	commend.add(swftomp4OutFolder+fileName);
        }
        
        commend.add(1,"-i");
        commend.add(2,infile);  
        commend.add(3,"-y");  
        
        ProcessBuilder processBuilder = new ProcessBuilder(); 
        processBuilder.command(commend);  
//	        processBuilder.directory(path);//切换工作目录  
        processBuilder.redirectErrorStream(true);  
        BufferedReader reader = null;
        try {  
		       Process process = processBuilder.start();  
		       StringBuilder result = new StringBuilder();  
		       reader = new BufferedReader(new InputStreamReader(process.getInputStream()));  
        
               String line;  
               while ((line = reader.readLine()) != null) {  
                   result.append(line);
                   logger.info(processBuilder.command().toString() + " --->: " + line);
               }  
 
               process.waitFor();
               logger.info(process.exitValue());
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {  
        	IOUtils.close(reader);
        }  
        
        return swftomp4Image+"/"+fileName;  
	}  
	  
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(processSwfToMp4("http://images.juheapi.com/jztk/subject4/34.swf"));

	}
}
