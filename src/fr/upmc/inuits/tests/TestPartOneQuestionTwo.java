package fr.upmc.inuits.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.connectors.DataConnector;
import fr.upmc.components.cvm.AbstractCVM;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.hardware.computers.Computer;
import fr.upmc.datacenter.hardware.computers.connectors.ComputerServicesConnector;
import fr.upmc.datacenter.hardware.processors.Processor;
import fr.upmc.datacenter.software.connectors.RequestNotificationConnector;
import fr.upmc.datacenter.software.connectors.RequestSubmissionConnector;
import fr.upmc.datacenterclient.requestgenerator.RequestGenerator;
import fr.upmc.datacenterclient.requestgenerator.connectors.RequestGeneratorManagementConnector;
import fr.upmc.datacenterclient.requestgenerator.ports.RequestGeneratorManagementOutboundPort;
import fr.upmc.inuits.software.admissioncontroller.AdmissionController;
import fr.upmc.inuits.software.application.Application;
import fr.upmc.inuits.software.application.connectors.ApplicationNotificationConnector;
import fr.upmc.inuits.software.application.connectors.ApplicationSubmissionConnector;

public class TestPartOneQuestionTwo extends AbstractCVM {

	public static final String C_SERVICES_IN_PORT_URI = "cs-ip";
	public static final String C_STATIC_STATE_DATA_IN_PORT_URI = "cssd-ip";
	public static final String C_DYNAMIC_STATE_DATA_IN_PORT_URI = "cdsd-ip";
	
	public static final String AC_SERVICES_OUT_PORT_URI = "acs-op";
	public static final String AC_STATIC_STATE_DATA_OUT_PORT_URI = "acssd-op";
	public static final String AC_DYNAMIC_STATE_DATA_OUT_PORT_URI = "acdsd-op";
	public static final String AC_APPLICATION_SUBMISSION_IN_PORT_URI = "acas-ip";
	public static final String AC_APPLICATION_NOTIFICATION_OUT_PORT_URI = "acan-op";
	public static final String AC_REQUEST_SUBMISSION_IN_PORT_URI = "acrs-ip";
	public static final String AC_REQUEST_NOTIFICATION_OUT_PORT_URI = "acrn-op";

	public static final String A_APPLICATION_SUBMISSION_OUT_PORT_URI = "aas-op";
	public static final String A_APPLICATION_NOTIFICATION_IN_PORT_URI = "aan-ip";
	
	public static final String RG_MANAGEMENT_IN_PORT_URI = "rgm-ip";
	public static final String RG_MANAGEMENT_OUT_PORT_URI = "rgm-op";
	public static final String RG_REQUEST_SUBMISSION_OUT_PORT_URI = "rgrs-op";
	public static final String RG_REQUEST_NOTIFICATION_IN_PORT_URI = "rgrn-ip";
	
	//protected ComputerServicesOutboundPort csOutPort;
	protected AdmissionController admissionController;
	protected Application application;
	protected RequestGenerator requestGenerator;
	protected RequestGeneratorManagementOutboundPort rgmOutPort;

	public TestPartOneQuestionTwo() throws Exception {
		super();
	}

