package controllers.logviewer.support;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.logviewer.services.LogConfig;
import org.logviewer.utility.ExecutorDefault;
import org.logviewer.utility.LogConfigProperties;

public class WebSocketLogConfig implements LogConfig {
    
    private final LogConfigProperties properties = new LogConfigProperties();
    private final ExecutorService executorService = new ExecutorDefault(properties);

    @Override
    public Executor getExecutor() {
        return executorService;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}