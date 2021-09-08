package com.bfxy.rabbit.api.exception;

/**
 * @author kongdz
 * @notes $MessageException
 * @create 2021/5/18 21:33
 **/
public class MessageException extends Exception {

    private static final long serialVersionUID = 6347951066190728758L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

}
