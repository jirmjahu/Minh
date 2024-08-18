package net.jirmjahu.minh.feature.extension;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExtensionManager {

    private static final Path EXTENSIONS_PATH = Path.of("extensions");
    private final List<Extension> loadedExtensions = new ArrayList<>();

    public ExtensionManager() {
        try {
            if (!Files.exists(EXTENSIONS_PATH)) {
                Files.createDirectory(EXTENSIONS_PATH);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create extensions directory", e);
        }
    }

    public void loadExtensions() {
        final File[] files = EXTENSIONS_PATH.toFile().listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) {
            return;
        }

        for (File file : files) {
            this.loadExtension(file);
        }
    }

    private void loadExtension(File file) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, getClass().getClassLoader())) {
            final ExtensionMeta meta = ExtensionConfigurationLoader.loadExtensionData(classLoader);
            final Class<?> clazz = Class.forName(meta.entrypoint(), true, classLoader);
            final Extension extension = (Extension) clazz.getDeclaredConstructor().newInstance();

            extension.onEnable();
            loadedExtensions.add(extension);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableExtensions() {
        for (Extension extension : loadedExtensions) {
            extension.onDisable();
        }
        loadedExtensions.clear();
    }

    public List<Extension> getLoadedExtensions() {
        return loadedExtensions;
    }
}
