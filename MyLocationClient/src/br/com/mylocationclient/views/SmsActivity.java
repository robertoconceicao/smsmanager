package br.com.mylocationclient.views;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import br.com.mylocation.bean.message.Command;
import br.com.mylocation.bean.message.Message;
import br.com.mylocation.bean.message.command.PacoteSms;
import br.com.mylocation.define.GlobalDefines;
import br.com.mylocationclient.R;
import br.com.mylocationclient.app.Client;
import br.com.mylocationclient.core.ObserverHost;

public class SmsActivity extends Activity implements ObserverHost {

	private Client client; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		
		client = Client.getInstance();
		client.setObserverHost(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * Metodo q recebe todas as mensagens, comandos enviados para essa atividade
	 */
	@Override
	public void onMessage(Message message) {
		switch(message.getType()) {
			case GlobalDefines.TYPE_COMMAND:
				onCommand((Command) message);				
			break;
			default:
				dialog("Comando Inválido");			
		}
	}
	
	private void onCommand(Command command) {
		switch(command.getOperation()){
		case GlobalDefines.OPERATION_SMS_SEND:
			sendSms((PacoteSms)command.getData());
		}
	}

	public void sendSms(PacoteSms pacoteSms){
		dialog("Enviando sms");
		
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(pacoteSms.getPhone(), 
								null, 
								pacoteSms.getSms(), null, null);
	}
	
	public void dialog(final String message) {
		Runnable run = new Runnable() {
			@Override
			public void run() {				
				Toast.makeText(SmsActivity.this, message, Toast.LENGTH_SHORT).show();		
			}
		};		
		runOnUiThread(run);		
	}
}