package br.com.neo.smsmanager.socket;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import br.com.mylocation.bean.message.Message;
import br.com.neo.smsmanager.model.ClientInfo;

public class ControllerClient {

    private Observer observer;
	private static ServerSocket serverSocket;
	private Map<SocketChannel, Client> clients;
	private static ControllerClient controllerClient;
	
	public static ControllerClient getInstance(){
		if(controllerClient == null){
			controllerClient = new ControllerClient();
			serverSocket = new ServerSocket(controllerClient);
			controllerClient.setServerSocket(serverSocket);
		}
		return controllerClient;
	}

	private ControllerClient() {
		clients = new HashMap<SocketChannel, Client>();
	}

	public List<ClientInfo> getClientInfoList() {
		List<ClientInfo> clientsList = new ArrayList<ClientInfo>();

		for (Map.Entry<SocketChannel, Client> entry : getClients().entrySet()) {
			Client client = entry.getValue();
			clientsList.add(client.getClientInfo());
		}

		return clientsList;
	}

	public void newClient(SocketChannel socket) {
		Client client = new Client(this, socket, observer);
		getClients().put(socket, client);
	}

	public void receiveMessage(SocketChannel socket, Message message) {
		Client client = getClients().get(socket);
		if (client != null) {
			client.receiveMessage(message);
		}
	}

	public void sendMessage(Client client, Message message) {
		serverSocket.write(client.getSocket(), message);
	}

	public void killClient(SocketChannel socket) {
		Client client = getClients().remove(socket);
		if (client != null) {
			client.kill();
		}
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

	public Map<SocketChannel, Client> getClients() {
		return clients;
	}	
}