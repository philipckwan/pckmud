package com.pck.session;

import java.io.IOException;

import com.pck.util.Ask;
import com.pck.util.ClientSession;
import com.pck.util.ReturnValue;
import com.pck.util.Tell;

public interface SessionHandler {
	public ReturnValue initClientSession(ClientSession clientSession);
	public ReturnValue tellClient(ClientSession clientSession, Tell tell) throws IOException;
	public ReturnValue askClient(ClientSession clientSession, Ask ask) throws IOException;
	public ReturnValue disconnectClient(ClientSession clientSession);
}
