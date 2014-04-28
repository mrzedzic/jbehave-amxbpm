package com.tibco.emea.amxbpm;

import com.tibco.n2.wp.api.WorkResponseDocument.WorkResponse;
import com.tibco.n2.wp.api.base.BaseWorkRequest;

public class CopyOfMain {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		AmxBpmClient a = new AmxBpmClient();
		AmxBpmProcess mainProcess = new AmxBpmProcess();
		LoginInfo login = new LoginInfo("Pawel Kukla", "8DD524B5-6F16-4A06-9F3D-CA5C923D3BEF");
		mainProcess.setOwner(login);
		
		
		mainProcess = a.startProcess(mainProcess.getOwner(), BusinessProcessNames.TestCompleteWorkitemProcess);
		boolean areTasksOnMe = false;
		while(!areTasksOnMe){
			if(a.getUserWorkItemsCount(mainProcess.getOwner())==0){
				areTasksOnMe = false;
				Thread.sleep(5*1000);
				System.out.println(" 3. nie ma jeszcze tasku dla procesu = " + mainProcess.getProcessId());
				
			}else{
				areTasksOnMe = true;
				while(true){
					if(a.getUserWorkItemsCount(mainProcess)==0){
						Thread.sleep(10*1000);
						System.out.println("nie ma jeszcze tasku pod tym procesem");
					}else{
						System.out.println("pojawil się task dla procesu " + mainProcess.getProcessId());
						BaseWorkRequest work = a.getWorkItemFromProcess(mainProcess, login);
						Thread.sleep(5*1000);
						WorkResponse openWorkItem= a.openWorkItem(mainProcess.getOwner(), work.getWorkItem().getId(), work.getWorkItem().getVersion());
						
						a.saveOpenWorkItem(mainProcess.getOwner(), work.getWorkItem().getId(), work.getWorkItem().getVersion(), openWorkItem.getWorkTypeDetail().getUid(), openWorkItem.getWorkTypeDetail().getVersion());
						Thread.sleep(5*1000);
						System.out.println("zamknalem taskid=" +work.getWorkItem().getId() );
					}
				}
			}
		}

	}

}
