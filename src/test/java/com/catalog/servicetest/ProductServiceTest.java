package com.catalog.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.catalog.entity.Product;
import com.catalog.exception.ProductException;
import com.catalog.repository.ProductRepository;
import com.catalog.request.ProductRequest;
import com.catalog.request.ProductUpdateRequest;
import com.catalog.response.ProductResponse;
import com.catalog.service.ProductServiceImplementation;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@InjectMocks
	ProductServiceImplementation serviceImplementation;

	@Mock
	ProductRepository productRepository;

	@Test
	private ProductRequest addProductTestData() {

		List<String> categorylevel = new LinkedList<String>();
		categorylevel.add("1-1-1");
		categorylevel.add("2-2-1");
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		ProductRequest productRequest = new ProductRequest("Azure", 10f, 7, categorylevel, "viji", "raj", false,
				categoryleveldes, "Description");
		return productRequest;
	}

	@Test
	private ProductUpdateRequest getProductTestData() {

		List<String> categorylevel = new LinkedList<String>();
		categorylevel.add("1-1-1");
		categorylevel.add("2-2-1");
		List<String> categoryleveldes = new LinkedList<String>();
		categoryleveldes.add("Category description");
		ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("Pjt111", "Azure", 10f, 6, categorylevel,
				"nisha", false, categoryleveldes, "Description");
		return productUpdateRequest;
	}

	private List<Product> product() {
		List<Product> product = new ArrayList<Product>();
		product.add(new Product("632b231ad3433a705e3b30cd", "Azure Cognitive Sevices", 18f, 6, categoryLevelData(),
				"athira", LocalDateTime.now(), "athira", LocalDateTime.now(), false, categoryLevelDescriptionData(),
				"desc1"));
		return product;
	}

	@Test
	public void addProductTestSuccess() throws Exception {

		ProductRequest productRequest = addProductTestData();
		LocalDateTime time = LocalDateTime.now();

		Product product = new Product("pjt111", productRequest.getProductName(), productRequest.getContractSpend(),
				productRequest.getStakeholder(), productRequest.getCategoryLevel(), productRequest.getCreatedBy(), time,
				productRequest.getLastUpdateBy(), time, false, productRequest.getCategoryLevelDescription(),
				productRequest.getProductDescription());
		when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
		assertEquals(product, serviceImplementation.addProduct(productRequest));

	}

	@Test
	public void addProductTestFailure() throws Exception {

		ProductRequest productRequest = addProductTestData();
		LocalDateTime time = LocalDateTime.now();

		Product product = new Product("pjt111", productRequest.getProductName(), productRequest.getContractSpend(),
				productRequest.getStakeholder(), productRequest.getCategoryLevel(), productRequest.getCreatedBy(), time,
				productRequest.getLastUpdateBy(), time, false, productRequest.getCategoryLevelDescription(),
				productRequest.getProductDescription());
		when(productRepository.findById(productRequest.getProductName())).thenReturn(Optional.of(product));
		assertThrows(ProductException.class, () -> serviceImplementation.addProduct(productRequest));
	}

	@Test()
	public void addDataBaseErrorExceptionTest() {
		ProductRequest productRequest = addProductTestData();
		when(productRepository.save(Mockito.any(Product.class))).thenThrow(ProductException.class);
		assertThrows(ProductException.class, () -> serviceImplementation.addProduct(productRequest));
	}

	@Test
	public void updateProductTestSuccess() throws Exception {

		ProductUpdateRequest productUpdateRequest = getProductTestData();
		LocalDateTime time = LocalDateTime.now();
		List<String> category = Arrays.asList("1-1-1");
		List<String> categorydes = Arrays.asList("category description");
		Product product = new Product("pjt111", "Azure", 10f, 7, category, "viji", time, "raj", time, false,
				categorydes, "product description");
		when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

		product.setProductName(productUpdateRequest.getProductName());
		product.setContractSpend(productUpdateRequest.getContractSpend());
		product.setStakeholder(productUpdateRequest.getStakeholder());
		product.setCategoryLevel(productUpdateRequest.getCategoryLevel());
		product.setLastUpdatedBy(productUpdateRequest.getLastUpdateBy());

		when(productRepository.save(product)).thenReturn(product);
		assertEquals(product, serviceImplementation.updateProduct(productUpdateRequest));

	}

	@Test
	public void updateProductTestFailure() throws Exception {
		ProductUpdateRequest productUpdateRequest = getProductTestData();
		when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(ProductException.class, () -> serviceImplementation.updateProduct(productUpdateRequest));

	}

	@Test()
	public void updateDataBaseErrorExceptionTest() {
		ProductUpdateRequest productUpdateRequest = getProductTestData();
		LocalDateTime time = LocalDateTime.now();
		List<String> category = Arrays.asList("1-1-1");
		List<String> categorydes = Arrays.asList("category description");
		Product product = new Product("pjt111", "Azure", 10f, 7, category, "viji", time, "raj", time, false,
				categorydes, "Product description");
		// Product product1=new Product("","",0,0,null,"",null,"",null,false);
		when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

		product.setProductName(productUpdateRequest.getProductName());
		product.setContractSpend(productUpdateRequest.getContractSpend());
		product.setStakeholder(productUpdateRequest.getStakeholder());
		product.setCategoryLevel(productUpdateRequest.getCategoryLevel());
		product.setLastUpdatedBy(productUpdateRequest.getLastUpdateBy());

		when(productRepository.save(product)).thenThrow(ProductException.class);
		assertThrows(ProductException.class, () -> serviceImplementation.updateProduct(productUpdateRequest));
	}

	@Test
	public void searchByNameTestSuccess() {
		when(productRepository.getProductByName(Mockito.anyString())).thenReturn(product());
		List<Product> product1 = serviceImplementation.searchByName("Azure");
		assertEquals("Azure Cognitive Sevices", product1.get(0).getProductName());
	}

	@Test
	public void searchByNameIsDeletedTestSuccess() {
		List<Product> product = new ArrayList<Product>();
		product.add(new Product("632b231ad3433a705e3b30cd", "Azure Cognitive Sevices", 18f, 6, categoryLevelData(),
				"athira", LocalDateTime.now(), "athira", LocalDateTime.now(), false, categoryLevelDescriptionData(),
				"desc1"));
		product.add(new Product("632b2359d3433a705e3b30ce", "Azure DevOps", 18f, 6, categoryLevelData(), "aneesh",
				LocalDateTime.now(), "aneesh", LocalDateTime.now(), true, categoryLevelDescriptionData(), "desc1"));
		when(productRepository.getProductByName(Mockito.anyString())).thenReturn(product);
		List<Product> products = serviceImplementation.searchByName("Azure");
		assertEquals(products.size(), 1);
	}

	@Test
	public void searchByNameTestFailure() {
		List<Product> product = new ArrayList<Product>();
		when(productRepository.getProductByName(Mockito.anyString())).thenReturn(product);
		List<Product> products = serviceImplementation.searchByName("A");
		assertEquals(products, product);
	}

	@Test
	public void searchByNameExceptionTest() {
		ProductException ex = assertThrows(ProductException.class, () -> serviceImplementation.searchByName(""));
		assertEquals("No product name given as input", ex.getErrorMessage());
	}

	@Test
	public void searchByNameDBExceptionTest() {
		when(productRepository.getProductByName(Mockito.anyString()))
				.thenThrow(new ProductException("DB Exception occured", HttpStatus.CONFLICT));
		ProductException ex = assertThrows(ProductException.class, () -> serviceImplementation.searchByName("M"));
		assertEquals("DB Exception occured", ex.getErrorMessage());
	}
	/***
	 * 
	 * Test of findProductByCategory for success
	 * 
	 */

	@Test
	public void findProductByCategory_success() {
		Product product = new Product("632b231ad3433a705e3b30cd", "Azure Cognitive Sevices", 18f, 6,
				categoryLevelData(), "athira", LocalDateTime.now(), "athira", LocalDateTime.now(), false,
				categoryLevelDescriptionData(), "desc1");
		List<Product> productList = new ArrayList<>();
		productList.add(product);
		ProductResponse productResponseVo = new ProductResponse("Azure Cognitive Sevices", "desc1",
				categoryLevelDescriptionData());
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
		// assertNull(productResponseList);
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
