/**
 * 比较网络列表和本地缓冲文件列表
 * 不存在缓冲交给网络异步加载
 * 存在交给本地异步加载
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
		
		tempNetList=net.getList(com.fox.utils.Common.DIR);//获得相册图片列表
		File files=new File(Common.NetTempDir+Common.DIR);
		if(files.exists()&&files.isDirectory()){
			if(files.list().length!=0){
				
				tempLocalList=(List<String>) Arrays.asList(files.list());//获得本地临时相册图片列表
				
			}
		}else{
			files.mkdirs();
		}
	}
	/**
	 * 比较netList和localList
	 * 相同就是本地有缓存:从netList中移除,放入tempList
	 * 没找到相同就是本地没缓存:不做操作
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