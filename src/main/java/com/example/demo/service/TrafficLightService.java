package com.example.demo.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.TrafficLight;
import com.example.demo.model.TrafficLight.Color;
import com.example.demo.repository.TrafficLightRepository;

@Service
public class TrafficLightService {

	@Autowired
	private TrafficLightRepository trafficLightRepository;

	public ArrayList<TrafficLight> findAll() {
		Iterable<TrafficLight> it = trafficLightRepository.findAll();
		ArrayList<TrafficLight> trafficLights = new ArrayList<TrafficLight>();
		it.forEach(e -> trafficLights.add(e));
		return trafficLights;
	}

	public Long count() {
		return  trafficLightRepository.count();
	}

	public void deleteById(String id) {
		trafficLightRepository.deleteById(id);
	}
	
	public void deleteAll(){
		trafficLightRepository.deleteAll();
	}
	
	public void createTrafficLight(String id, Color color){
		TrafficLight trafficLight = new TrafficLight(id, color, false);
		trafficLightRepository.save(trafficLight);
	}
	
	public TrafficLight findById(String id){
		return trafficLightRepository.findById(id).orElse(null);
	}
	
	public void setColorById(String id, Color color){
		TrafficLight trafficLight = this.findById(id);
		trafficLight.setColor(color);
		trafficLightRepository.save(trafficLight);
	}
	
	public void setCycleStartedById(String id, boolean cycleStarted){
		TrafficLight trafficLight = this.findById(id);
		trafficLight.setCycleStarted(cycleStarted);
		trafficLightRepository.save(trafficLight);
	}
}
