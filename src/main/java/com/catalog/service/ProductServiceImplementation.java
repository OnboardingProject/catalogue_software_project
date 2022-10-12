package com.catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalog.entity.Product;
import com.catalog.repository.ProductRepository;
import com.catalog.response.ProductResponse;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ProductServiceImplementation implements IProductService {
	
	@Autowired
	ProductRepository productRepository;
	/***
	 * The findProductByCategory method is used to fetch products based on
	 * categoryLevel
	 * 
	 * @param:CategoryLevel
	 * @return: products with specified category and replaces levelId with levelName
	 * 
	 * @exception:ProductNotFoundException
	 */

	@Override
	public List<ProductResponse> findProductByCategory(String CategoryLevel) {
		log.info("findProductByCategory started for " + CategoryLevel);
		List<Product> productsByCategory = productRepository.findProductsByCategory(CategoryLevel);
		List<ProductResponse> productResponse = new ArrayList<ProductResponse>();
		if (!productsByCategory.isEmpty()) {
			ModelMapper modelMapper = new ModelMapper();
			productResponse = productsByCategory.stream().map(p -> modelMapper.map(p, ProductResponse.class))
					.collect(Collectors.toList());
			log.info("findProductByCategory ended for " + CategoryLevel);
			return productResponse;
		} else {
			log.info("findProductByCategory couldn't find for " + CategoryLevel);
			return productResponse;
		}
	}

}
