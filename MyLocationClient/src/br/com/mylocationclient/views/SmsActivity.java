package br.com.mylocationclient.views;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
		switch (message.getType()) {
		case GlobalDefines.TYPE_COMMAND:
			onCommand((Command) message);
			break;
		default:
			dialog("Comando Inválido");
		}
	}

	private void onCommand(Command command) {
		switch (command.getOperation()) {
		case GlobalDefines.OPERATION_SMS_SEND:
			sendSms(command);
		}
	}

	@SuppressWarnings("deprecation")
	public void sendSms(Command command) {
		dialog("Enviando sms");
		
		String SENT = "SMS_SENT";	
		Intent intentSent = new Intent(SENT);		
		intentSent.putExtra("command", command);
		
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,intentSent, 0);
	
		//—when the SMS has been sent—
		registerReceiver(new BroadcastReceiver() {			
				@Override
				public void onReceive(Context context, Intent intent) {
					Command command = (Command) intent.getExtras().getSerializable("command");
					
					switch (getResultCode()){
					case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent "+command.getRid(), 
					Toast.LENGTH_SHORT).show();
					break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure "+command.getRid(), 
					Toast.LENGTH_SHORT).show();
					break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service "+command.getRid(), 
					Toast.LENGTH_SHORT).show();
					break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU "+command.getRid(), 
					Toast.LENGTH_SHORT).show();
					break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off "+command.getRid(), 
					Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));
		
		PacoteSms pacoteSms = (PacoteSms) command.getData();
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(pacoteSms.getPhone(), null, pacoteSms.getSms(), sentPI, null);
	}
	
	public void dialog(final String message) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				Toast.makeText(SmsActivity.this, message, Toast.LENGTH_SHORT)
						.show();
			}
		};
		runOnUiThread(run);
	}
}