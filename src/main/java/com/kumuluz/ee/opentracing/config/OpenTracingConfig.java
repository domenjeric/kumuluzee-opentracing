package com.kumuluz.ee.opentracing.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

/**
 * OpenTracing config interface
 * @author Domen Jeric
 * @since 1.0.0
 */
public abstract class OpenTracingConfig <T extends OpenTracingConfig<?>> {

    private static final String DEFAULT_SERVICE_NAME = "KumuluzEE Project";

    public abstract String getReporterHost();
    public abstract int getReporterPort();

    public String getServiceName() {
        return ConfigurationUtil.getInstance()
                .get(OpenTracingConfigLoader.CONFIG_PREFIX + "service-name")
                .orElse(DEFAULT_SERVICE_NAME);
    }

    public String getSelectedOperationNameProvider() {
        return ConfigurationUtil.getInstance()
                .get(OpenTracingConfigLoader.CONFIG_PREFIX + "server.operation-name-provider")
                .orElse("class-method");
    }
}
