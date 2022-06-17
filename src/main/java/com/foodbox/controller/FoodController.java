package com.foodbox.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodbox.exception.ResourceNotFoundException;
import com.foodbox.model.Food;
import com.foodbox.repository.FoodRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/")
public class FoodController {

	@Autowired
	private FoodRepository foodRepository;
	
	
	@GetMapping("/searchFoodName")
	public ResponseEntity<Object> getSearchData(@RequestParam(name ="foodItem",required = false) String foodItem){
		List<Food> foodList = foodRepository.findFoodListByName(foodItem);
		if (foodList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(foodList, HttpStatus.OK); 
		}
		
	}

	@GetMapping("/foods")
	public ResponseEntity<List<Food>> getAllFoods()
			 {
		try {
			List<Food> foods = new ArrayList<Food>();
			
				foodRepository.findAll().forEach(foods::add);
			
			if (foods.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(foods, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// get food by id rest api
	@GetMapping("/foods/{id}")
	public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
		Food food = foodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food does not exist with id :" + id));
		return ResponseEntity.ok(food);
	}

	// create food rest api
	@PostMapping("/foods")
	public Food createEmployee(@RequestBody Food food) {
		return foodRepository.save(food);
	}

	// update food rest api
	@PutMapping("/foods/{id}")
	public ResponseEntity<Food> updateFood(@PathVariable Long id, @RequestBody Food foodDetails) {
		Food food = foodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food does not exist with id :" + id));

		food.setName(foodDetails.getName());
		food.setPrice(foodDetails.getPrice());
		food.setStar(foodDetails.getStar());
		food.setCookTime(foodDetails.getCookTime());

		Food updatedFood = foodRepository.save(food);
		return ResponseEntity.ok(updatedFood);
	}

	// delete employee rest api
	@DeleteMapping("/foods/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteFood(@PathVariable Long id) {
		Food employee = foodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food does not exist with id :" + id));

		foodRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
