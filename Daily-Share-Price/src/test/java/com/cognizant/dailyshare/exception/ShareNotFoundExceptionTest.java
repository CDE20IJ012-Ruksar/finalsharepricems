package com.cognizant.dailyshare.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


/**
 * 
 * @author Ruksar, Revathi, Rameswara, Prachi
 *
 */
class ShareNotFoundExceptionTest {

	@Test
	void testShareNotFoundException() {
		ShareNotFoundException shareNotFoundException=new ShareNotFoundException("Error occured");
		assertEquals("Error occured",shareNotFoundException.getMessage());
	}

}
