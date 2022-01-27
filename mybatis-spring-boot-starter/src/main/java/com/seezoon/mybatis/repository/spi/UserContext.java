package com.seezoon.mybatis.repository.spi;

/**
 * 用户上下文 如有需要SPI注入
 */
public interface UserContext {

    default Object getUserId() {
        return null;
    }
}

class DefaultUserContext implements UserContext {

}