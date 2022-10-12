package com.catalog.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.catalog.constants.Constants;
import com.catalog.entity.Category;
import com.catalog.entity.SubCategory;
import com.catalog.exception.HierrarchyNotFoundException;
import com.catalog.exception.ListEmptyException;
import com.catalog.repository.CategoryRepository;
import com.catalog.request.CategoryRequest;
import com.catalog.request.CategoryUpdateRequest;
import com.catalog.service.CategoryServiceImplementation;

/**
 * This is the service test class of user management {@link UserService}
 * 
 * @author
 *
 */
@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

	@InjectMocks
	private CategoryServiceImplementation categoryService;

	@Mock
	CategoryRepository categoryRepo;

	/**
	 * {@link CategoryServiceImplementation#updateCatalog(CategoryUpdateRequest)}
	 * This method tests when the parent category is updated to the database
	 * according to the corresponding levelId.
	 * 
	 */

	@Test
	public void updateCategoryTest_Success() {
		CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(1, "INTERNAL", "U213", null);
		Category category = new Category("633d48eebc392713a72d1e29", 1, "INTERNAL tools", "U213",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "U213", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), null);
		category.setLevelName("INTERNAL");
		category.setLastUpdatedBy("U213");
		category.setLastUpdatedTime(LocalDateTime.now());
		when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category updateCategory = categoryService.updateCatalog(categoryUpdateRequest);
		updateCategory.setId("6338766a9f9d0307958e3dbb");
		updateCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		updateCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, updateCategory);

	}
 
	@Test
	public void updateCategoryParentTest_Exception() {
		Category category = new Category("633d48eebc392713a72d1e29", 1, "INTERNAL tools", "U213",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "U213", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), null);
		CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(3, "INTERNAL", "U213", null);

		try {
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.empty());
			categoryService.updateCatalog(categoryUpdateRequest);
		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}
	}

	@Test
	public void updateCategoryChildTest_Success() {
		List<SubCategory> subcategory = new ArrayList<>();
		subcategory.add(new SubCategory(1, "DeveloperTools", null));
		CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(1, "INTERNAL", "U213", "1");
		Category category = new Category("633d48eebc392713a72d1e29", 1, "INTERNAL tools", "U213",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "U213", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subcategory);
		when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category updateCategory = categoryService.updateCatalog(categoryUpdateRequest);
		assertEquals(category, updateCategory);

	}

	@Test
	public void updateCategoryChild2Test_Success() {
		List<SubCategory> subcategory = new ArrayList<>();
		subcategory.add(new SubCategory(1, "DeveloperTools", List.of(new SubCategory(1, "MachineLearning", null))));
		CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(1, "INTERNAL", "U213", "1-1");
		Category category = new Category("633d48eebc392713a72d1e29", 1, "INTERNAL tools", "U213",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "U213", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subcategory);
		when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category updateCategory = categoryService.updateCatalog(categoryUpdateRequest);
		assertEquals(category, updateCategory);

	}

	@Test
	public void updateCategoryChild1Test_Exception() {

		try {
			List<SubCategory> subcategory = new ArrayList<>();
			subcategory.add(new SubCategory(1, "MachineLearning", null));
			CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(1, "developerTools", "U213", "2");
			Category category = new Category("633d48eebc392713a72d1e29", 1, "INTERNAL tools", "U213",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "U213", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subcategory);
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryService.updateCatalog(categoryUpdateRequest);
		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}

	}

	@Test
	public void updateCategoryChild2Test_Exception() {

		try {
			List<SubCategory> subcategory1 = new ArrayList<>();
			subcategory1.add(new SubCategory(1, "MachineLearning", null));
			List<SubCategory> subcategory = new ArrayList<>();
			subcategory.add(new SubCategory(1, "DeveloperTools", subcategory1));
			CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(3, "LearningTool", "U213", "3-2");
			Category category = new Category("633d48eebc392713a72d1e29", 1, "INTERNAL tools", "U213",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "U213", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subcategory);
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryService.updateCatalog(categoryUpdateRequest);
		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}

	}

	@Test
	public void updateCategoryChild3Test_Exception() {

		try {
			List<SubCategory> subcategory2 = new ArrayList<>();
			subcategory2.add(new SubCategory(1, "coginitive services", null));
			List<SubCategory> subcategory1 = new ArrayList<>();
			subcategory1.add(new SubCategory(1, "MachineLearning", subcategory2));
			List<SubCategory> subcategory = new ArrayList<>();
			subcategory.add(new SubCategory(1, "DeveloperTools", subcategory1));
			CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(1, "LearningTool", "U213", "1-1-2");
			Category category = new Category("633d48eebc392713a72d1e29", 1, "INTERNAL tools", "U213",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "U213", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subcategory);
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryService.updateCatalog(categoryUpdateRequest);
		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}

	}
	


	/**
	 * {@link CategoryServiceImpl#saveCatalog(CategoryRequest)} This method tests
	 * when the parent category is saved to database according to the level
	 * hierarchy
	 *
	 */
	@Test
	public void saveCategoryTest_Parent_Success() {
		List<SubCategory> subCategory = new ArrayList<>();
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal tools", "admin", "admin", null);
		when(categoryRepo.save(Mockito.any())).thenReturn(category);
		Category saveCategory = categoryService.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);
	}

	/**
	 * This method to save catalog when list is not empty
	 */
	@Test
	public void saveCategoryTest_Parent2_Success() {
		List<SubCategory> subCategory = new ArrayList<>();
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		Category secondCategory = new Category("6338766a9f9d0307958e2dbb", 2, "external tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);

		CategoryRequest categoryRequest = new CategoryRequest("external tools", "admin", "admin", null);
		when(categoryRepo.findAll()).thenReturn(List.of(category));
		Category saveCategory = categoryService.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e2dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(secondCategory, saveCategory);
	}

	/**
	 * This method to save child catalog when list is not empty
	 */
	@Test
	public void saveCategoryTest_Child_Success() {
		List<SubCategory> subCategory = new ArrayList<>();
		subCategory.add(new SubCategory(1, "internal 1", null));
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal 1", "admin", "admin", "1");
		when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category saveCategory = categoryService.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);

	}

	@Test
	public void saveCategoryTest_Child2_Success() {
		List<SubCategory> childSubCategory = new ArrayList<>();
		childSubCategory.add(new SubCategory(1, "internal 1.1", null));
		List<SubCategory> subCategory = new ArrayList<>();

		subCategory.add(new SubCategory(1, "internal 1", childSubCategory));
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal 1.1", "admin", "admin", "1-1");
		when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category saveCategory = categoryService.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);

	}

	@Test
	public void saveCategoryTest_Child3_Success() {
		List<SubCategory> secondChildSubCategory = new ArrayList<>();
		secondChildSubCategory.add(new SubCategory(1, "internal 1.1.1", null));
		List<SubCategory> childSubCategory = new ArrayList<>();
		childSubCategory.add(new SubCategory(1, "internal 1.1", secondChildSubCategory));
		List<SubCategory> subCategory = new ArrayList<>();

		subCategory.add(new SubCategory(1, "internal 1", childSubCategory));
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal 1.1.1", "admin", "admin", "1-1-1");
		when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category saveCategory = categoryService.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);

	}

	@Test
	public void saveCategoryTest_Child1_Exception() {
		CategoryRequest categoryRequest = new CategoryRequest("internal 1", "admin", "admin", "1");
		HierrarchyNotFoundException ex = assertThrows(HierrarchyNotFoundException.class,
				() -> categoryService.saveCatalog(categoryRequest));
		assertEquals(Constants.HIERARCHY_EXCEPTION_MSG, ex.getErrorMessage());
	}

	@Test
	public void saveCategoryTest_Child2_Exception() {
		try {
			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					null);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1", "admin", "admin", "1-1");
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryService.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}
	}

	@Test
	public void saveCategoryTest_Child3_Exception() {
		try {
			List<SubCategory> subCategory = new ArrayList<>();

			subCategory.add(new SubCategory(1, "internal 1", null));
			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subCategory);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1.1", "admin", "admin", "1-1-1");
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryService.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}
	}

	@Test
	public void saveCategoryTest_Child4_Exception() {
		try {
			List<SubCategory> childSubCategory = new ArrayList<>();
			childSubCategory.add(new SubCategory(1, "internal 1.1", null));
			List<SubCategory> subCategory = new ArrayList<>();

			subCategory.add(new SubCategory(2, "internal 1", childSubCategory));

			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subCategory);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1", "admin", "admin", "1-1");
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryService.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}
	}

	@Test
	public void saveCategoryTest_Child5_Exception() {
		try {
			List<SubCategory> secondChildSubCategory = new ArrayList<>();
			secondChildSubCategory.add(new SubCategory(1, "internal 1.1.1", null));
			List<SubCategory> childSubCategory = new ArrayList<>();
			childSubCategory.add(new SubCategory(2, "internal 1.1", secondChildSubCategory));
			List<SubCategory> subCategory = new ArrayList<>();

			subCategory.add(new SubCategory(1, "internal 1", childSubCategory));
			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subCategory);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1.1", "admin", "admin", "1-1-1");
			when(categoryRepo.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryService.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierrarchyNotFoundException);
		}
	}

	@Test
	public void fetchCategoryServiceTest() {

		Category category1 = new Category("id1", 1, "internal", "u1212", LocalDateTime.now(), "u1212",
				LocalDateTime.now(), null);

		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(category1);

		when(categoryRepo.findAll()).thenReturn(categoryList);
		categoryService.fetchCategory();
	}

	@Test
	public void fetchCategoryServiceTestException() {

		try {
			Category category1 = new Category("id1", 1, "internal", "u1212", LocalDateTime.now(), "u1212",
					LocalDateTime.now(), null);
			Category category2 = new Category("id2", 2, "external", "u1212", LocalDateTime.now(), "u1212",
					LocalDateTime.now(), null);
			List<Category> categoryList = new ArrayList<Category>();
			categoryList.add(category1);
			categoryList.add(category2);
			when(categoryRepo.findAll()).thenReturn(null);

			categoryService.fetchCategory();
		} catch (Exception e) {
			assertTrue(e instanceof ListEmptyException);
		}
	}
}
