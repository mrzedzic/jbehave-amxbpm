/**
 * 
 */
package com.tibco.emea.amxbpm;

import com.tibco.bpm.service.connector.ServiceConnector;
import com.tibco.bpm.service.connector.ServiceConnectorFactory;
import com.tibco.n2.brm.api.GetWorkListItemsResponseDocument.GetWorkListItemsResponse;
import com.tibco.n2.brm.api.OrderFilterCriteria;
import com.tibco.n2.brm.api.WorkItem;
import com.tibco.n2.brm.api.WorkItemBody;
import com.tibco.n2.brm.services.InvalidEntityFault;
import com.tibco.n2.brm.services.SecurityFault;
import com.tibco.n2.brm.services.WorkItemFilterFault;
import com.tibco.n2.brm.services.WorkItemOrderFault;
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

/**
 * @author mrzedzic
 * 
 */
public class AmxBpmClient {

	Configuration.Protocol protocol = Configuration.Protocol.HTTP;
	String host = "localhost";
	int port = 8080;
	String username = "Pawel Kukla";
	SecurityHandler securityHandler = new DefaultSecurityHandler(username, "alamakota");
	ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler);

	public AmxBpmClient() {
		try {
			String guid = null;
			LookupUserResponse lookupUserResponse = serviceConnector.getEntityResolverService().lookupUser(username, null, null, true);
			if (lookupUserResponse.getDetailArray().length > 0) {
				guid = lookupUserResponse.getDetailArray(0).getGuid();
			} else {

			}
			System.out.println("marce = " + guid);
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

	public AmxBpmProcess startProcess(String guid, String processName) {
		QualifiedProcessName process = QualifiedProcessName.Factory.newInstance();
		process.setProcessName(processName);
//		AmxBpmProcess newProces = AmxBpmProcess.
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
					System.out.println(processId);
				}
			}
		} catch (IllegalArgumentFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationFailedFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return
	}

	public void getWorkItems(String guid) {
		GetWorkListItemsResponse response = null;
		/** set the start position of the worklist */
		int startPos = 0;
		int numberOfItems = 10;

		XmlModelEntityId entityId = buildEntityId(username, guid);
		OrderFilterCriteria oc = null;

		try {
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc, entityId, startPos, numberOfItems);

			System.out.println("items.sizeOfWorkItemsArray()="
					+ items.sizeOfWorkItemsArray());
			for (WorkItem item : items.getWorkItemsArray()) {
				// System.out.println(item.get);
				WorkItemBody body = item.getBody();
				System.out.println("item.getHeader().getDescription()="
						+ item.getHeader().getDescription());
				System.out.println("item.getHeader().getName()="
						+ item.getHeader().getStartDate());
			}

		} catch (InvalidEntityFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (com.tibco.n2.brm.services.InternalServiceFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WorkItemOrderFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WorkItemFilterFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityFault e) {
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
}
