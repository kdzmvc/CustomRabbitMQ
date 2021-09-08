package com.bfxy.rabbit.producer.broker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfxy.rabbit.api.Message;
import com.bfxy.rabbit.api.MessageProducer;
import com.bfxy.rabbit.api.MessageType;
import com.bfxy.rabbit.api.SendCallback;
import com.bfxy.rabbit.api.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;


/**
 * @author kongdz
 * @notes $ProducerClient 发送消息的实际实现类
 * @create 2021/5/31 14:14
 **/
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    private RabbitBroker mRabbitBroker;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                mRabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                mRabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                mRabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    /**
     * 	$send Messagetype 消息的批量发送
     */
    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {
        messages.forEach(message -> {
            message.setMessageType(MessageType.RAPID);
            MessageHolder.add(message);
        });
        mRabbitBroker.sendMessages();
    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {
        // TODO

    }

}
