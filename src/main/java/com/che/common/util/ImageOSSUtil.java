package com.che.common.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.aliyun.oss.OSSClient;
import com.che.common.web.GlobalInfo;

public class ImageOSSUtil {
	
	 	static Logger logger = Logger.getLogger(ImageOSSUtil.class);
 
	    private static String endpoint =  GlobalInfo.getInstance().ossEndpoint;
	    private static String accessKeyId =GlobalInfo.getInstance().ossAccessKeyId;
	    private static String accessKeySecret = GlobalInfo.getInstance().ossAccessKeySecret;
	    private static String bucketName =GlobalInfo.getInstance().ossBucketName;
 
 
	    public static void upload(String  targetFile,String sourceFile){
	    	 OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	    	 ossClient.putObject(bucketName, targetFile, new File(sourceFile));
	    	 ossClient.shutdown();
	    }
	    
}
