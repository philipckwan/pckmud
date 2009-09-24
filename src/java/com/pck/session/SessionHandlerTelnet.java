package com.pck.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.pck.mud.MudController;
import com.pck.server.TelnetServer;
import com.pck.util.Ask;
import com.pck.util.ClientSession;
import com.pck.util.Logger;
import com.pck.util.ReturnValue;
import com.pck.util.Tell;

public class SessionHandlerTelnet implements SessionHandler{
	
	public static final String KEY_WRITER = "writer";
	public static final String KEY_READER = "reader";

	//associations
	private Logger logger = null;
	
	//aggregations
	private static SessionHandler instance = null; 
	
	private SessionHandlerTelnet(){
		logger = Logger.getInstance();
	}
	
	static SessionHandler getInstance(){
		if (instance == null){
			synchronized(SessionHandlerTelnet.class) {
				if (instance == null){
					instance = new SessionHandlerTelnet();
				}
			}
		}
		return instance;
	}
	

	@Override
	public ReturnValue initClientSession(ClientSession clientSession) {
		Socket clientSock = (Socket) clientSession.getClientData(TelnetServer.KEY_CLIENT_SOCKET);
		PrintWriter out = null; 
		BufferedReader in = null;
		try {
			out = new PrintWriter(clientSock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
		} catch (IOException e) {
			MudController.getInstance().shutdown(e);
		} 
		clientSession.setClientData(KEY_WRITER, out);
		clientSession.setClientData(KEY_READER, in);
		return MudController.getInstance().aClientConnect(clientSession);
	}

	@Override
	public ReturnValue askClient(ClientSession clientSession, Ask ask) throws IOException {
		PrintWriter out = (PrintWriter) clientSession.getClientData(KEY_WRITER);
		BufferedReader in = (BufferedReader) clientSession.getClientData(KEY_READER);
		out.println(ask.getQuestion());
		
		ask.setAnswer(in.readLine());
		
		out.println();
		return new ReturnValue(ReturnValue.Retcode.SUCCESS);
	}

	@Override
	public ReturnValue tellClient(ClientSession clientSession, Tell tell) throws IOException {
		PrintWriter out = (PrintWriter) clientSession.getClientData(KEY_WRITER);
		BufferedReader in = (BufferedReader) clientSession.getClientData(KEY_READER);
		out.print(tell.getMsg());
		out.flush();
		
		in.readLine();
		
		out.println();
		return new ReturnValue(ReturnValue.Retcode.SUCCESS);
	}

	@Override
	public ReturnValue disconnectClient(ClientSession clientSession) {
		ReturnValue retVal = new ReturnValue(ReturnValue.Retcode.SUCCESS);
		PrintWriter out = (PrintWriter) clientSession.getClientData(KEY_WRITER);
		out.println();
		out.println("Sorry, you have been disconnected!");
		Socket clientSocket = (Socket) clientSession.getClientData(TelnetServer.KEY_CLIENT_SOCKET);
		try {
			clientSocket.close();
		} catch (IOException e) {
			logger.error(e);
			retVal.setStatus(ReturnValue.Retcode.FAILED);
		}
		return retVal;
	}	
}
