/**
 * 
 */
package com.tibco.emea.amxbpm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;

import com.tibco.bpm.de.api.base.XmlResourceDetail;
import com.tibco.bpm.service.connector.ServiceConnector;
import com.tibco.bpm.service.connector.ServiceConnectorFactory;
import com.tibco.n2.brm.api.GetWorkListItemsResponseDocument.GetWorkListItemsResponse;
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
import com.tibco.n2.de.services.DirectoryEngineFault;
import com.tibco.n2.de.services.InternalServiceFault;
import com.tibco.n2.de.services.InvalidServiceRequestFault;
import com.tibco.n2.process.management.api.BasicProcessTemplate;
import com.tibco.n2.process.management.api.OperationInfo;
import com.tibco.n2.process.management.api.ProcessInstance;
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
/** .adapter.binding.WorkListService
 * @author Marcin Rzedzicki
 * 
 */
public class AmxBpmClient {

	Configuration.Protocol protocol = Configuration.Protocol.HTTP;
	String host = "localhost";
	int port = 8080;
	String passwd = "alamakota";
	UserCredentialsConnectionFactoryAdapter adapter = null;
	String username = "tibco-admin";
	LoginInfo user = new LoginInfo(username, null);
	java.util.Map<BusinessForms, String> enumMap = new java.util.EnumMap<BusinessForms, String>(BusinessForms.class);
	
	SecurityHandler securityHandler = new DefaultSecurityHandler(username, "secret");

//	@Resource(name = "serviceConnectionFactory")
//	private ServiceConnectorFactory serviceConnectionFactory;
	
	ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler);
	
