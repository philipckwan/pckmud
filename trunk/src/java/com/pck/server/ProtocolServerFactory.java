package com.pck.server;

import com.pck.mud.MudController;
import com.pck.util.MudException;

public class ProtocolServerFactory {
	
	public static ProtocolServer getServer() throws MudException{
		ProtocolServer server = null;
		if (MudController.getInstance().getCurrentMode() == MudController.Mode.TELNET){
			server = TelnetServer.getInstance();
		} else {
			throw new MudException("Unrecognized currentMode");
		}
		return server;
	}
}
