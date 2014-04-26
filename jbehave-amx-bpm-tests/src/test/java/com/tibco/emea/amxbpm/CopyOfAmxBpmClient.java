/**
 * 
 */
package com.tibco.emea.amxbpm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.tibco.bpm.service.connector.ServiceConnector;
import com.tibco.bpm.service.connector.ServiceConnectorFactory;
import com.tibco.n2.brm.api.GetWorkListItemsResponseDocument.GetWorkListItemsResponse;
import com.tibco.n2.brm.api.ManagedObjectID;
import com.tibco.n2.brm.api.OrderFilterCriteria;
import com.tibco.n2.brm.api.WorkItem;
import com.tibco.n2.brm.services.InvalidEntityFault;
import com.tibco.n2.brm.services.SecurityFault;
import com.tibco.n2.brm.services.WorkItemFilterFault;
import com.tibco.n2.brm.services.WorkItemOrderFault;
import com.tibco.n2.common.channeltype.ChannelType;
import com.tibco.n2.common.datafeed.DataPayload;
import com.tibco.n2.common.datafeed.PayloadModeType;
import com.tibco.n2.common.organisation.api.OrganisationalEntityType;
import com.tibco.n2.common.organisation.api.XmlModelEntityId;
import com.tibco.n2.de.api.XmlModelEntity;
import com.tibco.n2.de.api.resolver.LookupUserResponseDocument.LookupUserResponse;
import com.tibco.n2.de.services.InternalServiceFault;
import com.tibco.n2.de.services.InvalidServiceRequestFault;
import com.tibco.n2.process.management.api.BasicProcessTemplate;
import com.tibco.n2.process.management.api.OperationInfo;
import com.tibco.n2.process.management.api.QualifiedProcessName;
import com.tibco.n2.process.management.api.StarterOperation;
import com.tibco.n2.process.management.services.IllegalArgumentFault;
import com.tibco.n2.process.management.services.OperationFailedFault;
import com.tibco.n2.service.adapter.config.Configuration;
import com.tibco.n2.service.connector.config.context.DefaultSecurityHandler;
import com.tibco.n2.service.connector.config.context.SecurityHandler;
import com.tibco.n2.wp.api.WorkResponseDocument.WorkResponse;
import com.tibco.n2.wp.api.base.ActionType;
import com.tibco.n2.wp.api.base.BaseWorkRequest;
import com.tibco.n2.wp.api.base.BaseWorkRequest.WorkTypeDetail;
import com.tibco.n2.wp.api.base.WorkRequest;
import com.tibco.n2.wp.services.ChainedTimeOutFault;
import com.tibco.n2.wp.services.DataOutOfSyncFault;
import com.tibco.n2.wp.services.InvalidWorkRequestFault;
import com.tibco.n2.wp.services.WorkItemUnavailableFault;
import com.tibco.n2.wp.services.WorkProcessingFault;
/**
 * @author mrzedzic
 * 
 */
public class CopyOfAmxBpmClient {

	Configuration.Protocol protocol = Configuration.Protocol.HTTP;
	String host = "localhost";
	int port = 8080;
	String username = "Pawel Kukla";
	LoginInfo user = new LoginInfo(username, null);
	
