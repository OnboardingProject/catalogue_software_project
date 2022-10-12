package com.catalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.catalog.constants.Constants;
import com.catalog.entity.Category;
import com.catalog.entity.SubCategory;
import com.catalog.exception.HierrarchyNotFoundException;
import com.catalog.exception.ListEmptyException;
import com.catalog.repository.CategoryRepository;
import com.catalog.repository.ProductRepository;
import com.catalog.request.CategoryRequest;
import com.catalog.request.CategoryUpdateRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * This is the service class for Category where we write business logic for
 * update category
 * 
 * @author
 *
 */
@Slf4j
@Service
public class CategoryServiceImplementation implements ICategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	ProductRepository productRepository;

	/**
	 * save method which is used to save parent and child category
	 */
	public Category saveCatalog(CategoryRequest categoryRequest) {
		log.info("Enterd into save catalog"); 
		Category category = null;

		// parent request
		if (Objects.isNull(categoryRequest.getHierarchyLevel())) {
			log.info("Call for parent request");
			category = saveParent(categoryRequest);
		} else {
			// child request
			log.info("Call for child request");
			category = saveChild(categoryRequest);
		}
		categoryRepository.save(category);
		log.info("Exit from save catalog");
		return category;

	}

	/**
	 * @param categoryRequest method used to save child category
	 * @return category
	 */
	private Category saveChild(CategoryRequest categoryRequest) {
		log.info("Enterd into child request method");
		Category category = null;
		SubCategory subCategory = null;
		List<SubCategory> subCategoryRequestList = new ArrayList<>();
		String[] levelsHierarchy = categoryRequest.getHierarchyLevel().split("-");
		List<Integer> levels = new ArrayList<Integer>();
		int initialArraySize = 1;
		for (String a : levelsHierarchy) {
			// adding levels to an integer list
			levels.add(Integer.parseInt(a));
		}
		log.info("Adding the child element in the level " + levels.get(0));
		category = categoryRepository.findByLevelId(levels.get(0)).orElseThrow(
				() -> new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.BAD_REQUEST));

		if (levels.size() == initialArraySize) {
			subCategoryRequestList = category.getLevels();
			subCategoryRequestList.add(new SubCategory(getNewId(subCategoryRequestList), categoryRequest.getLevelName(),
					new ArrayList<>()));
			category.setLevels(subCategoryRequestList);
			return category;
		} else {
			if (Objects.isNull(category.getLevels()))
				throw new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.BAD_REQUEST);
			else {

				subCategory = category.getLevels().stream().filter(x -> x.getLevelId() == levels.get(1)).findFirst()
						.orElseThrow(() -> new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
								HttpStatus.BAD_REQUEST));
				subCategory = traverseLevels(1, subCategory, categoryRequest, levels);
			}
		}

		List<SubCategory> parentCatList = category.getLevels().stream().filter(x -> x.getLevelId() != levels.get(1))
				.collect(Collectors.toList());
		parentCatList.add(subCategory);
		category.setLevels(parentCatList);
		log.info("Exit from child request method");
		return category;

	}

	List<SubCategory> catList;
	List<SubCategory> temp = new ArrayList<>();
	List<SubCategory> tempList = new ArrayList<>();
	int counter = 0;

	/**
	 * @param level
	 * @param subCategory
	 * @param catgeoryRequest
	 * @param levels          recursive function to traverse through the inner
	 *                        levels and save child categories
	 * @return subCategory
	 */
	private SubCategory traverseLevels(int level, SubCategory subCategory, CategoryRequest catgeoryRequest,
			List<Integer> levels) {
		// adding n child levels
		log.info("Enterd into recursive method for child");
		level = level + 1;
		if (Objects.isNull(subCategory.getLevels())) {
			throw new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.BAD_REQUEST);
		} else {
			List<SubCategory> subcategoryList = subCategory.getLevels();
			if (level == levels.size()) {
				subcategoryList.add(
						new SubCategory(getNewId(subcategoryList), catgeoryRequest.getLevelName(), new ArrayList<>()));
				subCategory.setLevels(subcategoryList);
			} else {
				int currentLevel = level;
				log.info("Trying to add  the child element in the level " + levels.get(level));
				int levelId = levels.get(level);
				SubCategory iteratedVal = subcategoryList.stream().filter(t -> t.getLevelId() == levelId).findFirst()
						.orElseThrow(() -> new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
								HttpStatus.BAD_REQUEST));
				iteratedVal = traverseLevels(level, iteratedVal, catgeoryRequest, levels);
				subcategoryList = subcategoryList.stream().filter(x -> x.getLevelId() != levels.get(currentLevel))
						.collect(Collectors.toList());
				subcategoryList.add(iteratedVal);
				subCategory.setLevels(subcategoryList);
			}
			log.info("Exit from recursive method for child");
			return subCategory;
		}

	}

	/**
	 * @param categoryRequest method used to save parent category
	 * @return category
	 */
	private Category saveParent(CategoryRequest categoryRequest) {
		log.info("Enterd into parent request method");
		Category category = null;
		List<Category> listCategory = categoryRepository.findAll();
		category = new Category();
		// initially if hierarchy is null
		if (listCategory.isEmpty()) {
			category.setLevelId(1);
		} else {
			int nextLevelId = listCategory.get(listCategory.size() - 1).getLevelId() + 1;
			category.setLevelId(nextLevelId);
		}
		category.setLevelName(categoryRequest.getLevelName());
		category.setCreatedBy(categoryRequest.getCreatedBy());
		category.setCreatedTime(LocalDateTime.now());
		category.setLastUpdatedBy(categoryRequest.getLastUpdatedBy());
		category.setLastUpdatedTime(LocalDateTime.now());
		category.setLevels(new ArrayList<>());
		log.info("Exit from parent request method");
		return category;
	}

	/**
	 * @param subCategoryList method used to update level id based on the highest
	 *                        level id present
	 * @return
	 */
	private int getNewId(List<SubCategory> subCategoryList) {
		log.info("Generating id ");
		int newId = 1;
		if (subCategoryList.size() > 0) {
			List<Integer> idList = subCategoryList.stream().map(x -> x.getLevelId()).sorted()
					.collect(Collectors.toList());
			newId = idList.get(idList.size() - 1) + 1;
		}
		return newId;
	}

	/**
	 * Service method to fetch all categories
	 * 
	 * @return category list
	 * @exception ListEmptyException if the list is empty
	 */
	@Override
	public List<Category> fetchCategory() {
		log.info("Service class fetch category starts");
		List<Category> categoryList = categoryRepository.findAll();
		if (ObjectUtils.isEmpty(categoryList)) {
			throw new ListEmptyException(Constants.LIST_EMPTY, HttpStatus.BAD_REQUEST);
		} else {
			log.info("Service class fetch category ends with response: {}", categoryList);
			return categoryList;
		}
	}

	/**
	 *
	 * This is the update method of Category
	 * 
	 * @param categoryUpdateRequest
	 * @return category
	 * @throws HierrarchyNotFoundException ;if entered Hierarchy level is invalid
	 * 
	 */
	@Override
	public Category updateCatalog(CategoryUpdateRequest categoryUpdateRequest) {
		// updating the parent level

		if (categoryUpdateRequest.getHierarchyLevel() == null) {
			log.info("trying to update parent category by levelId" + categoryUpdateRequest.getLevelId());
			Category category = categoryRepository.findByLevelId(categoryUpdateRequest.getLevelId()).orElseThrow(
					() -> new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.NOT_FOUND));
			category.setLevelName(categoryUpdateRequest.getLevelName());
			category.setLastUpdatedBy(categoryUpdateRequest.getLastUpdatedBy());
			category.setLastUpdatedTime(LocalDateTime.now());
			categoryRepository.save(category);
			return category;

		} else {
			// updating the child level

			SubCategory subCategory = null;
			List<SubCategory> subCategoryRequestList = new ArrayList<>();
			String[] l = categoryUpdateRequest.getHierarchyLevel().split("-");
			List<Integer> levels = new ArrayList<Integer>();
			for (String a : l) {
				// adding levels to an integer list
				levels.add(Integer.parseInt(a));
			}
			Category category = categoryRepository.findByLevelId(categoryUpdateRequest.getLevelId()).get();

			if (levels.size() == 1) {
				log.info("trying to update the child level category by levelId" + categoryUpdateRequest.getLevelId());
				subCategoryRequestList = category.getLevels();
				SubCategory subcategoryLevel1 = subCategoryRequestList.stream()
						.filter(a -> a.getLevelId() == levels.get(0)).findFirst()
						.orElseThrow(() -> new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
								HttpStatus.NOT_FOUND));
				subcategoryLevel1.setLevelName(categoryUpdateRequest.getLevelName());
			} else {
				log.info("trying to update the child level category by levelId" + levels.get(0));
				subCategory = category.getLevels().stream().filter(x -> x.getLevelId() == levels.get(0)).findFirst()
						.orElseThrow(() -> new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
								HttpStatus.NOT_FOUND)); 

				subCategory = traverseUpdateLevels(1, subCategory, categoryUpdateRequest, levels);

			}
			category.setLastUpdatedBy(categoryUpdateRequest.getLastUpdatedBy());
			category.setLastUpdatedTime(LocalDateTime.now());
			categoryRepository.save(category);
			return category;
		}
	}

	// updating n child levels
	/**
	 * @param level
	 * @param subCategory
	 * @param categoryUpdateRequest
	 * @param levels
	 * @return subCategory
	 */
	private SubCategory traverseUpdateLevels(Integer level, SubCategory subCategory,
			CategoryUpdateRequest categoryUpdateRequest, List<Integer> levels) {

		if (level < levels.size()) {
			log.info("trying to update the  child level category by levelId" + levels.get(level));
			Integer levelId = levels.get(level);
			SubCategory iteratedVal = subCategory.getLevels().stream().filter(t -> t.getLevelId() == levelId)
					.findFirst().orElseThrow(() -> new HierrarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
							HttpStatus.NOT_FOUND));

			iteratedVal.setLevelName(categoryUpdateRequest.getLevelName());

			level++;
			traverseUpdateLevels(level, iteratedVal, categoryUpdateRequest, levels);

		}
		return subCategory;
	}
}