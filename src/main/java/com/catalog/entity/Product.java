package com.catalog.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
	private String id;
	private String  productName;    
	private String contractSpend;
	private String stakeholder;
	private String productDescription;
	private List<String> categoryLevel;
	private List<String> categoryLevelDescription;
	private String createdBy;
	private LocalDateTime createdTime;
	private String lastUpdatedBy ;
	private LocalDateTime lastUpdatedTime;
	private Boolean isDeleted;
}
