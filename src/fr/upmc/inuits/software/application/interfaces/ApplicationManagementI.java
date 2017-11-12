package fr.upmc.inuits.software.application.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.components.pre.reflection.ports.ReflectionOutboundPort;
import fr.upmc.inuits.software.requestdispatcher.RequestDispatcher;

public interface ApplicationManagementI 
	extends OfferedI, RequiredI {

	public void doConnectionWithDispatcherForSubmission(String dispatcherRequestSubmissionInboundPortUri) 
			throws Exception;
	
	public void doDynamicConnectionWithDispatcherForSubmission(String dispatcherRequestSubmissionInboundPortUri) 
			throws Exception;
	
	public void doConnectionWithDispatcherForNotification(ReflectionOutboundPort ropForRequestDispatcher, 
			String dispatcherRequestNotificationOutboundPortUri) throws Exception;
	
	public void doDynamicConnectionWithDispatcherForNotification(ReflectionOutboundPort ropForRequestDispatcher, 
			String dispatcherRequestNotificationOutboundPortUri) throws Exception;
}
