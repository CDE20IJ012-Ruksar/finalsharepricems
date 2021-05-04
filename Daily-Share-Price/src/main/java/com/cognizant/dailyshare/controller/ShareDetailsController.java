package com.cognizant.dailyshare.controller;
/**
 * @author Ruksar, Revathi, Rameswara, Prachi
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.cognizant.dailyshare.exception.ShareNotFoundException;
import com.cognizant.dailyshare.model.ShareDetails;
import com.cognizant.dailyshare.service.ShareDetailsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ShareDetailsController {
	
	@Autowired
	private ShareDetailsService service;
	
	/**
	 * This method returns all shares list
	 * @param token
	 * @return
	 */
	@GetMapping("/getAllShares")
	public List<ShareDetails> getAllShares(@RequestHeader("Authorization") String token){
		log.info("START :: Method :: getAllShares() ::");
		if(service.isSessionValid(token)) {
			log.debug("Started fetching all shares after authorization");
			return service.getAllShares();
		}
		return null;
		
	}
	
	/**
	 * This method returns share detail for the share name passed in argument
	 * @param token
	 * @param shareName
	 * @return
	 * @throws ShareNotFoundException 
	 */
	@GetMapping("/getShare/shareName/{shareName}")
	public ShareDetails getShareByShareName(@RequestHeader("Authorization") String token,@PathVariable String shareName) throws ShareNotFoundException {
		log.info("START :: Method :: getShareByShareName() ::");
		if(service.isSessionValid(token)) {
			log.debug("Started fetching share with particular share name after authorization");
			return service.getShareByShareName(shareName);
		}
		//if(shareName.equals(" "))
			//throw new ShareNotFoundException("Enter valid share id");
		
		return null;
	}
	
	/**
	 * This method returns share detail for the share id passed in argument
	 * @param token
	 * @param shareId
	 * @return
	 * @throws ShareNotFoundException
	 */  
	@GetMapping("/getShare/shareId/{shareId}")
	public ShareDetails getShareByShareId(@RequestHeader("Authorization") String token,@PathVariable String shareId) throws ShareNotFoundException {
		log.info("START :: Method :: getShareByShareId() ::");
		if(service.isSessionValid(token)) {
			log.debug("Started fetching share with particular share id after authorization");
			
			//if(shareId.equals(" "))
				//throw new ShareNotFoundException("Enter valid share id");
		 	return service.getShareByShareId(shareId);
		 }
		return null;
	}
	/**
	 * This method returns list of stock values
	 * @param token
	 * @param shareId
	 * @return
	 */
	@GetMapping("/getShareValuesByShareIdList/shareIdList/{shareId}")
	public List<Double> getShareValuesByShareIdList(@RequestHeader("Authorization") String token,@PathVariable(value="shareId") List<String> shareIdList){
		log.info("START :: Method :: getShareValuesByShareIdList() ::");
		if(service.isSessionValid(token)) {
			log.debug("Started fetching list of stock values after authorization");
			return service.getShareValuesByShareIdList(shareIdList);
		}
		return null;
	}
	
	
	
}
