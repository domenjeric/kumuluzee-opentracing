package com.kumuluz.ee.opentracing.utils;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * OpenTracing util class
 * @author Domen Jeric
 * @since 1.0.0
 */
public abstract class OpenTracingUtil <T extends OpenTracingUtil<?>> {

    protected static final String OPENTRACING_SPAN_TITLE = "opentracing-span";

    public abstract void init();
    public abstract void startServiceSpan(ContainerRequestContext requestContext, String operationName);
    public abstract void finishServiceSpan(ContainerRequestContext requestContext);

}
