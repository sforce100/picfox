/**
 * �Ƚ������б�ͱ��ػ����ļ��б�
 * �����ڻ��彻�������첽����
 * ���ڽ��������첽����
 */
package com.fox.net.upanddown;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fox.utils.Common;
import com.fox.utils.FileManager;
import com.fox.utils.NetUtils;

class NetImageList{
	
	private NetUtils net;;
	private ArrayList<String> tempNetList;
	private List<String> tempLocalList;
	private ArrayList<String> netList;
	private List<String> localList;
	
	public NetImageList(){
		
		net=new NetUtils();
		netList=new ArrayList<String>();
		localList=new ArrayList<String>();
		tempNetList=new ArrayList<String>();
		tempLocalList=new ArrayList<String>();
		
		tempNetList=net.getList(com.fox.utils.Common.DIR);//������ͼƬ�б�
		File files=new File(Common.NetTempDir+Common.DIR);
		if(files.exists()&&files.isDirectory()){
			if(files.list().length!=0){
				
				tempLocalList=(List<String>) Arrays.asList(files.list());//��ñ�����ʱ���ͼƬ�б�
				
			}
		}else{
			files.mkdirs();
		}
	}
	/**
	 * �Ƚ�netList��localList
	 * ��ͬ���Ǳ����л���:��netList���Ƴ�,����tempList
	 * û�ҵ���ͬ���Ǳ���û����:��������
	 */
	public void compareList(){
		
		for(int i=0;i<tempNetList.size();i++){
			String net=tempNetList.get(i);
			int j=0;
			for(;j<tempLocalList.size();j++){
				String local=Common.DIR+"/"+tempLocalList.get(j);
				if(net.equalsIgnoreCase(local) && !FileManager.FileIsNull(local)){
					localList.add(Common.NetTempDir+local);
					System.out.println("local:"+local);
					break;
				}
			}
			if(j==tempLocalList.size()){
				netList.add(net);
				System.out.println("net:"+net);
			}
		}
	}

	public ArrayList<String> getNetList() {
		return netList;
	}
	public void setNetList(ArrayList<String> netList) {
		this.netList = netList;
	}
	public List<String> getLocalList() {
		return localList;
	}
	public void setLocalList(List<String> localList) {
		this.localList = localList;
	}
	

}