//	@Resource(name = "amxBpmPort")
//	private int amxBpmPort;

	
	public AmxBpmClient() {
//		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-amxbpm-tests.xml");
//		serviceConnectionFactory = context.getBean(ServiceConnectorFactory.class);
//		amxBpmPort = context.getBean(Integer.class);	
	}
	
	public LoginInfo getUserDetails(String name){
		
		DefaultSecurityHandler securityHandler1 =  new DefaultSecurityHandler("tibco-admin", "secret");
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler1);
		

		LoginInfo loginInfoUser = new LoginInfo(name, null);
		try {
			
			LookupUserResponse lookupUserResponse = serviceConnector.getEntityResolverService().lookupUser(name, null, null, true);
			if (lookupUserResponse.getDetailArray().length > 0) {
				loginInfoUser.setGuid(lookupUserResponse.getDetailArray(0).getGuid());
				String[] userGuids = {loginInfoUser.getGuid()};
				XmlResourceDetail userDetails= serviceConnector.getOrgResourceService().getResource(userGuids)[0];
				loginInfoUser.setPosition(userDetails.getPositionArray()[0].getLabel());
			}
		}catch (com.tibco.n2.de.services.SecurityFault e) {
			e.printStackTrace();
		} catch (InvalidServiceRequestFault e) {
			e.printStackTrace();
		} catch (InternalServiceFault e) {
			e.printStackTrace();
		} catch (DirectoryEngineFault e) {
			e.printStackTrace();
		}
		return loginInfoUser;
	}
	
	public AmxBpmProcess startProcess(LoginInfo login, String processName) {
		return startProcess(login, BusinessProcessNames.BzWbkTTY);
	}

	public AmxBpmProcess startProcess(LoginInfo login, BusinessProcessNames processName) {
		SecurityHandler securityHandler = new DefaultSecurityHandler(login.getUserName(), passwd);
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler);

		QualifiedProcessName process = QualifiedProcessName.Factory.newInstance();
		process.setProcessName(processName.toString());
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
					newProcess.setProcessId(processId);
					newProcess.setState(AmxBpmProcessState.ACTIVE);
				}
			}
		} catch (IllegalArgumentFault e) {
			e.printStackTrace();
		} catch (OperationFailedFault e) {
			e.printStackTrace();
		}
		return newProcess;
	}
	
	public ProcessInstance getProcessStatus(AmxBpmProcess myProcess){
		SecurityHandler securityHandler = new DefaultSecurityHandler("tibco-admin", "secret");
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler);
		ProcessInstance process = ProcessInstance.Factory.newInstance();
		
		try {
			process = serviceConnector.getProcessManagerService().getProcessInstanceStatus(myProcess.getProcessId());
		} catch (IllegalArgumentFault e) {
			process.setState(AmxBpmProcessState.COMPLETED.toString());
		} catch (OperationFailedFault e) {
			e.printStackTrace();
		}
		return process;
	}

	public int getUserWorkItemsCount(LoginInfo login) {
		DefaultSecurityHandler securityHandler1 =  new DefaultSecurityHandler("tibco-admin", "secret");
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler1);
		
		int startPos = 0;
		int numberOfItems = 10;
		int count = 0;

		XmlModelEntityId entityId = buildEntityId(login.getUserName(), login.getGuid());
		OrderFilterCriteria oc = null;

		try {
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc, entityId, startPos, numberOfItems);
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
	public int getUserWorkItemsCount(AmxBpmProcess process, LoginInfo user, String workItemName) {
		SecurityHandler securityHandler = new DefaultSecurityHandler(user.getUserName(), passwd);
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler);
		
		int startPos = 0;
		int numberOfItems = 10;
		int count = 0;

		XmlModelEntityId entityId = buildEntityId(user.getUserName(), user.getGuid());
		OrderFilterCriteria oc = buildOrderCriteria(process, workItemName);

		try {
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc, entityId, startPos, numberOfItems);
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
	
	public BaseWorkRequest getWorkItemFromProcess(AmxBpmProcess process, LoginInfo user) {
		DefaultSecurityHandler securityHandler1 =  new DefaultSecurityHandler(user.getUserName(), passwd);
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler1);
		
		int startPos = 0;
		int numberOfItems = 10;
		int count = 0;
		BaseWorkRequest workRequest = null;

		XmlModelEntityId entityId = buildEntityId(user.getUserName(), user.getGuid());
		OrderFilterCriteria oc = buildOrderCriteria(process);

		try {
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc, entityId, startPos, numberOfItems);
//			System.out.println("size=" +  items.getWorkItemsArray().length);
			for (WorkItem myWorkItem : items.getWorkItemsArray()) {
				workRequest = getWorkRequest(myWorkItem.getId().getId(), myWorkItem.getId().getVersion(), process.getOwner());
//				System.out.println("1 Są taski na "+ user.toString()+";name="+myWorkItem.getHeader().getName()+",id="+myWorkItem.getId().getId()+";version="+myWorkItem.getId().getVersion()); //+;process="+myWorkItem.getAttributes().getAttribute14()
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
	
	public WorkResponse openWorkItem(LoginInfo login, long workItemId, long workItemVersion) throws InstantiationException, IllegalAccessException {
		DefaultSecurityHandler securityHandler1 =  new DefaultSecurityHandler(login.getUserName(), passwd);
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler1);
	
		BaseWorkRequest workRequest = null;
		WorkResponse openWorkItem = WorkResponse.Factory.newInstance();
//		String uid = "";
		try{
			workRequest = getWorkRequest(workItemId, workItemVersion, login);	
			openWorkItem = serviceConnector.getWorkPresentationService().openWorkItem(workRequest);
//			uid = openWorkItem.getWorkTypeDetail().getUid();
//			System.out.println(openWorkItem.getPayloadModel().getSerializedPayload());
//			System.out.println(uid + " " + openWorkItem.getWorkTypeDetail().getVersion());
		} catch (InvalidWorkRequestFault e) {
			e.printStackTrace();
		} catch (WorkItemUnavailableFault e) {
			e.printStackTrace();
		} catch (com.tibco.n2.wp.services.InternalServiceFault e) {
			e.printStackTrace();
		}
		return openWorkItem;
	}

	public void saveOpenWorkItem(LoginInfo login, String formName, long workItemId, long workItemVersion, String uid, String uidVersion, String prcId) {
		DefaultSecurityHandler securityHandler1 =  new DefaultSecurityHandler(login.getUserName(), passwd);
		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler1);
		
		WorkRequest request = getWorkRequestS(workItemId, workItemVersion+1, login, uid, uidVersion);
		request.setResourceId(login.getGuid());
		DataPayload payload = request.addNewPayloadDetails();
		payload.setPayloadMode(PayloadModeType.JSON);
		String myJsonPayload = "";
		FileInputStream inputStream = null;
		prepareMap();
		String file = enumMap.get(BusinessForms.valueOf(formName));
		try {
			inputStream = new FileInputStream(file);
			myJsonPayload = IOUtils.toString(inputStream);
			myJsonPayload = myJsonPayload.replaceAll("myApplicationId", prcId);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		       try {
				inputStream.close();
			} catch (IOException e) {
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
			e.printStackTrace();
		} catch (InvalidWorkRequestFault e) {
			e.printStackTrace();
		} catch (com.tibco.n2.wp.services.InternalServiceFault e) {
			e.printStackTrace();
		} catch (DataOutOfSyncFault e) {
			e.printStackTrace();
		} catch (ChainedTimeOutFault e) {
			e.printStackTrace();
		}
	}
	
	
	private XmlModelEntity buildEntityId(String userName, String guid) {
		XmlModelEntity entityId = XmlModelEntity.Factory.newInstance();
		entityId.setGuid(guid);
		entityId.setName(userName);
		entityId.setEntityType(OrganisationalEntityType.RESOURCE);
		entityId.setModelVersion(-1);
		return entityId;
	}
	
	private OrderFilterCriteria buildOrderCriteria(AmxBpmProcess process) {
		OrderFilterCriteria orderCriteria = OrderFilterCriteria.Factory.newInstance();
		orderCriteria.setFilter("attribute14='" + process.getProcessId()+"' AND name='"+ process.getCurrentTaskName() +"'");
		return orderCriteria;
	}
	
	private OrderFilterCriteria buildOrderCriteria(AmxBpmProcess process, String workItemName) {
		OrderFilterCriteria orderCriteria = OrderFilterCriteria.Factory.newInstance();
		orderCriteria.setFilter("attribute14='" + process.getProcessId()+"' AND name='"+ workItemName +"'");
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
		return workRequest;
	}
	
	private void prepareMap(){
		enumMap.put(BusinessForms.PKPWybrKlienta1, "src/test/resources/json/BzWbkTTY/WybrKlientaIGrupy/MyWybrKlientaiGrupy.data.json");
		enumMap.put(BusinessForms.PKPWybrKlienta2, "src/test/resources/json/BzWbkTTY/WybrKlientaIGrupy/MyWybrKlientaiGrupy2.data.json");
		enumMap.put(BusinessForms.PKPKolejkaPrzedRones1, "src/test/resources/json/BzWbkTTY/KolejkaprzedRONES/KolejkaprzedRONES.data.json");
		enumMap.put(BusinessForms.PKPKolejkaPrzedRones2, "src/test/resources/json/BzWbkTTY/KolejkaprzedRONES/KolejkaprzedRONES.data.json");
	}
	
	
}