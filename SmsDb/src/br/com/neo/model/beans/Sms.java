package br.com.neo.model.beans;

public class Sms {
	
	private long id;
	private String message;
	private String phoneFrom;
	private String nameFrom;
	private String phoneTo;
	private String nameTo;
	
	public Sms() {
		this.setId(-1);
		this.setMessage("");
		this.setPhoneFrom("");
		this.setNameFrom("");
		this.setPhoneTo("");
		this.setNameTo("");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPhoneFrom() {
		return phoneFrom;
	}

	public void setPhoneFrom(String phoneFrom) {
		this.phoneFrom = phoneFrom;
	}

	public String getNameFrom() {
		return nameFrom;
	}

	public void setNameFrom(String nameFrom) {
		this.nameFrom = nameFrom;
	}

	public String getPhoneTo() {
		return phoneTo;
	}

	public void setPhoneTo(String phoneTo) {
		this.phoneTo = phoneTo;
	}

	public String getNameTo() {
		return nameTo;
	}

	public void setNameTo(String nameTo) {
		this.nameTo = nameTo;
	}	
	
}
