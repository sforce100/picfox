/**
 * 微博用户信息
 */
package com.fox.utils.sql;

public class WeiboTable {
	private   String ID=""; 
	private   String USERID="";
	private   String TOKEN="";
	private   String TOKENSECRET="";
	private   String ACCESS="";
	private   String ACCESSSECRET="";

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getTOKEN() {
		return TOKEN;
	}
	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}
	public String getTOKENSECRET() {
		return TOKENSECRET;
	}
	public void setTOKENSECRET(String tOKENSECRET) {
		TOKENSECRET = tOKENSECRET;
	}
	public String getACCESS() {
		return ACCESS;
	}
	public void setACCESS(String aCCESS) {
		ACCESS = aCCESS;
	}
	public String getACCESSSECRET() {
		return ACCESSSECRET;
	}
	public void setACCESSSECRET(String aCCESSSECRET) {
		ACCESSSECRET = aCCESSSECRET;
	}

}
