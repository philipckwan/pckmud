package com.pck.mud;

import java.util.Hashtable;

import com.pck.util.Logger;

public class CommandLineOptions {
	
	public static final String OPTION_STATES_FILENAME = "-states_filename";
	public static final String OPTION_STATES_DIRNAME = "-states_dirname";
	public static final String OPTION_START_STATE = "-start_state";
	
	private static final String OPTION_EXPECT_KEYWORD = "expect_keyword";
	private static final String OPTION_EXPECT_STRING = "expect_string";

	private Logger logger = null;
	
	public CommandLineOptions() {
		logger = Logger.getInstance();
	}
	
	public Hashtable<String, String> parseCommandLine(String[] args) {
		Hashtable<String, String> options = new Hashtable<String, String>();
		String expecting = OPTION_EXPECT_KEYWORD;
		String expectingFor = "";
		
		for(String arg : args){
			if (expecting.equals(OPTION_EXPECT_KEYWORD)) {
				if (arg.equals(OPTION_STATES_FILENAME)) {
					expectingFor = OPTION_STATES_FILENAME;
				} else if (arg.equals(OPTION_STATES_DIRNAME)) {
					expectingFor = OPTION_STATES_DIRNAME;
				} else if (arg.equals(OPTION_START_STATE)) {
					expectingFor = OPTION_START_STATE;
				} else {
					continue;
				}
				expecting = OPTION_EXPECT_STRING;
			} else if (expecting.equals(OPTION_EXPECT_STRING)) {
				options.put(expectingFor, arg);
				expecting = OPTION_EXPECT_KEYWORD;
			}
		}		
		
		return options;
	}

}
