package com.kumuluz.ee.opentracing.utils;

import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.UriInfo;
import java.util.regex.Pattern;

/**
 * Explicit tracing util
 * @author Domen Jeric
 * @since 1.0.0
 */
public class ExplicitTracingUtil {

    public static boolean tracingDisabled(ResourceInfo resourceInfo) {
        Traced tracedAnnotation =
                resourceInfo.getResourceClass().isAnnotationPresent(Traced.class) ?
                        resourceInfo.getResourceClass().getAnnotation(Traced.class) :
                        resourceInfo.getResourceMethod().getAnnotation(Traced.class);

        return tracedAnnotation != null && !tracedAnnotation.value();
    }

    public static boolean tracingDisabled(InvocationContext context) {
        Traced tracedAnnotation = getAnnotation(context);
        return tracedAnnotation != null && !tracedAnnotation.value();
    }

    public static Traced getAnnotation(InvocationContext context) {
        Class<?> clazz = context.getTarget().getClass().getSuperclass();
        return context.getMethod().isAnnotationPresent(Traced.class) ?
                        context.getMethod().getAnnotation(Traced.class) :
                        clazz.getAnnotation(Traced.class);
    }

    public static boolean pathMatchesSkipPattern(UriInfo uriInfo, Pattern skipPattern) {
        String path = uriInfo.getPath();

        if (path.charAt(0) != '/') {
            path = "/" + path;
        }

        return skipPattern.matcher(path).matches();
    }
}
