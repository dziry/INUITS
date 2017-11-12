package fr.upmc.inuits.software.application.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.components.pre.reflection.ports.ReflectionOutboundPort;
import fr.upmc.inuits.software.application.Application;
import fr.upmc.inuits.software.application.interfaces.ApplicationManagementI;

public class ApplicationManagementInboundPort
	extends AbstractInboundPort
	implements ApplicationManagementI {

	private static final long serialVersionUID = 1L;

	public ApplicationManagementInboundPort(ComponentI owner) throws Exception {
		
		super(ApplicationManagementI.class, owner);

		assert owner != null && owner instanceof Application;
	}

	public ApplicationManagementInboundPort(String uri, ComponentI owner) throws Exception {
		
		super(uri, ApplicationManagementI.class, owner);

		assert	owner != null && owner instanceof Application;
	}
	
	@Override
	public void doConnectionWithDispatcherForSubmission(String dispatcherRequestSubmissionInboundPortUri) 
			throws Exception {
		
		final Application app = (Application) this.owner;
		
		this.owner.handleRequestAsync(
				new ComponentI.ComponentService<Void>() {
					@Override
					public Void call() throws Exception {
						app.doConnectionWithDispatcherForSubmission(dispatcherRequestSubmissionInboundPortUri);
						return null;
					}
				});		
	}
	
	@Override
	public void doDynamicConnectionWithDispatcherForSubmission(String dispatcherRequestSubmissionInboundPortUri)
			throws Exception {
		
		final Application app = (Application) this.owner;
		
		this.owner.handleRequestAsync(
				new ComponentI.ComponentService<Void>() {
					@Override
					public Void call() throws Exception {
						app.doDynamicConnectionWithDispatcherForSubmission(dispatcherRequestSubmissionInboundPortUri);
						return null;
					}
				});
	}

	@Override
	public void doConnectionWithDispatcherForNotification(ReflectionOutboundPort ropForRequestDispatcher,
			String dispatcherRequestNotificationOutboundPortUri) throws Exception {
		
		final Application app = (Application) this.owner;
		
		this.owner.handleRequestAsync(
				new ComponentI.ComponentService<Void>() {
					@Override
					public Void call() throws Exception {
						app.doConnectionWithDispatcherForNotification(ropForRequestDispatcher, 
								dispatcherRequestNotificationOutboundPortUri);
						return null;
					}
				});
	}

	@Override
	public void doDynamicConnectionWithDispatcherForNotification(ReflectionOutboundPort ropForRequestDispatcher,
			String dispatcherRequestNotificationOutboundPortUri) throws Exception {
		
		final Application app = (Application) this.owner;
		
		this.owner.handleRequestAsync(
				new ComponentI.ComponentService<Void>() {
					@Override
					public Void call() throws Exception {
						app.doDynamicConnectionWithDispatcherForNotification(ropForRequestDispatcher, 
								dispatcherRequestNotificationOutboundPortUri);
						return null;
					}
				});
	}	
}
