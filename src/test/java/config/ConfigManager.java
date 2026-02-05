package config;

import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager handles environment-specific configuration loading.
 *
 * This class loads properties files based on the environment (dev, test, prod).
 * The environment is specified via the system property 'env' (defaults to 'dev').
 *
 * Example usage:
 *   String baseUrl = ConfigManager.get("baseUrl");
 *
 * The configuration files should be placed in src/test/resources/config/
 * with names like: dev.properties, test.properties, prod.properties
 */
public class ConfigManager {

    // Thread-safe properties storage
    private static final Properties props = new Properties();

    // Static block runs once when the class is first loaded
    static {
        // Get environment from system property, default to "dev"
        // Can be overridden with: mvn test -Denv=test
        String env = System.getProperty("env", "dev");
        String fileName = "config/" + env + ".properties";

        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }
            // Load properties from the file into memory
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Could not load config for env: " + env, e);
        }
    }

    /**
     * Retrieves a configuration value by key.
     *
     * @param key The property key to look up
     * @return The property value as a String, or null if not found
     */
    public static String get(String key) {
        return props.getProperty(key);
    }
}