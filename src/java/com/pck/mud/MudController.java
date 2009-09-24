package com.pck.mud;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.pck.logic.Logic;
import com.pck.logic.LogicInstFactory;
import com.pck.server.ProtocolServer;
import com.pck.server.ProtocolServerFactory;
import com.pck.session.SessionHandler;
import com.pck.session.SessionHandlerFactory;
import com.pck.util.ClientSession;
import com.pck.util.Logger;
import com.pck.util.MudException;
import com.pck.util.ReturnValue;

/* 
 * This class is a mediator (see Mediator pattern)
 *  that means all dirty logic goes here.
 */
public class MudController {

	public static final String KEY_SESSION_HANDLER = "session_handler";
	public enum Mode { TELNET }
	public static final int MAX_NUM_CLIENT_CONNECTION = 5;
	
	private static final int CLIENT_ID_UPPER_LIMIT = 10000;	
	
	//associations
	private Logger logger = null;
	
	// aggregation
	public Map<String, String> options;
	
	private static MudController instance;
	private final Mode currentMode = Mode.TELNET;
	private boolean running = true;
	// A table of client ID to client session;
	private Map<String, ClientSession> clientsTable;
	private ProtocolServer server = null;
	private Thread mainThread = null;

	//public static final String KEY_MUD_MAIN = "mud_main";

	
	private MudController() {
		logger = Logger.getInstance();
	}

	public static MudController getInstance(){
		if (instance == null){
			synchronized(MudController.class) {
				if (instance == null){
					instance = new MudController();
				}
			}
		}
		return instance;
	}
	
	public void init(String[] args) {	
		
		CommandLineOptions parser = new CommandLineOptions();
		options = parser.parseCommandLine(args);
		
		clientsTable = new Hashtable<String, ClientSession>();
		try {
			server = ProtocolServerFactory.getServer();
		} catch (MudException e1) {
			logger.error("MudController.init: ProtocolServerFactory.getServer() failed: " + e1.getRetValue().getMsg());
			System.exit(1);
		}
		mainThread = Thread.currentThread();
	}

	public void go() {
				
		try {
			server.startServer();
		} catch (MudException e1) {
			this.mainShutdown();
		}
		
		while(true){
			try{
				Thread.sleep(60000);
			} catch (InterruptedException e){
				logger.info("MudController.runMain: I am interrupted");
				break;
			}
		}
		this.mainShutdown();
		
	}
	
	public ReturnValue aClientConnect(ClientSession client) {
		// Generate and assign an unused client ID
		// range is 1 <= ID <= CLIENT_ID_UPPER_LIMIT
		int clientIDInt;
		String clientID;
		do {
			clientIDInt = new Random().nextInt(CLIENT_ID_UPPER_LIMIT - 1) + 1;
			clientID = String.valueOf(clientIDInt);
		} while (clientsTable.containsKey(clientID));
		clientsTable.put(clientID, client);
		client.setID(clientID);
		logger.debug("clientID: " + clientID);
		// Get an instance of MudMainLogic and delegate the client session to it
		ReturnValue retVal = null;
		try {
			Logic logic = LogicInstFactory.getLogicInst(LogicInstFactory.Mode.TEST_1, client);
			 retVal = logic.delegate();
		} catch (MudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if (retVal.getStatus() != ReturnValue.Retcode.SUCCESS){
			String msg = "MudController.aClientConnect: ERROR - MudMainLogic.breakout() failed";
			logger.error(msg);
			this.shutdown();
		}
		return retVal;
	}
	
	public ReturnValue aClientDisconnect(ClientSession client) {

		SessionHandler sessionHandler = (SessionHandler) client.getClientData(MudController.KEY_SESSION_HANDLER);
		ReturnValue retVal = sessionHandler.disconnectClient(client);

		clientsTable.remove(client.getID());
		return retVal;
	}

	public Mode getCurrentMode() {
		return currentMode;
	}
	
	public synchronized boolean getRunning() {
		return this.running;
	}
	
	private synchronized void setRunning(boolean running){
		this.running = running;
	}
	
	private void mainShutdown(){
		this.setRunning(false);
		try {
			server.stopServer();
		} catch (MudException e) {
		}
		ClientSession clientSession;
		Iterator<ClientSession> clientSessions = clientsTable.values().iterator();	
		SessionHandler sessionHandler = null;
		while (clientSessions.hasNext()){
			clientSession = clientSessions.next();
			sessionHandler = (SessionHandler) clientSession.getClientData(MudController.KEY_SESSION_HANDLER);
			sessionHandler.disconnectClient(clientSession);
		}
		logger.info("MudController.mainShutdown: someone asking me to shutdown, shutting down now");
	}
	
	public void shutdown() {
		// Interrupt the main thread to shutdown
		mainThread.interrupt();		
	}

	public void shutdown(String msg) {
		logger.error("MudController.abort: " + msg);
		this.shutdown();	
	}

	public void shutdown(MudException mex) {
		logger.error("MudController.abort: caught MudException...");
		logger.error(" retcode: " + mex.getRetValue().getStatus());
		logger.error(" msg: " + mex.getRetValue().getMsg());
		this.shutdown();	
	}

	public void shutdown(Exception ex) {
		logger.error("MudController.abort: caught Exception...");
		logger.error(ex);
		this.shutdown();	
	}
	
	

}
