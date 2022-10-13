package com.catalog.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

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
	@NotNull(message="Id field must be not null")
	private String id;
	private String productName;
	private Float contractSpend;
	private int stakeholder;
	private List<String> categoryLevel;	
	private String lastUpdateBy;
	private List<String> categoryDescription;
	private String productDescription;
}
