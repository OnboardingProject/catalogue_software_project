package com.catalog.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {

	
	/**
	 *product response data
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  productName;    
	private String productDescription;
	private List<String> categoryLevelDescription;
	
}
