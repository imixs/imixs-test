package org.imixs.example;

import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.engine.ModelPluginMock;
import org.imixs.workflow.engine.WorkflowMockEnvironment;
import org.imixs.workflow.exceptions.AccessDeniedException;
import org.imixs.workflow.exceptions.ModelException;
import org.imixs.workflow.exceptions.PluginException;
import org.imixs.workflow.exceptions.ProcessingErrorException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junit.framework.Assert;

/**
 * Manueller Test einzelner Business Rules
 * 
 * Modelldateien werden vom Parent Project unter /src/workflow/ gelesen
 * 
 * @author rsoika
 */
public class TestBPMN {
	WorkflowMockEnvironment workflowMockEnvironment;

	final static String MODEL_PATH = "/ticket.bpmn";
	final static String MODEL_VERSION = "ticket-workflow-1.0";

	ItemCollection workitem = null;

	@Before
	public void setup() throws PluginException, ModelException {

		// initialize @Mock annotations....
		MockitoAnnotations.initMocks(this);

		workflowMockEnvironment = new WorkflowMockEnvironment();
		workflowMockEnvironment.setModelPath(MODEL_PATH);
		workflowMockEnvironment.setup();

		Assert.assertNotNull(workflowMockEnvironment.getModel());

		// mock session context of plugin
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("manfred");

		Logger.getLogger("org.imixs.workflow.*").setLevel(Level.FINEST);
		// skip mail plugin
		try {
			workflowMockEnvironment.getModelService().addModel(new ModelPluginMock(workflowMockEnvironment.getModel(),
					"org.imixs.workflow.engine.plugins.OwnerPlugin", "org.imixs.workflow.engine.plugins.AccessPlugin",
					"org.imixs.workflow.engine.plugins.HistoryPlugin",
					"org.imixs.workflow.engine.plugins.ApplicationPlugin",
					"org.imixs.workflow.engine.plugins.RulePlugin"));
		} catch (ModelException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Simple Test
	 */
	@Test
	public void testSimple() {

		workitem = new ItemCollection();
		workitem.model(MODEL_VERSION).task(1000).event(10);
		workitem.replaceItemValue("_subject", "Hello World");

		try {
			workitem = workflowMockEnvironment.getWorkflowService().processWorkItem(workitem);

			Assert.assertNotNull(workitem);
			Assert.assertEquals(1100, workitem.getTaskID());
			Assert.assertEquals("manfred", workitem.getItemValue("namowner", String.class));

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}
}