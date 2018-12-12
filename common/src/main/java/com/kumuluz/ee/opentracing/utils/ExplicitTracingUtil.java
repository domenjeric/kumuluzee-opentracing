package com.kumuluz.ee.opentracing.utils;

import org.eclipse.microprofile.opentracing.Traced;

import javax.interceptor.InvocationContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Explicit tracing util
 * @author Domen Jeric
 * @since 1.0.0
 */
public class ExplicitTracingUtil {

    public static boolean tracingDisabled(ResourceInfo resourceInfo) {
        Traced tracedAnnotation =
                resourceInfo.getResourceMethod().isAnnotationPresent(Traced.class) ?
                        resourceInfo.getResourceMethod().getAnnotation(Traced.class) :
                        resourceInfo.getResourceClass().getAnnotation(Traced.class);

        return tracedAnnotation != null && !tracedAnnotation.value();
    }

    public static boolean tracingDisabled(InvocationContext context) {
        Traced tracedAnnotation = getAnnotation(context);
        return tracedAnnotation != null && !tracedAnnotation.value();
    }

    public static Traced getAnnotation(InvocationContext context) {
        Class<?> clazz = context.getTarget().getClass().getSuperclass();
        return getAnnotation(clazz, context.getMethod());
    }

    public static Traced getAnnotation(Class<?> clazz, Method method) {
        return method.isAnnotationPresent(Traced.class) ?
                method.getAnnotation(Traced.class) :
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
