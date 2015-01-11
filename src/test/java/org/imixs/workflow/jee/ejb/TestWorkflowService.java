package org.imixs.workflow.jee.ejb;

import javax.naming.InitialContext;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.exceptions.AccessDeniedException;
import org.imixs.workflow.exceptions.PluginException;
import org.imixs.workflow.exceptions.ProcessingErrorException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.sun.appserv.security.ProgrammaticLogin;

/**
 * Test class for WorkflowService
 * 
 * @author rsoika
 * 
 */
public class TestWorkflowService extends AbstractTestService {

	@Test
	@Category(org.imixs.workflow.jee.ejb.EntityServiceRemote.class)
	public void testService() {

		Assert.assertNotNull(workflowService);

	}

	@Test
	@Category(org.imixs.workflow.jee.ejb.EntityServiceRemote.class)
	public void testProcessWorkitem() {

		ItemCollection itemcol = new ItemCollection();
		itemcol.replaceItemValue("txtName", "Anna");
		itemcol.replaceItemValue("$processid", 10);
		itemcol.replaceItemValue("$activityid", 10);

		try {
			itemcol = workflowService.processWorkItem(itemcol);
			Assert.assertEquals("1.0.0",
					itemcol.getItemValueString("$ModelVersion"));
		} catch (PluginException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (AccessDeniedException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (ProcessingErrorException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

}
