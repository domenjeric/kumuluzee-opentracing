package com.kumuluz.ee.opentracing.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * OpenTracing config
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OpenTracingConfig {
    public static final String CONFIG_PREFIX = "mp.opentracing.";

    @Inject
    @ConfigProperty(name = "kumuluzee.name")
    private Optional<String> serviceName;

    @Inject
    @ConfigProperty(name = CONFIG_PREFIX + "server.operation-name-provider")
    private Optional<String> operationNameProvider;

    @Inject
    @ConfigProperty(name = CONFIG_PREFIX + "server.skip-pattern")
    private Optional<String> skipPattern;

    public String getServiceName() {
        return serviceName.orElse("KumuluzEE project");
    }

    public String getSelectedOperationNameProvider() {
        return operationNameProvider.orElse("class-method");
    }

    public Pattern getSkipPattern() {
        return Pattern.compile(skipPattern.orElse(""));
    }
}
