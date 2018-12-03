package com.kumuluz.ee.opentracing;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeComponentDependencies;
import com.kumuluz.ee.common.dependencies.EeComponentDependency;
import com.kumuluz.ee.common.dependencies.EeComponentType;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * KumuluzEE OpenTracing Extension
 * @author Domen Jeric
 * @since 1.0.0
 */

@EeExtensionDef(name = "OpenTracing", group = "OpenTracing")
@EeComponentDependencies({
    @EeComponentDependency(EeComponentType.JAX_RS),
        @EeComponentDependency(EeComponentType.CDI)
})
public class OpenTracingExtension implements Extension {

    private static final Logger LOG = Logger.getLogger(OpenTracingExtension.class.getName());

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {
        LOG.info("Initializing OpenTracing Extension.");
    }

    @Override
    public void load() {}

    @Override
    public List<String> scanLibraries() {
        return Collections.singletonList("kumuluzee-opentracing-common");
    }
}
