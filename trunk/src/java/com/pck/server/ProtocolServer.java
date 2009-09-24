package com.pck.server;

import com.pck.util.*;

/*
 * Defines the interface of a protocol server.
 * Each implementing class provides a server that handles a specific protocol
 *  e.g. telnet, HTTP, swing
 */
public interface ProtocolServer {
	public void startServer() throws MudException;
	public void stopServer() throws MudException;
}
