package com.kumuluz.ee.opentracing.utils;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Request context holder
 * @author Domen Jeric
 * @since 1.0.0
 */
public class RequestContextHolder {

    private static ThreadLocal<ContainerRequestContext> requestContextHolder = new ThreadLocal<>();

    public static void setRequestContextHolder(ContainerRequestContext requestContext) {
        requestContextHolder.set(requestContext);
    }

    public static ContainerRequestContext getRequestContext() {
        return requestContextHolder.get();
    }
}
