package com.catalog.controllertest;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

import com.catalog.controller.CategoryController;
import com.catalog.entity.Category;
import com.catalog.request.CategoryRequest;
import com.catalog.request.CategoryUpdateRequest;
import com.catalog.service.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is the Category Controller test class where we test add category api and
 * update category api {@link CategoryController}
 * 
 * @author HP
 *
 */

public class CategoryControllerTest {

	@InjectMocks
	private CategoryController categoryController;

	@Mock
	ICategoryService categoryService;
	@Autowired
	MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

	}

	Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internaltools", "admin", LocalDateTime.now(),
			"admin", LocalDateTime.now(), null);

	private CategoryRequest categoryRequest() {
		CategoryRequest categoryRequest = new CategoryRequest("internaltools", "admin", "admin", null);
		return categoryRequest;
	} 

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
	}

	@Test
	public void saveCatalogTest_Success() throws Exception {

		when(categoryService.saveCatalog(Mockito.any(CategoryRequest.class))).thenReturn(category);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/catalog/add-category").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(categoryRequest())))
				.andExpect(MockMvcResultMatchers.status().isCreated());

	}

	@Test
	public void fetchAllCategoryTestSuccess() throws Exception {

		Category category1 = new Category("id1", 1, "internal", "u1212", LocalDateTime.now(), "u1212",
				LocalDateTime.now(), null);
		Category category2 = new Category("id2", 2, "external", "u1212", LocalDateTime.now(), "u1212",
				LocalDateTime.now(), null);
		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(category1);
		categoryList.add(category2);
		when(categoryService.fetchCategory()).thenReturn(categoryList);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/catalog/fetch-category").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200));

	}

	/**
	 * {@link CategoryController #updateCategory(CategoryRequest)}
	 * 
	 * @throw Exception
	 */
	@Test
	public void updateTest() throws Exception {

		Category category = new Category("abcd12", 1, "Internal", "u123", LocalDateTime.now(), "u123",
				LocalDateTime.now(), null);
		CategoryUpdateRequest categoryEdit = new CategoryUpdateRequest(1, "INTERNAL", "U213", null);
		when(categoryService.updateCatalog(Mockito.any(CategoryUpdateRequest.class))).thenReturn(category);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/catalog/update-category")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(categoryEdit)))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

}
