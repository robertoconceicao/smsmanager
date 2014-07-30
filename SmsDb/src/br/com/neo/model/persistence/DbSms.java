package br.com.neo.model.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.neo.model.beans.Sms;

public class DbSms {

	public boolean insertSms(Sms sms) {
		try {
			PreparedStatement stmt = ConnectionFactory
					.getConnection()
					.prepareStatement(
							"insert into sms (\"message\", \"phone_from\", \"name_from\", \"phone_to\", \"name_to\") values (?,?,?,?,?);");
			stmt.setString(1, sms.getMessage());
			stmt.setString(2, sms.getPhoneFrom());
			stmt.setString(3, sms.getNameFrom());
			stmt.setString(4, sms.getPhoneTo());
			stmt.setString(5, sms.getNameTo());
			stmt.execute();

			stmt.close();

		} catch (SQLException ex) {
			return false;
		}

		return true;
	}
}
