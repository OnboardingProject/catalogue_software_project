package com.catalog.service;

import java.util.List;

import com.catalog.entity.Category;
import com.catalog.request.CategoryRequest;
import com.catalog.request.CategoryUpdateRequest;

/**
 * This is the service interface of Category
 * 
 * @author
 *
 */
public interface ICategoryService {
	
	/**
	 * @param catgeoryRequest
	 * @return
	 */
	public Category saveCatalog(CategoryRequest catgeoryRequest);

	public Category updateCatalog(CategoryUpdateRequest catgeoryUpdateRequest);

	

	public List<Category> fetchCategory();
	
}