	SecurityHandler securityHandler = new DefaultSecurityHandler(username, "alamakota");
	ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler);

	public CopyOfAmxBpmClient() {
		try {
			String guid = null;
			LookupUserResponse lookupUserResponse = serviceConnector.getEntityResolverService().lookupUser(username, null, null, true);
			if (lookupUserResponse.getDetailArray().length > 0) {
				guid = lookupUserResponse.getDetailArray(0).getGuid();
			} else {

			}
		}

		catch (com.tibco.n2.de.services.SecurityFault e) {

		} catch (InvalidServiceRequestFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalServiceFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AmxBpmProcess startProcess(LoginInfo login, String processName) {
		QualifiedProcessName process = QualifiedProcessName.Factory.newInstance();
		process.setProcessName(processName);
		AmxBpmProcess newProcess = new AmxBpmProcess();
		newProcess.setOwner(login);
		try {
			// Step 1 : List the available templates
			BasicProcessTemplate[] templates = serviceConnector.getProcessManagerService().listProcessTemplates(process);
			// Step 2 : Get the starter operations, in this example we use the
			// first template we got back
			if (templates != null && templates.length > 0) {
				StarterOperation[] operations = serviceConnector.getProcessManagerService().listStarterOperations(templates[0].getProcessQName());
				// Step 3 : Check the starter operation - in this example we use
				// the first starter operation we got back
				if (operations != null && operations.length > 0) {
					OperationInfo info = serviceConnector.getProcessManagerService().getStarterOperationInfo(templates[0].getProcessQName(), operations[0].getOperation());
					String processId = serviceConnector.getProcessManagerService().createProcessInstance(templates[0].getProcessQName(), operations[0].getOperation(), null);
					System.out.println("1. Utworzyłem proces = "+processId);
					newProcess.setProcessId(processId);
					newProcess.setState(AmxBpmProcessState.ACTIVE);
				}
			}
		} catch (IllegalArgumentFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationFailedFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newProcess;
	}

	public int getUserWorkItemsCount(LoginInfo login) {
		int startPos = 0;
		int numberOfItems = 10;
		int count = 0;

		XmlModelEntityId entityId = buildEntityId(login.getUserName(), login.getGuid());
		OrderFilterCriteria oc = null;

		try {
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc, entityId, startPos, numberOfItems);

			System.out.println("2. Sprwdzam czy są taski na mnie, ile="+ items.sizeOfWorkItemsArray());
			count = items.sizeOfWorkItemsArray();

		} catch (InvalidEntityFault e) {
			e.printStackTrace();
		} catch (com.tibco.n2.brm.services.InternalServiceFault e) {
			e.printStackTrace();
		} catch (WorkItemOrderFault e) {
			e.printStackTrace();
		} catch (WorkItemFilterFault e) {
			e.printStackTrace();
		} catch (SecurityFault e) {
			e.printStackTrace();
		}
		return count;
	}
	public int getUserWorkItemsCount(AmxBpmProcess process) {
		int startPos = 0;
		int numberOfItems = 10;
		int count = 0;

		XmlModelEntityId entityId = buildEntityId(process.getOwner().getUserName(), process.getOwner().getGuid());
		OrderFilterCriteria oc = null;;

		try {
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc, entityId, startPos, numberOfItems);

//			System.out.println("items.sizeOfWorkItemsArray()="+ items.sizeOfWorkItemsArray());
			count = items.sizeOfWorkItemsArray();

		} catch (InvalidEntityFault e) {
			e.printStackTrace();
		} catch (com.tibco.n2.brm.services.InternalServiceFault e) {
			e.printStackTrace();
		} catch (WorkItemOrderFault e) {
			e.printStackTrace();
		} catch (WorkItemFilterFault e) {
			e.printStackTrace();
		} catch (SecurityFault e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public BaseWorkRequest getWorkItemFromProcess(AmxBpmProcess process) {
		int startPos = 0;
		int numberOfItems = 10;
		int count = 0;
		BaseWorkRequest workRequest = null;

		XmlModelEntityId entityId = buildEntityId(process.getOwner().getUserName(), process.getOwner().getGuid());
		OrderFilterCriteria oc = null; //buildOrderCriteria(process);

		try {
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc, entityId, startPos, numberOfItems);
			System.out.println("size=" +  items.getWorkItemsArray().length);
			for (WorkItem myWorkItem : items.getWorkItemsArray()) {
				workRequest = getWorkRequest(myWorkItem.getId().getId(), myWorkItem.getId().getVersion(), process.getOwner());
//				System.out.println("WorkItem,id="+myWorkItem.getId().getId()+";version="+myWorkItem.getId().getVersion()); //+;process="+myWorkItem.getAttributes().getAttribute14()
			}
		} catch (InvalidEntityFault e) {
			e.printStackTrace();
		} catch (com.tibco.n2.brm.services.InternalServiceFault e) {
			e.printStackTrace();
		} catch (WorkItemOrderFault e) {
			e.printStackTrace();
		} catch (WorkItemFilterFault e) {
			e.printStackTrace();
		} catch (SecurityFault e) {
			e.printStackTrace();
		}
		return workRequest;
	}
	
	public WorkResponse openWorkItem(LoginInfo login, long workItemId, long workItemVersion) {
		BaseWorkRequest workRequest = null;
		WorkResponse openWorkItem = WorkResponse.Factory.newInstance();
		String uid = "";
		try{
			workRequest = getWorkRequest(workItemId, workItemVersion, login);	
			openWorkItem = serviceConnector.getWorkPresentationService().openWorkItem(workRequest);
			uid = openWorkItem.getWorkTypeDetail().getUid();
//			System.out.println(openWorkItem.getPayloadModel().getSerializedPayload());
//			System.out.println(uid + " " + openWorkItem.getWorkTypeDetail().getVersion());

		} catch (InvalidWorkRequestFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WorkItemUnavailableFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (com.tibco.n2.wp.services.InternalServiceFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return openWorkItem;
	}

	public void saveOpenWorkItem(LoginInfo login, long workItemId, long workItemVersion, String uid, String uidVersion) {
		
		WorkRequest request = getWorkRequestS(workItemId, workItemVersion+1, login, uid, uidVersion);
		request.setResourceId(login.getGuid());
		DataPayload payload = request.addNewPayloadDetails();
		payload.setPayloadMode(PayloadModeType.JSON);
//		File text = new File("C:/workspace/amx_bpm_bzwbk/BzWbkSpotfireSimulation/Forms/BzWbkSpotfireSimulation/1_Przygotowanie/WybrKlientaiGrupy/WybrKlientaiGrupy.data.json")
		String myJsonPayload = "";
		 FileInputStream inputStream = null;
//		 String file = "C:/workspace/amx_bpm_bzwbk/BzWbkSpotfireSimulation/Forms/BzWbkSpotfireSimulation/1_Przygotowanie/WybrKlientaiGrupy/MyWybrKlientaiGrupy.data.json";
		 String file = "C:/workspace/amx_bpm_bzwbk/TestCompleteWorkitem/Forms/TestCompleteWorkitem/TestCompleteWorkitemProcess/UserTask/MyUserTask.data.json";
		try {
			inputStream = new FileInputStream(file);
			myJsonPayload = IOUtils.toString(inputStream);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		       try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		payload.setSerializedPayload(myJsonPayload);
		
		request.setChannelId("openspaceGWTPull_DefaultChannel");
		request.setChannelType(ChannelType.OPENSPACE_CHANNEL);
		
		request.setAction(ActionType.COMPLETE);
		
		try {
			serviceConnector.getWorkPresentationService().completeWorkItem(request);
			
		} catch (WorkProcessingFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidWorkRequestFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (com.tibco.n2.wp.services.InternalServiceFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataOutOfSyncFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChainedTimeOutFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private XmlModelEntity buildEntityId(String userName, String guid) {
		/** Cretate an instance of entity Id */
		XmlModelEntity entityId = XmlModelEntity.Factory.newInstance();
		/** set the user GUID */
		entityId.setGuid(guid);
		/** set the user name */
		entityId.setName(userName);
		/** set the entity type as RESOURCE */
		entityId.setEntityType(OrganisationalEntityType.RESOURCE);
		/** set the version of organisation */
		entityId.setModelVersion(-1);
		return entityId;
	}
	
	private ManagedObjectID buildManagedObjectID(Long workItemId, Long workItemVersion) {
		ManagedObjectID workItemObjId = ManagedObjectID.Factory.newInstance();
		workItemObjId.setId(workItemId);
		workItemObjId.setVersion(workItemVersion);
		return workItemObjId;
	}
	
	private OrderFilterCriteria buildOrderCriteria(AmxBpmProcess process) {
		OrderFilterCriteria orderCriteria = OrderFilterCriteria.Factory.newInstance();
		orderCriteria.setFilter("attribute14='" + process.getProcessId()+"'");
		return orderCriteria;
	}
	
	private BaseWorkRequest getWorkRequest(long workItemId, long workItemVersion, LoginInfo login){
		BaseWorkRequest workRequest = BaseWorkRequest.Factory.newInstance();
		com.tibco.n2.wp.api.base.WorkItem workItem2Open = workRequest.addNewWorkItem();
		workItem2Open.setId(workItemId);
		workItem2Open.setVersion(workItemVersion);
		workRequest.setResourceId(login.getGuid());
		WorkTypeDetail workTypeDetail = workRequest.addNewWorkTypeDetail();
		workTypeDetail.setOpenNextPiled(false);
		
//		System.out.println("WorkItem,id="+workItem2Open.getId()+";version="+workItem2Open.getVersion());
		return workRequest;
	}
	
	private WorkRequest getWorkRequestS(long workItemId, long workItemVersion, LoginInfo login, String uid, String version){
		WorkRequest workRequest = WorkRequest.Factory.newInstance();
		com.tibco.n2.wp.api.base.WorkItem workItem2Open = workRequest.addNewWorkItem();
		workItem2Open.setId(workItemId);
		workItem2Open.setVersion(workItemVersion);
		workRequest.setResourceId(login.getGuid());
		WorkTypeDetail workTypeDetail = workRequest.addNewWorkTypeDetail();
		workTypeDetail.setOpenNextPiled(false);
		workTypeDetail.setUid(uid);
		workTypeDetail.setVersion(version);
		
		System.out.println("WorkItem,id="+workItem2Open.getId()+";version="+workItem2Open.getVersion());
		return workRequest;
	}
}