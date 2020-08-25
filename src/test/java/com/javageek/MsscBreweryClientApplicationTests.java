package com.javageek;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javageek.web.client.BreweryClient;
import com.javageek.web.client.CustomerClient;
import com.javageek.web.model.BeerDto;
import com.javageek.web.model.CustomerDto;

@SpringBootTest
class MsscBreweryClientApplicationTests {
	
	@Autowired
	BreweryClient breweryClient;
	
	@Autowired
	CustomerClient customerClient;

	@Test
	void getBeerById() {
		BeerDto beerDto = breweryClient.getBeerById(UUID.randomUUID());
		assertNotNull(beerDto);
	}
	
	@Test
	void saveNewBeer() {
		BeerDto beerDto = BeerDto.builder().beerName("Beer 1").build();
		URI uri = breweryClient.saveBeer(beerDto);
		assertNotNull(uri);
		System.out.println(uri.toString());
	}

	@Test
	void testUpdateBeer() {
		BeerDto beerDto = BeerDto.builder().beerName("New Beer").build();
		breweryClient.updateBeer(UUID.randomUUID(), beerDto);
		
	}
	
	@Test
	void testdeleteBeer() {
		breweryClient.deleteBeer(UUID.randomUUID());
	}
	
	@Test
	void getCustomerById() {
		CustomerDto customerDto = customerClient.getCustomerById(UUID.randomUUID());
		assertNotNull(customerDto);
	}
	
	@Test
	void saveNewCustomer() {
		CustomerDto customerDto = CustomerDto.builder().customerName("John Doe").build();
		URI uri = customerClient.saveCustomer(customerDto);
		
		assertNotNull(uri);
		System.out.println(uri.toString());
	}
	
	@Test
	void testUpdateCustomer() {
		CustomerDto customerDto = CustomerDto.builder().customerName("John Doe").build();
		customerClient.updateCustomer(UUID.randomUUID(), customerDto);
	}
	
	@Test
	void testDeleteCustomer() {
		customerClient.deleteCustomer(UUID.randomUUID());
	}
}
