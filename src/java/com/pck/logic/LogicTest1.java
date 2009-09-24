package com.pck.logic;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pck.logic.State.Next;
import com.pck.mud.CommandLineOptions;
import com.pck.mud.MudController;
import com.pck.session.SessionHandler;
import com.pck.util.Ask;
import com.pck.util.ClientSession;
import com.pck.util.Logger;
import com.pck.util.ReturnValue;
import com.pck.util.Tell;

public class LogicTest1 implements Runnable, Logic{
	
	public static final String KEY_FLAG_SET = "flag_set";
	
	//associations
	private ClientSession session = null;
	private Logger logger = null;
	
	//aggregations
	private Map<String, State> states = null;
	private boolean running = true;
	private boolean shutdown = false;
	private State nextState = null;
	
	public LogicTest1(ClientSession session){
		this.session = session;
	}
	
	@Override
	public ReturnValue delegate() {
		Thread logicThread = new Thread(this);
		logicThread.start();
		return new ReturnValue(ReturnValue.Retcode.SUCCESS);
	}
	
	@Override
	public void run() {
		logger = Logger.getInstance();
		try {
			this.states = new StatesReader().parse();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//LogicTest1Helper.setLogicStates(this);
		session.setClientData(KEY_FLAG_SET, new HashSet<String>());
		logger.debug("LogicTest1: clientID: " + session.getID());
		
		logger.info("LogicTest1.run: Starting to interact with this client session: " + session.getID());
		SessionHandler sessHandler = (SessionHandler) session.getClientData(MudController.KEY_SESSION_HANDLER);
		logger.info("size of states: " + states.size());
		nextState = states.get(MudController.getInstance().options.get(CommandLineOptions.OPTION_START_STATE));
		while(running && MudController.getInstance().getRunning()){
			try {
				operate(sessHandler);
			} catch (IOException e) {
				logger.error("LogicTest1.run: caught IOException");
			}
		}
		logger.info("LogicTest1.run: end of run()");
		MudController.getInstance().aClientDisconnect(session);
		if (shutdown == true) {
			MudController.getInstance().shutdown("Shutting down");
		}
	}
	

	@SuppressWarnings("unchecked")
	private void operate(SessionHandler sessHandler) throws IOException{
		// First thing, set nextState to currentState
		State currentState = nextState;
		
		// Set any flags this state has
		String[] flags = currentState.getFlags();
		if (flags != null){
			Set<String> flagSet = (Set<String>) session.getClientData(KEY_FLAG_SET);
			for (String flag : flags) {
				flagSet.add(flag);				
			}
		}
		
		// If there is something to tell, tell it
		Tell tell = new Tell(currentState.getTell());
		if (tell.getMsg() != null) sessHandler.tellClient(session, tell);
		
		// If there is something to ask, ask it
		Ask ask = new Ask(currentState.getAsk());		
		if (ask.getQuestion() != null) {
			sessHandler.askClient(session, ask);
		}		
		String answer = ask.getAnswer();
		
		List<Next> nexts = currentState.getNexts();
		
		if (answer.equals("shutdown")){
			tell.setMsg("Oh no, you are shutting the whole thing down?");
			sessHandler.tellClient(session, tell);
			shutdown = true;
			running = false;
			return;
		} else if (answer.equalsIgnoreCase("quit") || nexts.size() == 0){
			tell.setMsg("Goodbye!");
			sessHandler.tellClient(session, tell);
			running = false;
			return;
		}
		
		if (ask.getQuestion() != null) logger.debug("user typed:" + answer + ":");
		
		for (Next next : nexts) {
			if (next.input != null && (next.input.equals(answer.trim()) || next.input.equals("*"))){				
				nextState = states.get(next.nextState);
				break;
			}
			if (next.flag != null) {
				Set<String> flagSet = (Set<String>) session.getClientData(KEY_FLAG_SET);
				if (flagSet.contains(next.flag)){
					nextState = states.get(next.nextState);
					break;
				}				
			}
		}
		
		
		
	}

}
