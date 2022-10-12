package com.catalog.service;

import java.util.List;

import com.catalog.response.ProductResponse;

public interface IProductService {
	List<ProductResponse> findProductByCategory(String CategoryLevel);

}
