package com.bfxy.rabbit.api;

/**
 * @author kongdz
 * @notes $MessageListener 消费者监听消息
 * @create 2021/5/18 21:33
 **/
public interface MessageListener {

    void onMessage(Message message);

}
