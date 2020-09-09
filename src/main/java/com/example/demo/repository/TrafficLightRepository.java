package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.TrafficLight;

@Repository
public interface TrafficLightRepository extends CrudRepository<TrafficLight, String> {}