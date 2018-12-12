# KumuluzEE OpenTracing

> KumuluzEE OpenTracing extension provides OpenTracing instrumentation for JAX-RS. 
Implementation is compliant with MicroProfile OpenTracing specification.

KumuluzEE OpenTracing extension provides an easy way to take advantage of distributed tracing in KumuluzEE projects. 


## Usage 
You can enable KumuluzEE OpenTracing extension by adding the following dependency. Currently only Jaeger tracing is supported.
```xml
<dependency>
    <groupId>com.kumuluz.ee.opentracing</groupId>
    <artifactId>kumuluzee-opentracing-jaeger</artifactId>
    <version>${kumuluzee-opentracing.version}</version>
</dependency>
```
CDI and JAX-RS dependencies are prerequisites. 
Please refer to [KumuluzEE readme]( https://github.com/kumuluz/kumuluzee/) for more information.

## Configuration
Here is an example configuration.
```yaml
mp:
  opentracing:
    reporter-host: localhost
    reporter-port: 5775
    server:
      operation-name-provider: http-path
      skip-pattern: opentracing
```

### Tracing with no code instrumentation
Tracing is automatically enabled by adding KumuluzEE OpenTracing extension dependency.

### Tracing with explicit code instrumentation
There is `@Traced` annotation available to define explicit Span creation. 
`@Traced` annotation can be added to class or method.\
Tracing in JAX-RS resource classes is enabled by default. 
It can be disabled by adding `@Traced(false)` annotation 
to class or method.

### Accessing configured tracer
The configured tracer object can be accessed by injecting Tracer class:
```java
@Inject
io.opentracing.Tracer configuredTracer;
```


## Tracing client requests
To enable JAX-RS client tracing, client filters should be 
added:
```java
Client httpClient = ClientBuilder.newBuilder()
                .register(OpenTracingClientRequestFilter.class)
                .register(OpenTracingClientResponseFilter.class)
                .build();
```





For more in-depth specification and configuration options
please refer to [MicroProfile-OpenTracing (MP-OT)](https://github.com/eclipse/microprofile-opentracing).



