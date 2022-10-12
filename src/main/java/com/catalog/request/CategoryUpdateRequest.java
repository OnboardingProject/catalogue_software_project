package com.catalog.request;

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
public class CategoryUpdateRequest {
	 
	private Integer levelId;
	private String levelName;
	private String lastUpdatedBy;
	private String hierarchyLevel;
}
