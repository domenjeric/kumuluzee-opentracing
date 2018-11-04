package com.kumuluz.ee.opentracing;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.*;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;

import java.util.logging.Logger;

/**
 * KumuluzEE OpenTracing Extension
 * @author Domen Jeric
 * @since 1.0.0
 */

@EeExtensionDef(name = "OpenTracing", group = EeExtensionGroup.LOGS)
@EeComponentDependencies({
    @EeComponentDependency(EeComponentType.JAX_RS)
})
public class OpenTracingExtension implements Extension {

    private static final Logger log = Logger.getLogger(OpenTracingExtension.class.getName());

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {
        log.info("Initializing OpenTracing Extension.");
    }

    @Override
    public void load() {
        log.info("OpenTracing Extension initialized.");
    }
}
