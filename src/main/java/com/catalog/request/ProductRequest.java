package com.catalog.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest implements Serializable {
	/**
	 * Product Request data
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message="Product Name cannot be empty")
	private String productName;	
	private Float contractSpend;
	private Integer stakeholder;
	@NotEmpty(message="Category Level cannot be empty")
	private List<String> categoryLevel;
	private String createdBy;	
	private String lastUpdateBy;	
	private boolean isDeleted;
	private List<String> categoryLevelDescription;
	private String productDescription;
}
