package com.peelocator.kira.peelocator.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("url.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }
}
