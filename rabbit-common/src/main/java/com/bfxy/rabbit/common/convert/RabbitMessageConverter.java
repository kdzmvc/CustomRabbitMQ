package com.bfxy.rabbit.common.convert;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import com.google.common.base.Preconditions;


/**
 * @author kongdz
 * @notes $RabbitMessageConverter
 * @create 2021/5/31 14:14
 **/
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;

//	private final String delaultExprie = String.valueOf(24 * 60 * 60 * 1000);

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    @Override
    public org.springframework.amqp.core.Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
//		messageProperties.setExpiration(delaultExprie); 设置过期时间
        com.bfxy.rabbit.api.Message message = (com.bfxy.rabbit.api.Message) object;
        messageProperties.setDelay(message.getDelayMills());
        return this.delegate.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(org.springframework.amqp.core.Message message) throws MessageConversionException {
        return (com.bfxy.rabbit.api.Message)this.delegate.fromMessage(message);
    }

}
