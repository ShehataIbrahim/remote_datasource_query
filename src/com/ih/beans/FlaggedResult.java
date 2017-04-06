package com.ih.beans;

public class FlaggedResult {
    private boolean flag;
    private String message;
    public FlaggedResult() {
        super();
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
