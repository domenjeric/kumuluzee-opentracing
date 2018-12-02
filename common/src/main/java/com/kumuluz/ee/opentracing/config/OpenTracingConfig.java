package com.kumuluz.ee.opentracing.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.enterprise.context.ApplicationScoped;
import java.util.regex.Pattern;

/**
 * OpenTracing config
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OpenTracingConfig {

    private static final String DEFAULT_SERVICE_NAME = "KumuluzEE Project";
    public static final String CONFIG_PREFIX = "kumuluzee.opentracing.";

    public String getServiceName() {
        return ConfigurationUtil.getInstance()
                .get("kumuluzee.name")
                .orElse(DEFAULT_SERVICE_NAME);
    }

    public String getSelectedOperationNameProvider() {
        return ConfigurationUtil.getInstance()
                .get(CONFIG_PREFIX + "server.operation-name-provider")
                .orElse("class-method");
    }

    public Pattern getSkipPattern() {
        String skipPattern = ConfigurationUtil.getInstance()
                .get(CONFIG_PREFIX + "server.skip-pattern")
                .orElse(null);

        return skipPattern != null ? Pattern.compile(skipPattern) : null;
    }
}
