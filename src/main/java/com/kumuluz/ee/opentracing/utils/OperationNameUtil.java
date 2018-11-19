package com.kumuluz.ee.opentracing.utils;

import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfigLoader;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;

/**
 * Server Span name util
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OperationNameUtil {

    @Inject
    OpenTracingConfigLoader config;

    public String operationName(ContainerRequestContext requestContext, ResourceInfo resourceInfo) {
        OpenTracingConfig<?> tracerConfig = config.getConfig();

        if (tracerConfig == null) {
            return null;
        }

        String operationName = tracerConfig.getSelectedOperationNameProvider();

        if (operationName.equals("class-method")) {
            return this.operationNameClassMethod(requestContext, resourceInfo);
        }

        return this.operationNameHttpPath(requestContext);
    }

    private String operationNameClassMethod(ContainerRequestContext requestContext, ResourceInfo resourceInfo) {
        return requestContext.getMethod() + ": " +
                resourceInfo.getResourceClass().getName()
                + "." +
                resourceInfo.getResourceMethod().getName();
    }


    private String operationNameHttpPath(ContainerRequestContext requestContext) {
        return requestContext.getMethod() + ": /" + requestContext.getUriInfo().getPath();
    }

}
