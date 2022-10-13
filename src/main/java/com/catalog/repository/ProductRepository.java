package com.catalog.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.catalog.entity.Product;
/**
 * This is the repository class for product
 *
 */
public interface ProductRepository extends MongoRepository<Product, String> {
	/***
	 *  To find products from product table for category level 
	 * @param categoryLevel
	 * @return
	 */
    @Query("{categoryLevel:{$regex:/^?0/}}")
	List<Product> findProductsByCategory(String categoryLevel);
	
    @Query("{productName:{$regex:?0}}")
	List<Product> getProductByName(String productName);

}
