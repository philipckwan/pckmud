package com.pck.logic;

import com.pck.mud.MudController;
import com.pck.util.ClientSession;
import com.pck.util.MudException;

public class LogicInstFactory {
	public enum Mode { TEST_1 }
	
	public static Logic getLogicInst(LogicInstFactory.Mode mode, ClientSession clientSession) throws MudException {
		Logic logic = null;
		if (mode == Mode.TEST_1){
			logic = new LogicTest1(clientSession);	
		} else {
			throw new MudException("Unrecognized currentMode");
		}
		return logic;
	}
}
