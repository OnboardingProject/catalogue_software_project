package com.catalog.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the category Request class for updation in SoftwareCatalog 
 * 
 * @author 
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest implements Serializable{
	 
	/**
	 * Category Update Request data 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message="Id field must be not null")
	private Integer levelId;
	private String levelName;
	private String lastUpdatedBy;
	private String hierarchyLevel;
}
