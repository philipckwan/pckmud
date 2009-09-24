package com.pck.server;

import java.io.IOException;
import java.net.*;

import com.pck.mud.MudController;
import com.pck.session.SessionHandler;
import com.pck.session.SessionHandlerFactory;
import com.pck.util.ClientSession;
import com.pck.util.Logger;
import com.pck.util.MudException;
import com.pck.util.ReturnValue;

public class TelnetServer implements ProtocolServer, Runnable{

	private static final int SERVER_PORT = 8091;
	private static final int ONE_MINUTE = 60000;
	public static final String KEY_CLIENT_SOCKET = "client_socket";

	//associations
	private Logger logger = null;
	
	//aggregations
	private static ProtocolServer instance = null;
	private ServerSocket serverSock = null;
	private int activeClientSession = 0;
	
	private TelnetServer(){
		logger = Logger.getInstance();
	}
	
	static ProtocolServer getInstance(){
		if (instance == null){
			synchronized(TelnetServer.class) {
				if (instance == null){
					instance = new TelnetServer();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void startServer() throws MudException {
		try {
			serverSock = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			String msg = "TelnetServer.startServer: ERROR - caught IOException";
			logger.error(e, msg);
			throw new MudException(msg);
		}
		Thread listenThread = new Thread(this);
		listenThread.start();
	}
	
	@Override
	public void stopServer() throws MudException {
		try {
			serverSock.close();
		} catch (IOException e) {
			String msg = "TelnetServer.stopServer: ERROR - caught IOException";
			logger.error(e, msg);
			throw new MudException(msg);
		}
	}

	@Override
	public void run() {
		while (MudController.getInstance().getRunning()){
			while (activeClientSession >= MudController.MAX_NUM_CLIENT_CONNECTION) {
				try {
					Thread.sleep(ONE_MINUTE);
				} catch (InterruptedException e) {
					logger.error(e);
				}
			}
			listenForAClient();
		}
		logger.info("TelnetServer.run: end of run()");
	}
	
	private void listenForAClient(){
		try {
			Socket clientSock = serverSock.accept();
			activeClientSession++;
			ClientSession session = new ClientSession();
					
			logger.info("TelnetServer.listenForAClient: a client is connected at port " + clientSock.getPort());

			session.setClientData(KEY_CLIENT_SOCKET, clientSock);
			// Instantiates a SessionHandler, delegate the client session to it
			SessionHandler sessionHandler = null;
			try {
				sessionHandler = SessionHandlerFactory.getSessionHandler();
			} catch (MudException e) {
				String msg = "TelnetServer.listenForAClient: ERROR - caught MudException in getSessionHandler()";
				logger.error(e, msg);
				MudController.getInstance().shutdown(msg);				
			}
			session.setClientData(MudController.KEY_SESSION_HANDLER, sessionHandler);
			
			ReturnValue retVal = sessionHandler.initClientSession(session);
			if (retVal.getStatus() != ReturnValue.Retcode.SUCCESS){
				String msg = "TelnetServer.listenForClient: ERROR - sessionHandler.initClientSession() failed";
				logger.error(msg);
				MudController.getInstance().shutdown(msg);
			}
		} catch (IOException e) {
			logger.info("TelnetServer.listenForAClient: caught IOException");
		}
		
	}

}
