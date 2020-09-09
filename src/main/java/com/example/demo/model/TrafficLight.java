package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("TrafficLight")
public class TrafficLight {
	
	public enum Color { 
        RED, YELLOW, GREEN
    }

	@Id private String id;
    private Color color;
    private boolean cycleStarted;
}
