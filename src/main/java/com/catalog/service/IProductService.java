package com.catalog.service;

import java.util.List;

import com.catalog.entity.Product;
import com.catalog.request.ProductRequest;
import com.catalog.request.ProductUpdateRequest;
import com.catalog.response.ProductResponse;

public interface IProductService {
	Product addProduct(ProductRequest productRequest);

	public List<Product> searchByName(String productName);

	Product updateProduct(ProductUpdateRequest productUpdateRequest);

	List<ProductResponse> findProductByCategory(String CategoryLevel);
	
	List<Product> findAllProducts();

}
