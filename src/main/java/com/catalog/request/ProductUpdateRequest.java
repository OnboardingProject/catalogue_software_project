package com.catalog.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest implements Serializable{
	/**
	 * Update Request data
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message="Id field must be not empty")
	private String id;
	private String productName;
	private Float contractSpend;
	private int stakeholder;
	private List<String> categoryLevel;	
	private String lastUpdateBy;
	private Boolean isDeleted;
	private List<String> categoryLevelDescription;
	private String productDescription;
}
