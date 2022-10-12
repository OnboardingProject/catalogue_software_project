package com.catalog.controllertest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.catalog.controller.ProductController;
import com.catalog.response.ProductResponse;
import com.catalog.service.ProductServiceImplementation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductControllerTest {

	@InjectMocks
	private ProductController productController;

	@Mock
	private ProductServiceImplementation productServiceImplementation;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
	}

	/***
	 * Test for findProductByCategoryTest
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */

	@Test
	public void findProductByCategoryTest_successs() throws JsonProcessingException, Exception {
		ProductResponse productResponseVo = new ProductResponse("pulse", "product description",
				categoryLevelDescriptionData());
		List<ProductResponse> productResponseVoList = new ArrayList<>();
		productResponseVoList.add(productResponseVo);
		when(productServiceImplementation.findProductByCategory(Mockito.anyString())).thenReturn(productResponseVoList);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/products/bycategory?categoryLevel=1")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString("1-1-1")))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(productServiceImplementation, times(1)).findProductByCategory(Mockito.anyString());
	}

	private List<String> categoryLevelDescriptionData() {
		List<String> categoryLevelDescription = new ArrayList<>();
		categoryLevelDescription.add("Internal/workflow/chattool");
		categoryLevelDescription.add("Security/developertool/usertelemetry");
		return categoryLevelDescription;
	}

}
