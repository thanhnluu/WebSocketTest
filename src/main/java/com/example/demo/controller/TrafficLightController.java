package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.TrafficLight;
import com.example.demo.model.TrafficLight.Color;
import com.example.demo.service.TrafficLightService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/trafficlight")
public class TrafficLightController {

	@Autowired
	private TrafficLightService trafficLightService;

	@GetMapping(path = "/{id}")
	public ResponseEntity<TrafficLight> getTrafficLightById(@PathVariable("id") String id){
		TrafficLight trafficLight = trafficLightService.findById(id);
		if(trafficLight == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);	
		return new ResponseEntity<>(trafficLight, HttpStatus.OK);
	}

	@GetMapping(path = "/trafficlights")
	public ResponseEntity<ArrayList<TrafficLight>> getAllTrafficLights(){
		ArrayList<TrafficLight> trafficLights = trafficLightService.findAll();
		return new ResponseEntity<>(trafficLights, HttpStatus.OK);
	}

	@GetMapping(path = "/count")
	public ResponseEntity<Long> getCount(){	
		return new ResponseEntity<>(trafficLightService.count(), HttpStatus.OK);
	}
	
	@DeleteMapping()
	public ResponseEntity<String> deleteAll(){
		trafficLightService.deleteAll();
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteTrafficLightById(@PathVariable("id") String id){
		TrafficLight trafficLight = trafficLightService.findById(id);
		if(trafficLight == null)
			return new ResponseEntity<>(String.format("Traffic light with id %s does not exist",id), HttpStatus.NOT_FOUND);
		trafficLightService.deleteById(id);	
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<String> createTrafficLight(@RequestBody TrafficLight trafficLight){
		String id = trafficLight.getId();
		Color color = trafficLight.getColor();
		trafficLightService.createTrafficLight(id, color);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@PostMapping(path = "/start/{id}")
	public ResponseEntity<String> startLightCycle(@PathVariable("id") String id){
		TrafficLight trafficLight = trafficLightService.findById(id);
		if(trafficLight == null)
			return new ResponseEntity<>(String.format("Traffic light with id %s does not exist",id), HttpStatus.NOT_FOUND);
		if(trafficLight.isCycleStarted())
			return new ResponseEntity<>("Cycle already started", HttpStatus.OK);

		trafficLightService.setCycleStartedById(id, true);
		// simulates a job with changing statuses
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			int iterations = 0;
			@Override
			public void run() {
				iterations++;
				if (iterations > 3){
					trafficLightService.setCycleStartedById(id, false);
					timer.cancel();
					return;
				}
				TrafficLight trafficLight = trafficLightService.findById(id);	
				Color trafficLightColor = trafficLight.getColor();
				switch(trafficLightColor){
				case RED:
					trafficLightService.setColorById(id, Color.GREEN);
					break;
				case YELLOW:
					trafficLightService.setColorById(id, Color.RED);
					break;
				case GREEN:
					trafficLightService.setColorById(id, Color.YELLOW);
					break;
				}
			}
		}, 0, 5000);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}



}
