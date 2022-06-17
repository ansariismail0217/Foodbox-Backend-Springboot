package com.foodbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodbox.model.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

	@Query(value = "SELECT f FROM Food f where f.name=:foodItem")
	List<Food> findFoodListByName(@Param("foodItem") String foodItem);

}
