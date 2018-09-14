package com.yunkouan.wms.modules.message.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yunkouan.util.FileUtil;

@Component("issFileUtil")
public class IssFileUtil extends FileUtil{

	@Value("${remoteISSFilePath}")
	private String remoteFilePath;
	
	private String localFilePath;
	
	
//	public static void defaultLocalFilePath(){
//		String path = getResourcePath().replace("classes/", "tmpfile/");
//	}
	
	public File[] getFileList(){
		File remoteDoc = new File(remoteFilePath);
		File[] fileList = remoteDoc.listFiles(new FilenameFilter() {
			private Pattern patter = Pattern.compile(".*\\.XML");
			
			public boolean accept(File dir, String name) {
				
				return patter.matcher(name).matches();
			}
		});
		return fileList;
	}
	
	
	public void deleteFile(String fileStr){
		String fileName = remoteFilePath + File.pathSeparator + fileStr;
		File file = new File(fileName);
		deleteFile(file);
	}
	
	public void deleteFile(File file){
		if(file.exists()) file.delete();
	}
	

	public String getRemoteFilePath() {
		return remoteFilePath;
	}

	public void setRemoteFilePath(String remoteFilePath) {
		this.remoteFilePath = remoteFilePath;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public static void main(String[] args) {
		IssFileUtil issFileUtil = new IssFileUtil();
		issFileUtil.setRemoteFilePath("D:\\tmpfile");
		File[] files = issFileUtil.getFileList();
		for(File file:files){
			System.out.println(file.getName()+"======"+file.getPath());
		}
		
//		String name = "ISS01010-ISIS-XYJC-000AB1A3E1C4429991E62BE50137F9E7.XML";
//		Pattern patter = Pattern.compile(".*\\.XML");
//		System.out.println(patter.matcher(name).matches());
		
	}
	
}
