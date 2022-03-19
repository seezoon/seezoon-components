package com.seezoon.web.i18n;

import com.seezoon.web.component.SpringContextHolder;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 如果没有session 上下文的话，LocaleContextHolder.getLocale() 取的系统默认，找不到资源就报错
 *
 * 如果项目需要国际化，需要显示配置，框架内部配置这个参数不生效
 * spring.messages.basename=i18n/seezoon,i18n/messages
 */
public class Message {

    static MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);

    /**
     * 适合有session 上下文的
     *
     * @param key
     * @param args
     * @return
     */
    public static String get(String key, Object... args) {
        final Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }


    public static String getWithDefault(String key, String defaultMessage, Object... args) {
        final Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, defaultMessage, locale);
    }

    public static String get(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }
}
