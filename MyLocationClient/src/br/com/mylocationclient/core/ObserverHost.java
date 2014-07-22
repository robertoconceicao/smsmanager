package br.com.mylocationclient.core;

import br.com.mylocation.bean.message.Message;

public interface ObserverHost {

	void onMessage(Message message);
}
