package com.pck.session;

import com.pck.mud.MudController;
import com.pck.server.ProtocolServer;
import com.pck.server.TelnetServer;
import com.pck.util.MudException;

public class SessionHandlerFactory {
	public static SessionHandler getSessionHandler() throws MudException{
		SessionHandler sessionHandler = null;
		if (MudController.getInstance().getCurrentMode() == MudController.Mode.TELNET){
			sessionHandler = SessionHandlerTelnet.getInstance();	
		} else {
			throw new MudException("Unrecognized currentMode");
		}
		return sessionHandler;
	}
}
