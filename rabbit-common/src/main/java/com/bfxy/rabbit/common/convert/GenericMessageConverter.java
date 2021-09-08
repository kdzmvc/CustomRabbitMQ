package com.bfxy.rabbit.common.convert;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import com.bfxy.rabbit.common.serializer.Serializer;
import com.google.common.base.Preconditions;


/**
 * @author kongdz
 * @notes $GenericMessageConverter
 * @create 2021/5/31 14:14
 **/
public class GenericMessageConverter implements MessageConverter {

    private Serializer mSerializer;

    public GenericMessageConverter(Serializer serializer) {
        Preconditions.checkNotNull(serializer);
        this.mSerializer = serializer;
    }

    @Override
    public org.springframework.amqp.core.Message toMessage(Object object, MessageProperties messageProperties)
            throws MessageConversionException {
        return new org.springframework.amqp.core.Message(this.mSerializer.serializeRaw(object), messageProperties);
    }

    @Override
    public Object fromMessage(org.springframework.amqp.core.Message message) throws MessageConversionException {
        return this.mSerializer.deserialize(message.getBody());
    }


}
