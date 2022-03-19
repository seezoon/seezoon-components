package com.seezoon.web.i18n;

import java.util.Locale;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

public class LocaleFactory {

    public static Locale getDefaultLocale() {
        return Locale.SIMPLIFIED_CHINESE;
    }

    public static Locale create(String locale) {
        return StringUtils.isEmpty(locale) ? getDefaultLocale() : LocaleUtils.toLocale(locale);
    }
}
