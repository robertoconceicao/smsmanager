package br.com.neo.main;

import br.com.neo.model.beans.Sms;
import br.com.neo.model.persistence.DbSms;

public class Main {

	public static void main(String[] args) {
		Sms sms = new Sms();
		sms.setMessage("Mensagemd e teste.");
		sms.setPhoneFrom("9613-7085");
		sms.setNameFrom("Carlito");
		sms.setPhoneTo("1111-1111");
		sms.setNameTo("Maria");
		
		DbSms dbSms = new DbSms();
		dbSms.insertSms(sms);
	}

}
