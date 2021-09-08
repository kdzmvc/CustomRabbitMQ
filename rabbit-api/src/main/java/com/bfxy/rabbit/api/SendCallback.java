package com.bfxy.rabbit.api;

/**
 * @author kongdz
 * @notes  $SendCallback 回调函数处理
 * @create 2021/5/18 21:33
 **/
public interface SendCallback {

	void onSuccess();
	
	void onFailure();
	
}
