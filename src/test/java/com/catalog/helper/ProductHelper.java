package com.catalog.helper;

import java.util.Arrays;

import com.catalog.entity.Product;
import com.catalog.request.ProductRequest;

public class ProductHelper {
	public static  Product getProduct() {
	    Product product=new Product();
	    product.setProductId("productId");
	    product.setLastUpdatedBy("user1");
	    return product;
	    
	}
	public static ProductRequest setProductDTO() {
		ProductRequest productRequest = new ProductRequest();
	       
	        productRequest.setProductName("Azure");
	        productRequest.setProductDescription("Description about the project");
	        productRequest.setContractSpend(10F);
	        productRequest.setStakeholder(7);
	        productRequest.setCategoryLevel(Arrays.asList("1-1-1"));
	        productRequest.setCategoryLevelDescription(Arrays.asList("detailed description"));
	        productRequest.setCreatedBy("user1");
	        productRequest.setDeleted(false);
	        return productRequest;
	}
}
