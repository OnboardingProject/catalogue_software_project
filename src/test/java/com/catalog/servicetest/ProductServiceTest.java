package com.catalog.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.catalog.entity.Product;
import com.catalog.repository.ProductRepository;
import com.catalog.response.ProductResponse;
import com.catalog.service.ProductServiceImplementation;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@InjectMocks
	ProductServiceImplementation serviceImplementation;

	@Mock
	ProductRepository productRepository;
	/***
	 * 
	 * Test of findProductByCategory for success
	 * 
	 */

	@Test
	public void findProductByCategory_success() {
		List<String> categoryLevel = categoryLevelData();
		List<String> categoryLevelDescription = categoryLevelDescriptionData();
		Product product = new Product("632b3a8e29dca725884ac6d6", "pulse", "contract1", "stakeholder1",
				"product description", categoryLevel, categoryLevelDescription, "admin", LocalDateTime.now(), "admin",
				LocalDateTime.now(), false);
		List<Product> productList = new ArrayList<>();
		productList.add(product);
		ProductResponse productResponseVo = new ProductResponse("pulse", "product description",
				categoryLevelDescription);
		List<ProductResponse> productResponseVoList = new ArrayList<>();
		productResponseVoList.add(productResponseVo);
		when(productRepository.findProductsByCategory(Mockito.anyString())).thenReturn(productList);
		List<ProductResponse> productResponseList = serviceImplementation.findProductByCategory(Mockito.anyString());
		assertEquals(productResponseVoList, productResponseList);

	}
	/***
	 * Test for findProductByCategory with Exception ProductNotFoundException
	 * 
	 */

	@Test
	public void findProductByCategory_Failure() {
		    List<Product> productList = new ArrayList<>();
		    List<ProductResponse> productResponseVoList = new ArrayList<>();
			when(productRepository.findProductsByCategory(Mockito.anyString())).thenReturn(productList);
			List<ProductResponse> productResponseList = serviceImplementation.findProductByCategory(Mockito.anyString());
			//assertNull(productResponseList);
			assertEquals(productResponseVoList, productResponseList);

		

	}

	private List<String> categoryLevelDescriptionData() {
		List<String> categoryLevelDescription = new ArrayList<>();
		categoryLevelDescription.add("Internal/workflow/chattool");
		categoryLevelDescription.add("Security/developertool/usertelemetry");
		return categoryLevelDescription;
	}

	private List<String> categoryLevelData() {
		List<String> categoryLevel = new ArrayList<>();
		categoryLevel.add("1-1-1");
		categoryLevel.add("1-2-1");
		return categoryLevel;
	}
}
