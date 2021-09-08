package com.bfxy.rabbit.producer.broker;

import com.bfxy.rabbit.api.Message;


/**
 * @author kongdz
 * @notes $RabbitBroker 具体发送不同种类型消息的接口
 * @create 2021/5/31 14:14
 **/
public interface RabbitBroker {
	
	void rapidSend(Message message);
	
	void confirmSend(Message message);
	
	void reliantSend(Message message);
	
	void sendMessages();
	
}
