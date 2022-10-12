package com.catalog.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the category entity class
 * 
 * @author
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "catalog_tb")
public class Category {
	@Id
	private String id;
	private Integer levelId;
	private String levelName;
	private String createdBy;
	private LocalDateTime createdTime;
	private String lastUpdatedBy;
	private LocalDateTime lastUpdatedTime;
	private List<SubCategory> levels;
}
