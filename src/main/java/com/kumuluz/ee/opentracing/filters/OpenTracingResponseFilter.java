package com.kumuluz.ee.opentracing.filters;


import com.kumuluz.ee.opentracing.config.JaegerTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfigLoader;
import com.kumuluz.ee.opentracing.utils.JaegerTracingUtil;
import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class OpenTracingResponseFilter implements ContainerResponseFilter {

    @Inject
    private OpenTracingConfigLoader config;

    @Inject
    private OpenTracingUtil<JaegerTracingUtil> jaegerTracer;

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        OpenTracingConfig<?> tracerConfig = config.getConfig();

        if (tracerConfig instanceof JaegerTracingConfig) {
            jaegerTracer.finishServiceSpan(requestContext);
        }
    }
}
