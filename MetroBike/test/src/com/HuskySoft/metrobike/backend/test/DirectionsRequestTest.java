/**
 * 
 */
package com.HuskySoft.metrobike.backend.test;

import junit.framework.TestCase;

import com.HuskySoft.metrobike.backend.DirectionsRequest;

/**
 * @author dutchscout
 *
 */
public class DirectionsRequestTest extends TestCase {

	/**
	 * @param name
	 */
	public DirectionsRequestTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test_simple_constructor(){
		DirectionsRequest myRequest = new DirectionsRequest();
		assertNotNull(myRequest);
	}
	
	public void test_simple_dummy_test_fail(){
	    assertNotNull("\n\n*** THIS DUMMY TEST SHOULD FAIL! ***\n\n", null);
	}

}
