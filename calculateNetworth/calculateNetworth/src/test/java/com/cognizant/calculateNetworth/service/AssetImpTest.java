package com.cognizant.calculateNetworth.service;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
//import static org.mockito.Mockito.when;  
import com.cognizant.calculateNetworth.controller.AuthClient;
import com.cognizant.calculateNetworth.repository.AssetRepository;
import com.cognizant.calculateNetworth.service.AssetServiceImpl;
import com.cognizant.calculateNetworth.service.SellAssetServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.calculateNetworth.controller.AuthClient;
import com.cognizant.calculateNetworth.model.Asset;
import com.cognizant.calculateNetworth.repository.AssetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=AssetServiceImpl.class)
class AssetImpTest {

	@MockBean
	AssetRepository repository;

	@Mock
	AuthClient authClient;
	
	@Autowired
	AssetServiceImpl assetServiceImpl;
	
	@Test
	public void testgetAllAssetForPortfolio() {
		Asset asset1=new Asset(1,"AMZ",101,"Share",10);
		Asset asset2=new Asset(2,"GGL",101,"Share",10);
		List<Asset> assetList=new ArrayList<>();
		assetList.add(asset1);
		assetList.add(asset2);
		Mockito.when(repository.findByPortfolioidOrderByAssetid(101)).thenReturn(assetList);
		assertThat(assetServiceImpl.getAllAssetForPortfolio(101)).isEqualTo(assetList);
	}
}
