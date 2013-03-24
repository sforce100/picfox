package com.fox.utils;

import java.io.File;

public class FileManager {

	/**
	 * 判断文件是否为空
	 * 空就放回true
	 * @param path
	 * @return
	 */
	public static boolean FileIsNull(String path){
		File f=new File(path);
		if(f.length()==0)return true;
		return false;
	}
	
}
