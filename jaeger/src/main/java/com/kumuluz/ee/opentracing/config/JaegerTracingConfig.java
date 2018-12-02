package com.kumuluz.ee.opentracing.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

/**
 * Jaeger tracing config
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class JaegerTracingConfig implements OpenTracingConfigInterface {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5775;

    private String reporterHost;
    private int reporterPort;

    private static final Logger LOG = Logger.getLogger(JaegerTracingConfig.class.getName());

    @PostConstruct
    public void init() {
        this.setReporterHost().setReporterPort();
        LOG.info(String.format("Jaeger config loaded: %s:%d", this.getReporterHost(), this.getReporterPort()));
    }

    @Override
    public String getReporterHost() {
        return reporterHost;
    }

    private JaegerTracingConfig setReporterHost() {
        this.reporterHost = ConfigurationUtil.getInstance()
                .get(OpenTracingConfig.CONFIG_PREFIX + "reporter_host")
                .orElse(DEFAULT_HOST);

        return this;
    }

    @Override
    public int getReporterPort() {
        return reporterPort;
    }

    private JaegerTracingConfig setReporterPort() {
        this.reporterPort =  ConfigurationUtil.getInstance()
                .getInteger(OpenTracingConfig.CONFIG_PREFIX + "reporter_port")
                .orElse(DEFAULT_PORT);

        return this;
    }
}
