package com.tibco.emea.amxbpm;

import com.tibco.n2.process.management.api.ProcessInstance;
import com.tibco.n2.wp.api.WorkResponseDocument.WorkResponse;
import com.tibco.n2.wp.api.base.BaseWorkRequest;

public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		AmxBpmClient a = new AmxBpmClient();
		AmxBpmProcess mainProcess = new AmxBpmProcess();
		LoginInfo login1 = new LoginInfo("Pawel Kukla", "8DD524B5-6F16-4A06-9F3D-CA5C923D3BEF");
		LoginInfo login2 = new LoginInfo("Marek Kozierski", "6358CEAE-198C-474A-B331-CCF2D5A57641");
		mainProcess.setOwner(login1);
		
		mainProcess = a.startProcess(mainProcess.getOwner(), "TestCompleteWorkitemProcess");
		boolean areTasksOnMe = false;
		byte howLongIWait = -1;
		while(!areTasksOnMe){
			if(a.getUserWorkItemsCount(login1)==0){
				areTasksOnMe = false;
				Thread.sleep(5*1000);
				howLongIWait++;
				System.out.println("3. nie ma jeszcze tasku dla procesu = " + mainProcess.getProcessId()+"; howLongIWait="+ howLongIWait);
				
			}else{
				areTasksOnMe = true;
				
				while(true){
					if(a.getUserWorkItemsCount(mainProcess)==0){
						Thread.sleep(5*1000);
						howLongIWait++;
						System.out.println("nie ma jeszcze tasku pod tym procesem"+"; howLongIWait="+ howLongIWait);
						if(howLongIWait>1){
							ProcessInstance p =  a.getProcessStatus(mainProcess);
							if(p.getState().equals(AmxBpmProcessState.COMPLETED.toString())){
								System.out.println("koniec bo process "+ mainProcess.getProcessId() +"=" +p.getState());
								break;
							}else{
								System.out.println("pewnie proces jest pod innym userem bo ciągle jest "+ p.getState());
								BaseWorkRequest work = a.getWorkItemFromProcess(mainProcess, login2);
								Thread.sleep(5*1000);
								WorkResponse openWorkItem= a.openWorkItem(login2, work.getWorkItem().getId(), work.getWorkItem().getVersion());
								
								a.saveOpenWorkItem(login2, work.getWorkItem().getId(), work.getWorkItem().getVersion(), openWorkItem.getWorkTypeDetail().getUid(), openWorkItem.getWorkTypeDetail().getVersion());
								Thread.sleep(5*1000);
								System.out.println("zamknalem taskid=" +work.getWorkItem().getId()+"; howLongIWait="+ howLongIWait);
								howLongIWait=0;
							}
						}
					}else{
						BaseWorkRequest work = a.getWorkItemFromProcess(mainProcess, login1);
						Thread.sleep(5*1000);
						WorkResponse openWorkItem= a.openWorkItem(login1, work.getWorkItem().getId(), work.getWorkItem().getVersion());
						
						a.saveOpenWorkItem(login1, work.getWorkItem().getId(), work.getWorkItem().getVersion(), openWorkItem.getWorkTypeDetail().getUid(), openWorkItem.getWorkTypeDetail().getVersion());
						Thread.sleep(5*1000);
						System.out.println("zamknalem taskid=" +work.getWorkItem().getId()+"; howLongIWait="+ howLongIWait);
						howLongIWait=0;
					}
				}
			}
		}

	}
}
