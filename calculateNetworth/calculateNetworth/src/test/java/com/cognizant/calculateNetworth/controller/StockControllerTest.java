package com.cognizant.calculateNetworth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cognizant.calculateNetworth.controller.StocksContoller;
import com.cognizant.calculateNetworth.model.Asset;
import com.cognizant.calculateNetworth.model.AuthResponse;
import com.cognizant.calculateNetworth.model.MutualFundDetails;
import com.cognizant.calculateNetworth.model.StockDetails;
import com.cognizant.calculateNetworth.repository.AssetRepository;
import com.cognizant.calculateNetworth.service.AssetServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.junit4.SpringRunner;
@AutoConfigureMockMvc
@SpringBootTest

class StockControllerTest {
	@Autowired
	private StocksContoller controller;
	@Autowired
	MockMvc mockMvc;

	@Autowired
	private MockMvc mvc;
	
	@MockBean
    MutualFundDetailsFeignProxy mutualFundProxy;
	
	@MockBean
	AssetServiceImpl assetServiceImpl;
	
	@MockBean
	ShareDetailsFiegnProxy proxy;
	
	@MockBean
    AuthClient authClient;
	
	@MockBean
	private AssetRepository repository;
	
	List<MutualFundDetails> mutualFundsList=new ArrayList<MutualFundDetails>();
	
	List<Asset> assetList=new ArrayList<Asset>();
	
	List<StockDetails> shareslist=new ArrayList<StockDetails>();
		 @BeforeEach
		public void setup()  {
			Asset asset1=new Asset(1,"AMZ",101,"Share",10);
			Asset asset2=new Asset(2,"AXIS",101,"MF",10);
			Asset asset3=new Asset(3,"GGL",101,"Share",10);
			Asset asset4=new Asset(4,"DSP",101,"MF",10);
			assetList.add(asset1);
			assetList.add(asset2);
			assetList.add(asset3);
			assetList.add(asset4);
			MutualFundDetails mutualfund1=new MutualFundDetails("AXIS", "Axis Bluechip fund", 9800.0) ;
			MutualFundDetails mutualfund2=new MutualFundDetails("DSP", "DSP Midcap Fund", 6000.0) ;
			mutualFundsList.add(mutualfund1);
			mutualFundsList.add(mutualfund2);
		}
	@Test
	public void testGetAllAssets() throws Exception {
		when(assetServiceImpl.getAllAssetForPortfolio(101)).thenReturn(assetList);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/GetAllAssets/101").content(this.mapToJson(assetList))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("Authorization", "token")
				).andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllAssetsNegative() throws Exception {
		when(assetServiceImpl.getAllAssetForPortfolio(101)).thenReturn(assetList);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/GetAllAssets/101").content(this.mapToJson(assetList))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isBadRequest());
	}

	@Test
	public void testGetAllMutualFunds() throws Exception 
	{	
		when(mutualFundProxy.getAllMutualFunds("token")).thenReturn(mutualFundsList);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/mutualfunds").content(this.mapToJson(mutualFundsList))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("Authorization", "token")
				).andExpect(status().isOk());
	}
	

	@Test
	public void testGetStockName() throws Exception {
		StockDetails stockDetail=new StockDetails("AMZ","Amazon",2500.0);
		when(proxy.getShareByShareName("token","AMZ")).thenReturn(stockDetail);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/pershare/AMZ").content(this.mapToJson(stockDetail))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("Authorization", "token")
				).andExpect(status().isOk());
	}
    
	

	@Test
	public void testGetNetworth() throws Exception {
		double expected=0;
		List<String> stockAssetList = new ArrayList<>();
		List<String> mutualFundAssetList = new ArrayList<>();
		List<Double> stockValueList = new ArrayList<>();
		List<Double> mutualFundValueList = new ArrayList<>();
		assetList = assetServiceImpl.getAllAssetForPortfolio(101);
		stockAssetList.add("AMZ");
		mutualFundAssetList.add("AXIS");
		stockAssetList.add("GGL");
		mutualFundAssetList.add("DSP");
		stockValueList.add(2500.0);
		mutualFundValueList.add(2500.0);
		stockValueList.add(2000.0);
		mutualFundValueList.add(2000.0);
		expected = 10 * stockValueList.get(0)+ 10 * stockValueList.get(1) +10 * mutualFundValueList.get(0)+10 * mutualFundValueList.get(1);
		assertEquals(90,000.00,expected);
		when(proxy.getShareValuesByShareIdList("token",stockAssetList)).thenReturn(stockValueList);
		when(mutualFundProxy.getMutualFundByIdList("token",mutualFundAssetList)).thenReturn(mutualFundValueList);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/calculateNetworth/101")
				.header("Authorization", "token")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetNetworthEmptyMutaulFunds() throws Exception {
		double expected=0;
		List<String> stockAssetList = new ArrayList<>();
		List<String> mutualFundAssetList = new ArrayList<>();
		List<Double> stockValueList = new ArrayList<>();
		List<Double> mutualFundValueList = new ArrayList<>();
		assetList = assetServiceImpl.getAllAssetForPortfolio(101);
		stockAssetList.add("AMZ");
		stockAssetList.add("GGL");
		stockValueList.add(2500.0);
		stockValueList.add(2000.0);
		expected = 10 * stockValueList.get(0)+ 10 * stockValueList.get(1);
		assertEquals(45000.00,expected);
		when(proxy.getShareValuesByShareIdList("token",stockAssetList)).thenReturn(stockValueList);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/calculateNetworth/101")
				.header("Authorization", "token")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetNetworthEmptyShares() throws Exception {
		double expected=0;
		List<String> mutualFundAssetList = new ArrayList<>();
		List<Double> mutualFundValueList = new ArrayList<>();
		assetList = assetServiceImpl.getAllAssetForPortfolio(101);
		mutualFundAssetList.add("AXIS");
		mutualFundAssetList.add("DSP");
		mutualFundValueList.add(2500.0);
		mutualFundValueList.add(2000.0);
		expected = 10 * mutualFundValueList.get(0)+ 10 * mutualFundValueList.get(1);
		assertEquals(45000.00,expected);
		when(mutualFundProxy.getMutualFundByIdList("token",mutualFundAssetList)).thenReturn(mutualFundValueList);	
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/calculateNetworth/101")
				.header("Authorization", "token")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllStocks() throws Exception {
		List<Asset> stockList = new ArrayList<>();
		StockDetails stockDetails1=new StockDetails("AMZ","Amazon",2500.0);
		StockDetails stockDetails2=new StockDetails("GGL","Google",2000.0);
		shareslist.add(stockDetails1);
		shareslist.add(stockDetails2);
		when(proxy.getAllShares("token")).thenReturn(shareslist);
		stockList.add(new Asset(10, "AMZ", 1, "share", 10));
		stockList.add(new Asset(10, "GGL", 2, "share", 10));
		mockMvc.perform(
				MockMvcRequestBuilders.get("/NetWorth/shares").content(this.mapToJson(stockList))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("Authorization", "token")
				).andExpect(status().isOk());
	
	}
	
	
	
	/**
	 * Maps object into JSON string.Uses Jackson ObjectMapper	
	 */
	private String mapToJson(Object object) throws JsonProcessingException{
		ObjectMapper objectMapper=new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
}