package com.pck.logic;

import com.pck.util.ClientSession;
import com.pck.util.ReturnValue;

public class InputHandlerKeyword extends InputHandlerBase {

	public InputHandlerKeyword(InputHandlerBase next_) {
		super(next_);
	}

	@Override
	public ReturnValue handleInput(ClientSession session) {
		return null;
	}

}
