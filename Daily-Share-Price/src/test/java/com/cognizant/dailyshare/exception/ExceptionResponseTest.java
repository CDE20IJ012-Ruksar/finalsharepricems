package com.cognizant.dailyshare.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;


/**
 * 
 * @author Ruksar, Revathi, Rameswara, Prachi
 *
 */
class ExceptionResponseTest {
	
	ExceptionResponse exceptionResponse=new ExceptionResponse();

	
	@Test
	void testGetTimeStamp() {
		exceptionResponse.setTimeStamp(new Date(2021, 3, 21));
		assertEquals(new Date(2021, 3, 21),exceptionResponse.getTimeStamp());
	}

	@Test
	void testGetDetails() {
		exceptionResponse.setDetails("Error");
		assertEquals("Error",exceptionResponse.getDetails());
	}

	@Test
	void testGetMessage() {
		exceptionResponse.setMessage("Error occured");
		assertEquals("Error occured",exceptionResponse.getMessage());
	}

	

}
