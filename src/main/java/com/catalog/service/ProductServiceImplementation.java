package com.catalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.catalog.constants.Constants;
import com.catalog.entity.Product;
import com.catalog.exception.ProductException;
import com.catalog.repository.ProductRepository;
import com.catalog.request.ProductRequest;
import com.catalog.request.ProductUpdateRequest;
import com.catalog.response.ProductResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
	/**
	 * This method will save the details in DB if the product is not already
	 * available in DB
	 * 
	 * @throws DataBaseErrorException
	 * @throws ProductAlreadyExistsException
	 */
	@Override
	public Product addProduct(ProductRequest productRequest) {
		Product product = new Product();

		Optional<Product> opt = productRepository.findById(productRequest.getProductName());

		if (opt.isPresent()) {
			log.error("Given product is already added!!!");
			throw new ProductException(Constants.ALREADY_EXISTS, HttpStatus.ALREADY_REPORTED);
		} else {
			log.info("Add product details is started");
			try {
				setProduct(productRequest, product);

				Product productSaved = productRepository.save(product);
				log.info("Product details are added successfully");
				return productSaved;
			} catch (ProductException ex) {
				log.error("Problem occured during adding product details to the database");
				throw new ProductException(Constants.DB_EXCEPTION, HttpStatus.CONFLICT);
			}

		}
		

	}
	private void setProduct(ProductRequest productRequest, Product product) {
		product.setProductName(productRequest.getProductName());
		product.setProductDescription(productRequest.getProductDescription());
		product.setContractSpend(productRequest.getContractSpend());
		product.setStakeholder(productRequest.getStakeholder());
		product.setCategoryLevel(productRequest.getCategoryLevel());
		product.setCategoryLevelDescription(productRequest.getCategoryLevelDescription());
		product.setCreatedBy(productRequest.getCreatedBy());
		product.setCreatedTime(LocalDateTime.now());
		product.setLastUpdatedBy(productRequest.getLastUpdateBy());
		product.setLastUpdatedTime(LocalDateTime.now());
		product.setIsDeleted(false);
	}
	/**
	 * Method to get the product by name (using word in any position of name)
	 *
	 * @param Product name
	 * @return Product
	 */
	@Override
	public List<Product> searchByName(String productName) {
		log.info("ProductService searchtByName  call started with {}", productName);
		if (StringUtils.isEmpty(productName)) {
			log.error("Product name is blank");
			throw new ProductException(Constants.PRODUCTNAME_EMPTY, HttpStatus.BAD_REQUEST);
		}
		try {
			List<Product> product = new ArrayList<Product>();
			product = productRepository.getProductByName(productName);
			log.info("ProductService searchtByName  call ended with {}", product);
			return product.stream().filter(a -> a.getIsDeleted().equals(false)).collect(Collectors.toList());
		} catch (Exception e) {
			throw new ProductException(Constants.DB_EXCEPTION, HttpStatus.CONFLICT);
		}
	}

	/**
	 * This method will get the product details from details and modify the details
	 * then save the details in DB .
	 * 
	 * @throws DataBaseErrorException
	 * @throws DataNotFoundException
	 */
	@Override
	public Product updateProduct(ProductUpdateRequest productUpdateRequest) {
		log.info("{} In service class,the modify method is started" + productUpdateRequest);
		try {

			Optional<Product> opt = productRepository.findById(productUpdateRequest.getId());

			if (opt.isPresent()) {

				log.info("Product details are ready to modify");
				Product getproduct = opt.get();
				getproduct.setProductName(productUpdateRequest.getProductName());
				getproduct.setProductDescription(productUpdateRequest.getProductDescription());
				getproduct.setContractSpend(productUpdateRequest.getContractSpend());
				getproduct.setStakeholder(productUpdateRequest.getStakeholder());
				getproduct.setCategoryLevel(productUpdateRequest.getCategoryLevel());
				getproduct.setCategoryLevelDescription(productUpdateRequest.getCategoryLevelDescription());
				getproduct.setIsDeleted(productUpdateRequest.getIsDeleted());
				getproduct.setLastUpdatedBy(productUpdateRequest.getLastUpdateBy());
				getproduct.setLastUpdatedTime(LocalDateTime.now());
				Product updatedproduct = productRepository.save(getproduct);
				log.info("{} Product details are modified successfully" + productUpdateRequest);
				return updatedproduct;
			}

			else {
				log.error("No product details are available to modify");
				throw new ProductException(Constants.PRODUCT_NOT_FOUND, HttpStatus.BAD_REQUEST);
			}
		} catch (ProductException ex) {
			log.error("Problem occured in database during modify process");
			throw new ProductException(Constants.DB_EXCEPTION, HttpStatus.CONFLICT);
		}
	}
	

}
