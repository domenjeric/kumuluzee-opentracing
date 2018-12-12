package com.kumuluz.ee.opentracing;


import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class WebInfProcessor implements ApplicationArchiveProcessor {

    @Override
    public void process(Archive<?> archive, TestClass testClass) {
        WebArchive war = archive.as(WebArchive.class);
        war.addAsWebInfResource("WEB-INF/web.xml");
    }
}
