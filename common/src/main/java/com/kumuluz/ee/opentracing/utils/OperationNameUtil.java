package com.kumuluz.ee.opentracing.utils;

import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import java.lang.reflect.Method;

/**
 * Server Span name util
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OperationNameUtil {

    @Inject
    OpenTracingConfig tracerConfig;

    public String operationName(ContainerRequestContext requestContext, ResourceInfo resourceInfo) {
        return this.operationName(requestContext, resourceInfo.getResourceClass(), resourceInfo.getResourceMethod());
    }

    public String operationNameExplicitTracing(ContainerRequestContext requestContext, InvocationContext context) {
        Class<?> clazz = context.getTarget().getClass().getSuperclass();
        Method method = context.getMethod();

        Traced tracedAnnotation = ExplicitTracingUtil.getAnnotation(context);

        if (tracedAnnotation != null && !tracedAnnotation.operationName().equals("")) {
            return tracedAnnotation.operationName();
        }

        if (clazz.isAnnotationPresent(Path.class) || method.isAnnotationPresent(Path.class)) {
            return this.operationName(requestContext, clazz, method);
        }

        return clazz.getName() + "." + method.getName();
    }

    private String operationName(ContainerRequestContext requestContext, Class<?> clazz, Method method) {
        String operationNameProvider = this.operationNameProvider();

        Traced tracedAnnotation = ExplicitTracingUtil.getAnnotation(clazz, method);

        if (tracedAnnotation != null && !tracedAnnotation.operationName().equals("")) {
            return tracedAnnotation.operationName();
        }

        if (operationNameProvider != null && operationNameProvider.equals("class-method")) {
            return this.operationNameClassMethod(requestContext, clazz, method);
        }

        return this.operationNameHttpPath(requestContext);
    }

    private String operationNameProvider() {
        return tracerConfig.getSelectedOperationNameProvider();
    }

    private String operationNameClassMethod(ContainerRequestContext requestContext, Class<?> clazz, Method method) {
        return requestContext.getMethod() + ":" + clazz.getName() + "." + method.getName();
    }


    private String operationNameHttpPath(ContainerRequestContext requestContext) {
        return requestContext.getMethod() + ":/" + requestContext.getUriInfo().getPath();
    }

}
