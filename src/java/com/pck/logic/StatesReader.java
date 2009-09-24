package com.pck.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.pck.logic.State.Next;
import com.pck.mud.CommandLineOptions;
import com.pck.mud.MudController;
import com.pck.util.Logger;

public class StatesReader {
	
	private static final String PARSE_BEGIN = "<<<";
	private static final String PARSE_STATE = "state:";
	private static final String PARSE_TELL = "tell:";
	private static final String PARSE_LLET = ":llet";
	private static final String PARSE_ASK = "ask:";
	private static final String PARSE_NEXT = "next:";
	private static final String PARSE_END = ">>>";
	
	private static final String STATE_IDLE = "idle";
	private static final String STATE_KEYWORD = "keyword";
	private static final String STATE_TEXT = "text";
	
	private static final String SEP = ":";
	
	private Logger logger = null;
	private String line = null;
	private String tell = "";
	private int lineNum = 1;
	private Map<String, State> states = new Hashtable<String, State>();
	
	public StatesReader() {
		logger = Logger.getInstance();
	}
		
	public Map<String, State> parse() throws IOException {
		String filename = MudController.getInstance().options.get(CommandLineOptions.OPTION_STATES_FILENAME);
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String[] splits = null;
		List<String> nextsArray = null;
		String next = null;
		State state = null;
		
		// possible readState: idle, keyword, text
		String readState = STATE_IDLE;
		while ((line = br.readLine()) != null){
			if (line.indexOf(PARSE_BEGIN) == 0 && readState.equals(STATE_IDLE)){
				readState = STATE_KEYWORD;
				state = new State();
				nextsArray = new ArrayList<String>();
			} else if (readState.equals(STATE_KEYWORD)) {
				if (line.indexOf(PARSE_STATE) == 0) {
					splits = line.split(SEP);
					if (splits.length < 2) error();
					state.setName(splits[1]);
					if (splits.length > 2) {
						//logger.debug("This state[" + splits[1] + "] has flag[" + splits[2] +"]");
						String[] stateFlags = new String[splits.length - 2];
						for (int i = 2; i < splits.length; i++ ) {
							stateFlags[i - 2] = splits[i] + "=T";
						}
						state.setFlags(stateFlags);
					}
				} else if (line.indexOf(PARSE_TELL) == 0) {
					tell = "";
					readState = STATE_TEXT;
				} else if (line.indexOf(PARSE_ASK) == 0) {
					splits = line.split(SEP);
					if (splits.length != 2) error();
					state.setAsk(splits[1]);
				} else if (line.indexOf(PARSE_NEXT) == 0) {
					splits = line.split(SEP);
					if (splits.length != 4) error();
					next = splits[1] + "::" + splits[2] + "::" + splits[3].trim();
					nextsArray.add(next);					
				} else if (line.indexOf(PARSE_END) == 0){
					state.setNexts(nextsArray.toArray(new String[0]));
					states.put(state.getName(), state);
					state = null;
					nextsArray = null;
					readState = STATE_IDLE;
				} else {
					error();
				}		
			} else if (line.indexOf(PARSE_LLET) == 0 && readState.equals(STATE_TEXT)) {
				state.setTell(tell);
				readState = STATE_KEYWORD;
			} else if (readState.equals(STATE_TEXT)) {
				tell += line + System.getProperty("line.separator");
			} else if (readState.equals(STATE_IDLE)) {
				
			} else {
				error();
			}	
			lineNum++;
		}
		logger.info("Done reading!");
		return states;
	}
	
	public void error() {
		logger.error("ERROR: line: " + line + "; #" + lineNum);
		System.exit(1);
	}
	
	public void test1(){
		System.out.println("size of states: " + states.size());
		Collection<State> statesCol = states.values();
		for (State aState : statesCol) {
			System.out.println("state:" + aState.getName());
			for (Next aNext : aState.getNexts()){
				System.out.println("-next:" + aNext.id + ";flag:" + aNext.flag + ";input:" + aNext.input + ";nextState:" + aNext.nextState);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		StatesReader sr = new StatesReader();
		sr.parse();
		sr.test1();
	}

}
