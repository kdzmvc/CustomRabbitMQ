package com.bfxy.rabbit.common.serializer.impl;

import java.io.IOException;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfxy.rabbit.common.serializer.Serializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author kongdz
 * @notes $JacksonSerializer
 * @create 2021/5/31 14:14
 **/
public class JacksonSerializer implements Serializer {

    private static final Logger mLogger = LoggerFactory.getLogger(JacksonSerializer.class);
    private static final ObjectMapper mMapper = new ObjectMapper();
    private final JavaType mType;

    static {
        mMapper.disable(SerializationFeature.INDENT_OUTPUT); //不需要美化输出，转换格式化的json
        mMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //在遇到未知属性的时候不抛出异常。
        mMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true); //允许JSON字符串包含非引号控制字符
        mMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); //允许单引号来包住属性名称和字符串值
        mMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);//允许接受所有引号引起来的字符，使用‘反斜杠\’机制
        mMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true); //允许解析使用Java/C++ 样式的注释（包括'/'+'*' 和'//' 变量）
        mMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);//允许识别"Not-a-Number" (NaN)标识集合作为一个合法的浮点数
        mMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true); //允许JSON整数以多个0开始(比如，如果000001赋值给json某变量
        mMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); //允许使用非双引号属性名字
    }

    public static JacksonSerializer createParametricType(Class<?> clazz) {
        return new JacksonSerializer(mMapper.getTypeFactory().constructType(clazz));
    }

    public byte[] serializeRaw(Object data) {
        try {
            return mMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            mLogger.error("序列化出错", e);
        }
        return null;
    }

    public String serialize(Object data) {
        try {
            return mMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            mLogger.error("序列化出错", e);
        }
        return null;
    }

    public <T> T deserialize(String content) {
        try {
            return mMapper.readValue(content, mType);
        } catch (IOException e) {
            mLogger.error("反序列化出错", e);
        }
        return null;
    }

    public <T> T deserialize(byte[] content) {
        try {
            return mMapper.readValue(content, mType);
        } catch (IOException e) {
            mLogger.error("反序列化出错", e);
        }
        return null;
    }
    private JacksonSerializer(JavaType type) {
        this.mType = type;
    }

    public JacksonSerializer(Type type) {
        this.mType = mMapper.getTypeFactory().constructType(type);
    }
}