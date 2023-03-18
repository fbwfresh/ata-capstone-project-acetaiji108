package com.kenzie.capstone.service.exceptions;

import java.util.HashMap;
import java.util.Map;

public class InvalidGameException extends RuntimeException {
    public InvalidGameException(String message){
        super(message);
    }
    public Map<String, Object> errorPayload() {
        Map<String, Object> errorPayload = new HashMap();
        errorPayload.put("errorType", "invalid_game");//changed from invalid_data to this
        errorPayload.put("message", this.getMessage());
        return errorPayload;
    }
}
