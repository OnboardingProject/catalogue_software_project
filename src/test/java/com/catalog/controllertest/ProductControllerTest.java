package com.catalog.controllertest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
import com.catalog.entity.Product;
import com.catalog.request.ProductRequest;
import com.catalog.request.ProductUpdateRequest;
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
	
	private List<String> categoryLevel() {
		List<String> categoryLevel = new ArrayList<String>();
		categoryLevel.add("1-1-1");
		return categoryLevel;
	}
	private List<String> catagoryDescription() {
		List<String> category= new ArrayList<String>();
		category.add("desc");
		return category;
	}

	private List<Product> product() {
		List<Product> product = new ArrayList<Product>();
		product.add(new Product("632b231ad3433a705e3b30cd", "Azure Cognitive Sevices", 18f, 6, categoryLevel(),
				"athira", LocalDateTime.now(), "athira", LocalDateTime.now(), false,catagoryDescription(),"desc1"));
		return product; 
	}
	
	LocalDateTime time=LocalDateTime.now(); 
	@Test
	private ProductRequest addProductTestData() {

		List<String> categorylevel = new LinkedList<String>();
		categorylevel.add("1-1-1");
		categorylevel.add("2-2-1");
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		ProductRequest productRequest = new ProductRequest("Azure",10f,7,categorylevel,"viji","raj",false,categoryleveldes,"Description");
		return productRequest; 
	}
	
	
	
	
	private ProductUpdateRequest updateProductTestData() {

		List<String> categorylevel = new LinkedList<String>();
		categorylevel.add("1-1-1");
		categorylevel.add("2-2-1");
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("Pjt111", "Azure", 10f,6, categorylevel,"nisha",false, categoryleveldes,"Description");
		return productUpdateRequest;
	}
	/**{@link ProductController #updateProduct(UpdateDTO) with response}
	 * 
	 */
	@Test
    public void addProjectTest_success() throws Exception {
    	
		ProductRequest productRequest=addProductTestData();	
		List<String>category=Arrays.asList("1-1-1");
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		LocalDateTime time = LocalDateTime.now();
		Product product=new Product("pjt111","Azure",10f,7,category,"viji",time,"raj",time,false,categoryleveldes,"product description");
    	
        when(productServiceImplementation.addProduct(Mockito.any(ProductRequest.class))).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(productRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
	/**{@link ProductController #addProduct(ProductDTO) with exception}
	 * 
	 */
	@Test
	public void addProjectTest_Failure() throws Exception{
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		ProductRequest productRequest = new ProductRequest("",10f,7,null,"viji","raj",false,categoryleveldes,"Description");	
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(productRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
 
	}
    
	/**{@link ProductController #updateProduct(UpdateDTO)with response}
	 * 
	 */
	@Test
    public void modifyProductTest() throws Exception {
		LocalDateTime time = LocalDateTime.now();
		ProductUpdateRequest productUpdateRequest=updateProductTestData();	
		List<String>category=Arrays.asList("1-1-1");
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		Product product=new Product("pjt111","Azure",10f,7,category,"viji",time,"raj",time,false,categoryleveldes,"product description");
    	productUpdateRequest.setProductName("Azure");
    	productUpdateRequest.setProductDescription("product description");
    	productUpdateRequest.setContractSpend(10f);
    	productUpdateRequest.setStakeholder(5);
    	productUpdateRequest.setCategoryLevel(category);
    	productUpdateRequest.setCategoryLevelDescription(categoryleveldes);
    	productUpdateRequest.setLastUpdateBy("nisha");  
    	
        when(productServiceImplementation.updateProduct(Mockito.any(ProductUpdateRequest.class))).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.put("/product")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(productUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
	/**{@link ProductController #updateProduct(UpdateDTO) with exception}
	 * 
	 */
	@Test
	public void modifyProjectTest_failure() throws Exception{
		List<String>category=Arrays.asList("1-1-1");
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		 when(productServiceImplementation.updateProduct(Mockito.any(ProductUpdateRequest.class))).thenReturn(null);
		 ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("Pjt111", "Azure", 10f,6, category,"nisha",false, categoryleveldes,"Description");
		mockMvc.perform(MockMvcRequestBuilders.put("/product").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(productUpdateRequest)))
				.andExpect(MockMvcResultMatchers.status().isConflict());
 
	}
	/**{@link ProductController #searchByName(String) with response}
	 * 
	 */
	@Test
	public void searchByNameTestSucess() throws JsonProcessingException, Exception {

		when(productServiceImplementation.searchByName(Mockito.anyString())).thenReturn(product());
		mockMvc.perform(MockMvcRequestBuilders.get("/product/search/name").param("productName", "Azure")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

	}
	/**{@link ProductController #searchByName(String) with exception}
	 * 
	 */
	@Test
	public void searchByNameTestFailure() throws JsonProcessingException, Exception {
		when(productServiceImplementation.searchByName(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get("/product/search/name").param("productName", "Teams")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent());
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
		mockMvc.perform(MockMvcRequestBuilders.get("/product/search/category?categoryLevel=1")
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
