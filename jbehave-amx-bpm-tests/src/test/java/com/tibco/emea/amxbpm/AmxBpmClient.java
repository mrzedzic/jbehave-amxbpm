/**
 * 
 */
package com.tibco.emea.amxbpm;

import com.tibco.bpm.service.connector.ServiceConnector;
import com.tibco.bpm.service.connector.ServiceConnectorFactory;
import com.tibco.n2.brm.api.GetWorkListItemsResponseDocument.GetWorkListItemsResponse;
import com.tibco.n2.brm.api.OrderFilterCriteria;
import com.tibco.n2.brm.api.WorkItem;
import com.tibco.n2.brm.services.InvalidEntityFault;
import com.tibco.n2.brm.services.SecurityFault;
import com.tibco.n2.brm.services.WorkItemFilterFault;
import com.tibco.n2.brm.services.WorkItemOrderFault;
import com.tibco.n2.common.organisation.api.XmlModelEntityId;
import com.tibco.n2.de.api.resolver.LookupUserResponseDocument.LookupUserResponse;
import com.tibco.n2.de.services.InternalServiceFault;
import com.tibco.n2.de.services.InvalidServiceRequestFault;
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
	String username = "tibco-admin";
	SecurityHandler securityHandler = new DefaultSecurityHandler(username, "secret");
	ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(protocol, host, port, securityHandler);
	
	

	public AmxBpmClient() {
		try {
			String guid = null;
			LookupUserResponse lookupUserResponse = serviceConnector
					.getEntityResolverService().lookupUser(username, null,
							null, true);
			if (lookupUserResponse.getDetailArray().length > 0) {
				guid = lookupUserResponse.getDetailArray(0).getGuid();
			} else {
				
			}
			System.out.println("marce = "+guid);
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
	
	public void getWorkItems(){
		GetWorkListItemsResponse response = null;
		/**set the start position of the worklist*/
		int startPos = 0;
		int numberOfItems = 10;
		
		XmlModelEntityId entityId = buildEntityId("tibco-admin", "tibco-admin");

		/**you can pass the order filter criteria for setting the order and filter string
		 * but order filter criteria is optional so currently passing null */
		OrderFilterCriteria oc = null;

		try
		{
			GetWorkListItemsResponse items = serviceConnector.getWorkListService().getWorkListItems(oc,
					entityId, startPos, numberOfItems);
			
			for (WorkItem item : items.getWorkItemsArray()){
				System.out.println(item.getBody().toString());
			}

		}
		catch (InvalidEntityFault e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (com.tibco.n2.brm.services.InternalServiceFault e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (WorkItemOrderFault e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (WorkItemFilterFault e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SecurityFault e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private XmlModelEntityId buildEntityId(String userName, String guid)
	{
		/**Cretate an instance of entity Id*/
		XmlModelEntityId entityId = XmlModelEntityId.Factory.newInstance();
		/**set the user GUID*/
		entityId.setGuid(guid);
		/**set the user name*/
		//entityId.set
		/**set the entity type as RESOURCE*/
		//entityId.set .setEntityType(OrganisationalEntityType.RESOURCE);
		/**set the version of organisation*/
		//((XmlOrgModelVersion) entityId).setModelVersion(-1);
		return entityId;
	}
}
