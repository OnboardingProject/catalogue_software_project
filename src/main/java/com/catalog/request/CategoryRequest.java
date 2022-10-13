package com.catalog.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest implements Serializable {
	/**
	 * Category Request data
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "Levelname cannot be empty")
	private String levelName;
	private String createdBy;
	private String lastUpdatedBy;
	private String hierarchyLevel;
}
 