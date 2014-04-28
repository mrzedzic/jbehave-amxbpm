package com.tibco.emea.amxbpm;


public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args)  {
		
		Test t = new Test();
		int counter = 0;
		for(;;){
			try {
				System.out.println("----counter="+ ++counter+"--------");
				t.test();
//				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}
}
