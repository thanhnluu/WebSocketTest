package com.example.demo.config;

import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.example.demo.model.KeySpaceNotificationMessageListener;

@Configuration
@EnableCaching
public class RedisConfig {

	KeySpaceNotificationMessageListener keySpaceNotificationMessageListener;


	@Value("${redis.hostname}")
	private String redisHostName;

	@Value("${redis.port}")
	private int redisPort;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory
		= new JedisConnectionFactory();
		jedisConFactory.setHostName(this.redisHostName);
		jedisConFactory.setPort(this.redisPort);
		return jedisConFactory;
	}

	@Bean
	MessageListenerAdapter messageListener() {
		return new MessageListenerAdapter(keySpaceNotificationMessageListener);
	}

	@Bean
	RedisMessageListenerContainer redisContainer(JedisConnectionFactory jedisConnectionFactory) {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory);
		container.addMessageListener(messageListener(), new PatternTopic("__keyspace@*:*"));
		container.setTaskExecutor(Executors.newFixedThreadPool(4));
		return container;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

	public void setKeySpaceNotificationMessageListener(KeySpaceNotificationMessageListener keySpaceNotificationMessageListener)
	{
		this.keySpaceNotificationMessageListener = keySpaceNotificationMessageListener;
	}
}