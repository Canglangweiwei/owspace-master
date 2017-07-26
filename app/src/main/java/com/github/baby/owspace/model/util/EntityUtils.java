package com.github.baby.owspace.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

@SuppressWarnings("ALL")
public final class EntityUtils {

    private EntityUtils() {
        super();
    }

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
            .create();
}
