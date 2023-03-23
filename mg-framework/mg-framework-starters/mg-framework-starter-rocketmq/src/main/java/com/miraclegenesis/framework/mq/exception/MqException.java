package com.miraclegenesis.framework.mq.exception;

/**
 * @author robert
 */
public class MqException extends RuntimeException {

    public MqException(Exception e) {
        super(e);
    }

    public MqException(String message) {
        super(new RuntimeException(message));
    }

    public MqException(String message, Exception e) {
        super(new Exception(message, e));
    }


}
