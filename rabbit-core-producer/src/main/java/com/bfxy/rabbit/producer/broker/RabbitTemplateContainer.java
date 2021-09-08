package com.bfxy.rabbit.producer.broker;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.bfxy.rabbit.api.Message;
import com.bfxy.rabbit.api.MessageType;
import com.bfxy.rabbit.api.exception.MessageRunTimeException;
import com.bfxy.rabbit.common.convert.GenericMessageConverter;
import com.bfxy.rabbit.common.convert.RabbitMessageConverter;
import com.bfxy.rabbit.common.serializer.Serializer;
import com.bfxy.rabbit.common.serializer.SerializerFactory;
import com.bfxy.rabbit.common.serializer.impl.JacksonSerializerFactory;
import com.bfxy.rabbit.producer.service.MessageStoreService;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;


/**
 * @author kongdz
 * @notes $RabbitTemplateContainer池化封装
 * 		1.提高发送的效率
 * 		2.可以根据不同的需求制定化不同的RabbitTemplate, 比如每一个topic 都有自己的routingKey规则
 * @create 2021/5/31 14:14
 **/
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

	private Map<String /* TOPIC */, RabbitTemplate> mRabbitMap = Maps.newConcurrentMap();
	
	private Splitter mSplitter = Splitter.on("#");
	
	private SerializerFactory mSerializerFactory = JacksonSerializerFactory.mInstance;
	
	@Autowired
	private ConnectionFactory mConnectionFactory;
	
	@Autowired
	private MessageStoreService mMessageStoreService;
	
	public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
		Preconditions.checkNotNull(message);
		String topic = message.getTopic();
		RabbitTemplate rabbitTemplate = mRabbitMap.get(topic);
		if(rabbitTemplate != null) {
			return rabbitTemplate;
		}
		log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);
		
		RabbitTemplate newTemplate = new RabbitTemplate(mConnectionFactory);
		newTemplate.setExchange(topic);
		newTemplate.setRoutingKey(message.getRoutingKey());
		newTemplate.setRetryTemplate(new RetryTemplate());
		
		//	添加序列化，反序列化和converter对象
		Serializer serializer = mSerializerFactory.create();
		GenericMessageConverter gmc = new GenericMessageConverter(serializer);
		RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
		newTemplate.setMessageConverter(rmc);
		String messageType = message.getMessageType();
		//如果不是迅速消息类型，则需要设置callBack
		if(!MessageType.RAPID.equals(messageType)) {
			newTemplate.setConfirmCallback(this);
		}
		mRabbitMap.putIfAbsent(topic, newTemplate);
		return mRabbitMap.get(topic);
	}

	/**
	 * 	无论是confirm消息还是reliant消息，发送消息以后，broker都会去回调confirm
	 * 	迅速消息不需要确认
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		// 	具体的消息应答
		List<String> strings = mSplitter.splitToList(correlationData.getId());

		String messageId = strings.get(0);
		long sendTime = Long.parseLong(strings.get(1));
		String messageType = strings.get(2);
		if(ack) {
			//	当broker返回ACK成功时, 更新日志表里对应的消息发送状态为SEND_OK
			
			// 	如果当前消息类型为reliant,数据库查找并进行更新
			if(MessageType.RELIANT.endsWith(messageType)) {
				this.mMessageStoreService.succuess(messageId);
			}
			log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
		} else {
			this.mMessageStoreService.failure(messageId);
			log.error("send message is Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);

		}
	}
}
