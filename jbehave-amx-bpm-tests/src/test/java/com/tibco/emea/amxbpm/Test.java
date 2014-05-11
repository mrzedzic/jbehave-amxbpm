package com.tibco.emea.amxbpm;

import com.tibco.n2.wp.api.WorkResponseDocument.WorkResponse;
import com.tibco.n2.wp.api.base.BaseWorkRequest;

public class Test {
	AmxBpmClient a = new AmxBpmClient();
	AmxBpmProcess mainProcess = new AmxBpmProcess();
	LoginInfo login1 = new LoginInfo("Pawel Kukla", "8DD524B5-6F16-4A06-9F3D-CA5C923D3BEF");
	LoginInfo login2 = new LoginInfo("Marek Kozierski", "6358CEAE-198C-474A-B331-CCF2D5A57641");
	LoginInfo login3 = new LoginInfo("Dyrektor Kredytowy", "FA8CF97D-C6B4-43EB-9AE6-321245F41B11");
	LoginInfo login4 = new LoginInfo("Dyrektor Biznesowy", "EB73EA46-A3C4-467F-9A57-710A2CB7E61C");
	
	
	public void test() throws InterruptedException, InstantiationException, IllegalAccessException{
		byte stepNo = 0;
		mainProcess.setOwner(login1);
		
		System.out.print(++stepNo+".");
		System.out.println(a.getUserDetails(login1.getUserName()).getGuid());
		mainProcess = a.startProcess(mainProcess.getOwner(), BusinessProcessNames.BzWbkTTY);
		Thread.sleep(5*1000);
		
		byte howLongIWait = -1;
		
		while(true){
			System.out.print(++stepNo+".");
			if(a.getUserWorkItemsCount(login1)!=0){
				openProcessClose(login1, stepNo);
				howLongIWait=0;
			}else if(a.getUserWorkItemsCount(login2)!=0){
				openProcessClose(login2, stepNo);
				howLongIWait=0;
			}else if(a.getUserWorkItemsCount(login3)!=0){
				openProcessClose(login3, stepNo);
				howLongIWait=0;
			}else if(a.getUserWorkItemsCount(login4)!=0){
				openProcessClose(login4, stepNo);
				howLongIWait=0;
			}else{
				++howLongIWait;
				if(howLongIWait>3){
					System.out.println(" Koniec procesu=" + mainProcess.getProcessId());
					break;
				}
			}
		}
	}
	
	public void openProcessClose(LoginInfo login, byte step) throws InterruptedException, InstantiationException, IllegalAccessException{
		BaseWorkRequest work = a.getWorkItemFromProcess(mainProcess, login);
//		Thread.sleep(5*1000);
		WorkResponse openWorkItem= a.openWorkItem(login, work.getWorkItem().getId(), work.getWorkItem().getVersion());
		a.saveOpenWorkItem(login, work.getWorkItem().getId(), work.getWorkItem().getVersion(), openWorkItem.getWorkTypeDetail().getUid(), openWorkItem.getWorkTypeDetail().getVersion());
		Thread.sleep(5*1000);
		System.out.println(step+".2 "+login.toString()+" zamknal taskid=" +work.getWorkItem().getId());
	}
}
