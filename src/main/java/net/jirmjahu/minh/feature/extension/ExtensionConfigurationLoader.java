package net.jirmjahu.minh.feature.extension;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLClassLoader;

public class ExtensionConfigurationLoader {

    private static final Gson GSON = new GsonBuilder().create();

    public static ExtensionMeta loadExtensionData(URLClassLoader classLoader) {
        final InputStream inputStream = classLoader.getResourceAsStream("extension.json");
        //if the extension.json file was not found
        if (inputStream == null) {
            try {
                throw new IOException("The 'extension.json' was not found");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return GSON.fromJson(reader, ExtensionMeta.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
