package com.tibco.emea.amxbpm;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AmxBpmClient a = new AmxBpmClient();
		a.startProcess("8DD524B5-6F16-4A06-9F3D-CA5C923D3BEF", "BzWbkTTY");
		a.getWorkItems("8DD524B5-6F16-4A06-9F3D-CA5C923D3BEF");

	}

}
