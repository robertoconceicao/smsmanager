package br.com.neo.smsmanager.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import br.com.mylocation.bean.message.Command;
import br.com.mylocation.bean.message.command.PacoteSms;
import br.com.mylocation.define.GlobalDefines;
import br.com.neo.smsmanager.socket.Client;
import br.com.neo.smsmanager.socket.ControllerClient;

/**
 * Servlet implementation class SmsManager
 */
@WebServlet("/SmsManager")
public class SmsManager extends HttpServlet implements Observer {
	private static final long serialVersionUID = 1L;
	private ControllerClient controllerClient;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SmsManager() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		controllerClient = ControllerClient.getInstance();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String de = "";
		if(request.getParameter("de") !=null){
			de = request.getParameter("de");
		}
		String phone = request.getParameter("phone");
		String sms = request.getParameter("sms");
		
		//String propaganda = "\n Sms enviado pelo bob server :-P";
		
		//sms += propaganda;
		
		Map<SocketChannel, Client> celularesDeEnvio = controllerClient.getClients();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Get client's IP address
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }	
		
		PrintWriter out = response.getWriter();
		
		//if the client origin is found in our list then give access 
		//if you don't want to check for origin and want to allow access
		//to all incoming request then change the line to this
		//response.setHeader("Access-Control-Allow-Origin", clientOrigin);
		
		response.setHeader("Access-Control-Allow-Origin", "*");

		JSONObject resposta = new JSONObject();
		resposta.put("status", "OK");
		resposta.put("teste","testando resposta");
		resposta.put("de",de);
		resposta.put("phone",phone);
		resposta.put("sms",sms);
		
		out.print(resposta.toString());
		
		if (celularesDeEnvio.isEmpty()) {
			//out.print("N�o tem celular conectado no servidor para poder enviar o sms, ;-P");			
			out.close();
		} else {
			Client celular = null;
			// pega s� o primeiro por enquanto pra enviar o sms
			// quando tiver muitos envios sera utilizado mais celulares para o
			// envio
			for (Map.Entry<SocketChannel, Client> entry : celularesDeEnvio.entrySet()) {
				celular = entry.getValue();
				break;
			}

			Command command = new Command(GlobalDefines.OPERATION_SMS_SEND);
			PacoteSms pacoteSms = new PacoteSms(phone, sms);
			command.setData(pacoteSms);
			celular.sendMessage(command);

			//out.print("Sms enviado ...");
			out.close();
		}
	}

	@Override
	public void update(Observable observable, Object clientInfoArg) {

	}
}