	@Override
	public void deploy() throws Exception {
		
		AbstractComponent.configureLogging(System.getProperty("user.home"), "log", 400, '|');
		Processor.DEBUG = true;
		// --------------------------------------------------------------------
		ArrayList<String> computersURI = new ArrayList<>();
		
		String computerURI = "computer0";		
		int numberOfProcessors = 2;
		int numberOfCores = 2;
		Set<Integer> admissibleFrequencies = new HashSet<Integer>();
		admissibleFrequencies.add(1500);
		admissibleFrequencies.add(3000);
		Map<Integer,Integer> processingPower = new HashMap<Integer,Integer>();
		processingPower.put(1500, 1500000);
		processingPower.put(3000, 3000000);
		
		computersURI.add(computerURI);
		
		Computer computer = new Computer(
				computerURI, 
				admissibleFrequencies, 
				processingPower, 
				1500, 
				1500, 
				numberOfProcessors, 
				numberOfCores, 
				C_SERVICES_IN_PORT_URI, 
				C_STATIC_STATE_DATA_IN_PORT_URI, 
				C_DYNAMIC_STATE_DATA_IN_PORT_URI);		
		
		this.addDeployedComponent(computer);				
		// --------------------------------------------------------------------
		this.admissionController = new AdmissionController(								
				computersURI,
				AC_SERVICES_OUT_PORT_URI,
				AC_STATIC_STATE_DATA_OUT_PORT_URI, 
				AC_DYNAMIC_STATE_DATA_OUT_PORT_URI,
				AC_APPLICATION_SUBMISSION_IN_PORT_URI,
				AC_APPLICATION_NOTIFICATION_OUT_PORT_URI,
				AC_REQUEST_SUBMISSION_IN_PORT_URI,
				AC_REQUEST_NOTIFICATION_OUT_PORT_URI);
		
		this.addDeployedComponent(this.admissionController);
		
		AdmissionController.DEBUG_LEVEL = 3;
		this.admissionController.toggleTracing();
		this.admissionController.toggleLogging();			
		
		this.admissionController.doPortConnection(				
				AC_SERVICES_OUT_PORT_URI,
				C_SERVICES_IN_PORT_URI,
				ComputerServicesConnector.class.getCanonicalName());
		
		this.admissionController.doPortConnection(
				AC_STATIC_STATE_DATA_OUT_PORT_URI,
				C_STATIC_STATE_DATA_IN_PORT_URI,
				DataConnector.class.getCanonicalName());

		this.admissionController.doPortConnection(
				AC_DYNAMIC_STATE_DATA_OUT_PORT_URI,
				C_DYNAMIC_STATE_DATA_IN_PORT_URI,
				ControlledDataConnector.class.getCanonicalName());			
		// --------------------------------------------------------------------
				this.application = new Application(				
						"app0",				
						A_APPLICATION_SUBMISSION_OUT_PORT_URI,
						A_APPLICATION_NOTIFICATION_IN_PORT_URI);
				
				this.addDeployedComponent(application);
			
				Application.DEBUG_LEVEL = 1;
				this.application.toggleTracing();
				this.application.toggleLogging();
			
				this.application.doPortConnection(
						A_APPLICATION_SUBMISSION_OUT_PORT_URI,
						AC_APPLICATION_SUBMISSION_IN_PORT_URI,
						ApplicationSubmissionConnector.class.getCanonicalName());
				
				this.admissionController.doPortConnection(				
						AC_APPLICATION_NOTIFICATION_OUT_PORT_URI,
						A_APPLICATION_NOTIFICATION_IN_PORT_URI,
						ApplicationNotificationConnector.class.getCanonicalName());
		// --------------------------------------------------------------------		
		this.requestGenerator = new RequestGenerator(				
				"rg0",
				500.0,
				6000000000L,
				RG_MANAGEMENT_IN_PORT_URI,
				RG_REQUEST_SUBMISSION_OUT_PORT_URI,
				RG_REQUEST_NOTIFICATION_IN_PORT_URI);
		
		this.addDeployedComponent(requestGenerator);
	
		RequestGenerator.DEBUG_LEVEL = 1;
		this.requestGenerator.toggleTracing();
		this.requestGenerator.toggleLogging();
	
		this.requestGenerator.doPortConnection(
				RG_REQUEST_SUBMISSION_OUT_PORT_URI,
				AC_REQUEST_SUBMISSION_IN_PORT_URI,
				RequestSubmissionConnector.class.getCanonicalName());
		
		this.admissionController.doPortConnection(				
				AC_REQUEST_NOTIFICATION_OUT_PORT_URI,
				RG_REQUEST_NOTIFICATION_IN_PORT_URI,
				RequestNotificationConnector.class.getCanonicalName());								
		// --------------------------------------------------------------------
		this.rgmOutPort = new RequestGeneratorManagementOutboundPort(				
				RG_MANAGEMENT_OUT_PORT_URI,
				new AbstractComponent(0, 0) {});
		
		this.rgmOutPort.publishPort();
		
		this.rgmOutPort.doConnection(
				RG_MANAGEMENT_IN_PORT_URI,
				RequestGeneratorManagementConnector.class.getCanonicalName());
		// --------------------------------------------------------------------
		super.deploy();
	}
	
	@Override
	public void start() throws Exception {
		
		super.start();
	}
	
	@Override
	public void shutdown() throws Exception {
				
		this.admissionController.doPortDisconnection(AC_SERVICES_OUT_PORT_URI);
		this.admissionController.doPortDisconnection(AC_STATIC_STATE_DATA_OUT_PORT_URI);
		this.admissionController.doPortDisconnection(AC_DYNAMIC_STATE_DATA_OUT_PORT_URI);
		this.admissionController.doPortDisconnection(AC_APPLICATION_NOTIFICATION_OUT_PORT_URI);
		this.admissionController.doPortDisconnection(AC_REQUEST_NOTIFICATION_OUT_PORT_URI);
		this.requestGenerator.doPortDisconnection(RG_REQUEST_SUBMISSION_OUT_PORT_URI);
		this.rgmOutPort.doDisconnection();

		// print logs on files, if activated
		/*this.applicationVM.printExecutionLogOnFile("applicationVM");
		this.requestDispatcher.printExecutionLogOnFile("requestDispatcher");
		this.requestGenerator.printExecutionLogOnFile("requestGenerator");*/
		
		super.shutdown();
	}
	
	public void testScenario() throws Exception {
		
		this.application.sendRequestForApplicationExecution(); //change it as service
		this.rgmOutPort.startGeneration();
		Thread.sleep(20000L);		
		this.rgmOutPort.stopGeneration();
	}
	
	public static void main(String[] args) {
		
		try {
			final TestPartOneQuestionTwo test = new TestPartOneQuestionTwo();
			test.deploy();
			
			System.out.println("starting...");
			test.start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						test.testScenario();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}).start();
			
			Thread.sleep(90000L);
			
			System.out.println("shutting down...");
			test.shutdown();
			
			System.out.println("ending...");
			System.exit(0);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
