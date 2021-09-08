package com.bfxy.rabbit.common.serializer;

/**
 * @author kongdz
 * @notes 序列化和反序列化的接口
 * @create 2021/5/31 14:14
 **/
public interface Serializer {

    byte[] serializeRaw(Object data);

    String serialize(Object data);

    <T> T deserialize(String content);

    <T> T deserialize(byte[] content);

}
