package com.fox.utils;

import java.io.File;

public class FileManager {

	/**
	 * �ж��ļ��Ƿ�Ϊ��
	 * �վͷŻ�true
	 * @param path
	 * @return
	 */
	public static boolean FileIsNull(String path){
		File f=new File(path);
		if(f.length()==0)return true;
		return false;
	}
	
}
