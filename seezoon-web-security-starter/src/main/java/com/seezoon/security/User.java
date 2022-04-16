package com.seezoon.security;

import java.io.Serializable;

public class User implements Serializable {

    private Serializable userId;

    public Serializable getUserId() {
        return userId;
    }

    public void setUserId(Serializable userId) {
        this.userId = userId;
    }
}
