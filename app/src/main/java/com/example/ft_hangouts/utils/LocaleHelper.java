package com.example.ft_hangouts.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleHelper {
    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();

        conf.setLocale(locale);
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

    public static LocaleList getLocale(Context context) {
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        return conf.getLocales();
    }
}
