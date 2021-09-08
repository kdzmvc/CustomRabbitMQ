package com.bfxy.rabbit.producer.broker;

import java.util.List;

import com.bfxy.rabbit.api.Message;
import com.google.common.collect.Lists;

public class MessageHolder {

	private List<Message> mMessages = Lists.newArrayList();

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static final ThreadLocal<MessageHolder> mHolder = new ThreadLocal() {
		@Override
		protected Object initialValue() {
			return new MessageHolder();
		}
	};
	
	public static void add(Message message) {
		mHolder.get().mMessages.add(message);
	}
	
	public static List<Message> clear() {
		List<Message> tmp = Lists.newArrayList(mHolder.get().mMessages);
		mHolder.remove();
		return tmp;
	}
	
}
