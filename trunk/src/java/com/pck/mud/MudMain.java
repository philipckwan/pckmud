package com.pck.mud;

public class MudMain {

	public static void main(String[] args) {		
		System.out.println("MudMain.main: start of main()");
		MudController mudController = MudController.getInstance();
		mudController.init(args);
		mudController.go();		
		System.out.println("MudMain.main: end of main()");	
	}

}
