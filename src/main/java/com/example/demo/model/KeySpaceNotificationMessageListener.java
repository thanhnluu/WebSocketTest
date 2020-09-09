package com.example.demo.model;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.example.demo.config.RedisConfig;
import com.example.demo.model.TrafficLight.Color;
import com.example.demo.service.TrafficLightService;

@Component
public class KeySpaceNotificationMessageListener implements MessageListener {

	@Autowired
	RedisConfig redisConfig;

	@Autowired
	private TrafficLightService trafficLightService;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@PostConstruct
	public void init()
	{
		redisConfig.setKeySpaceNotificationMessageListener(this);
	}

	@Override
	public void onMessage(Message message, byte[] pattern)
	{
		String action = new String(message.getBody());
		String keyInfo = new String(message.getChannel());
		
		if(action.equals("hset")){
			String [] splitKeyInfo = keyInfo.split(":");
			String trafficId = splitKeyInfo[splitKeyInfo.length - 1];
			Color newColor = trafficLightService.findById(trafficId).getColor();
			System.out.println(String.format("Value of key (%s) changed to %s", trafficId, newColor));
			// sends a message to clients suscribed to that particular traffic light id
			simpMessagingTemplate.convertAndSend("/topic/" + trafficId, newColor);
		}
		
	}

}
