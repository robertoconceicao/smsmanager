package br.com.neo.smsmanager.socket;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Observer;

import br.com.mylocation.bean.message.Message;
import br.com.neo.smsmanager.model.ClientInfo;
import br.com.neo.smsmanager.protocol.SwitchMessages;

public class Client {

	private SwitchMessages switchMessages;
	private ControllerClient controllerClient;
	private SocketChannel socket;
	private ClientInfo clientInfo;

	public Client(ControllerClient controllerClient, SocketChannel socket, Observer observer) {
		System.out.println("Novo cliente...");
		this.controllerClient = controllerClient;
		this.socket = socket;
		switchMessages = new SwitchMessages(this);
		clientInfo = new ClientInfo(observer);
	}

	public ClientInfo getClientInfo() {
		return clientInfo;
	}

	public SocketChannel getSocket() {
		return socket;
	}

	public void receiveMessage(Message message) {
		System.out.println("Cliente recebeu mensagem...");
		switchMessages.switchMessage(message);
	}

	public void sendMessage(Message message) {
		System.out.println("Cliente escreveu mensagem...");
		controllerClient.sendMessage(this, message);
	}
	
	public void removeAndKill(){
		controllerClient.killClient(socket);
		kill();
	}

	public void kill() {
		System.out.println("Matando cliente...");
		clientInfo.kill();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		switchMessages = null;
		controllerClient = null;
		socket = null;
		clientInfo = null;
	}
}
