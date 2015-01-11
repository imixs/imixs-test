package org.imixs.workflow.jee.jaxrs;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.services.rest.RestClient;
import org.imixs.workflow.xml.XMLItemCollection;
import org.imixs.workflow.xml.XMLItemCollectionAdapter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for imixs-jax-rs workflow rest api
 * 
 * The test class checks post scenarios
 * 
 * @author rsoika
 * 
 */
public class TestRestAPIWorkflow {

	static String USERID = "Manfred";
	static String PASSWORD = "manfred";

	@Test
	public void testPostXmlWorkitem() {

		RestClient restClient = new RestClient();

		restClient.setCredentials(USERID, PASSWORD);

		String uri = "http://localhost:8080/workflow/rest/workflow/workitem";

		ItemCollection workitem = new ItemCollection();
		workitem.replaceItemValue("txtName", "workitem test");
		workitem.replaceItemValue("$ProcessID", 10);
		workitem.replaceItemValue("$ActivityID", 10);

		XMLItemCollection xmlReport;
		try {
			xmlReport = XMLItemCollectionAdapter.putItemCollection(workitem);
			int httpResult = restClient.postEntity(uri, xmlReport);

			// expected result 200
			Assert.assertEquals(200, httpResult);
		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();
		}

	}
	
	
	
	/**
	 * <code>
	 * 
			{ "item" : 
			  { "name" : { "$" : "Bill"},
			    "value" : { "$" : "Burke" }
			  }
			}
			
			
			
			{ "item" : 
			  { "name" : { "$" : "Bill"},
			    "value" : [ { "$" : "Burke" } ]
			  }
			}
     * </code>
	 */
	@Test
	public void testPostJsonWorkitem() {

		RestClient restClient = new RestClient();

		restClient.setCredentials(USERID, PASSWORD);

		String uri = "http://localhost:8080/workflow/rest/workflow/workitem.json";

		// create a json test string
		String json = "{\"item\":["
				+ "	{\"name\":\"$processid\",\"value\":{\"@type\":\"xs:int\",\"$\":\"10\"}},"
				+ "	{\"name\":\"$activityid\",\"value\":{\"@type\":\"xs:int\",\"$\":\"10\"}},"
				+ "	{\"name\":\"txtList\",\"value\":[{\"@type\":\"xs:string\",\"$\":\"A\"},{\"@type\":\"xs:string\",\"$\":\"B\"}]},"
				+ "	{\"name\":\"txtname\",\"value\":{\"@type\":\"xs:string\",\"$\":\"workitem json test\"}}"
				+ "]}";
		
		
		
		
		
		
		// http://www.jsonschema.net/
		try {
			int httpResult = restClient.postJsonEntity(uri, json);

			String sContent=restClient.getContent();
			// expected result 200
			Assert.assertEquals(200, httpResult);
			
			Assert.assertTrue(sContent.indexOf("$uniqueid")>-1);
		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();
		}

	}
	
	
	
	
	/**
	 * This test simulats a invalid processid for the current model
	 */
	@Test
	public void testPostWrongModelJsonWorkitem() {

		RestClient restClient = new RestClient();

		restClient.setCredentials(USERID, PASSWORD);

		String uri = "http://localhost:8080/workflow/rest/workflow/workitem.json";

		// create a json test string
		String json = "{\"item\":["
				+ "	{\"name\":\"$processid\",\"value\":{\"@type\":\"xs:int\",\"$\":\"11\"}},"
				+ "	{\"name\":\"$activityid\",\"value\":{\"@type\":\"xs:int\",\"$\":\"10\"}},"
				+ "	{\"name\":\"txtList\",\"value\":[{\"@type\":\"xs:string\",\"$\":\"A\"},{\"@type\":\"xs:string\",\"$\":\"B\"}]},"
				+ "	{\"name\":\"txtname\",\"value\":{\"@type\":\"xs:string\",\"$\":\"workitem json test\"}}"
				+ "]}";
		
		
		// http://www.jsonschema.net/
		try {
			int httpResult = restClient.postJsonEntity(uri, json);

			String sContent=restClient.getContent();
			// expected result 200
			Assert.assertEquals(200, httpResult);
			
			Assert.assertTrue(sContent.indexOf("$uniqueid")>-1);
		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();
		}

	}
	
	
	
	/**
	 * This test simulats a invalid json structure 
	 */
	@Test
	public void testPostInvalidJson() {

		RestClient restClient = new RestClient();

		restClient.setCredentials(USERID, PASSWORD);

		String uri = "http://localhost:8080/workflow/rest/workflow/workitem.json";

		// create a json test string
		String json = "{\"item\":["
				+ "	{\"name\":\"$processid\",\"value\":{\"@type\":\"xs:int\",\"$\":\"10\"}},"
				+ "	\"name\":\"$activityid\",\"value\":\"@type\":\"xs:int\",\"$\":\"10\"}},"
				+ "	{\"name\":\"txtList\",\"value\":[{\"@type\":\"xs:string\",\"$\":\"A\"},{\"@type\":\"xs:string\",\"$\":\"B\"}]},"
				+ "	{\"name\":\"txtname\",\"value\":{\"@type\":\"xs:string\",\"$\":\"workitem json test\"}}"
				+ "]}";
		
		try {
			int httpResult = restClient.postJsonEntity(uri, json);

			Assert.assertEquals(406, httpResult);
			
		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();
		}

	}
}
