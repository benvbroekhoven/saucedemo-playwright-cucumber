package config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("env", "dev");
        String fileName = "config/" + env + ".properties";
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Could not load config for env: " + env, e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
