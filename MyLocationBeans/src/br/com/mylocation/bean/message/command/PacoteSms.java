package br.com.mylocation.bean.message.command;

import java.io.Serializable;

public class PacoteSms implements Serializable {

	private static final long serialVersionUID = 1L;
	private String phone;
	private String sms;
	
	public PacoteSms(){
	}
	
	public PacoteSms(String phone, String sms) {
		super();
		this.phone = phone;
		this.sms = sms;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
}