package com.bfxy.rabbit.producer.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfxy.rabbit.producer.constant.BrokerMessageStatus;
import com.bfxy.rabbit.producer.entity.BrokerMessage;
import com.bfxy.rabbit.producer.mapper.BrokerMessageMapper;

@Service
public class MessageStoreService {

    @Autowired
    private BrokerMessageMapper mBrokerMessageMapper;

    public int insert(BrokerMessage brokerMessage) {
        return this.mBrokerMessageMapper.insert(brokerMessage);
    }

    public BrokerMessage selectByMessageId(String messageId) {
        return this.mBrokerMessageMapper.selectByPrimaryKey(messageId);
    }

    public void succuess(String messageId) {
        this.mBrokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_OK.getCode(),
                new Date());
    }

    public void failure(String messageId) {
        this.mBrokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_FAIL.getCode(),
                new Date());
    }

    public List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus) {
        return this.mBrokerMessageMapper.queryBrokerMessageStatus4Timeout(brokerMessageStatus.getCode());
    }

    public int updateTryCount(String brokerMessageId) {
        return this.mBrokerMessageMapper.update4TryCount(brokerMessageId, new Date());
    }


}
