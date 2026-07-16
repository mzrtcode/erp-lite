package com.mzrt.erp_lite.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public final class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(@Nullable String name,
                                                  EncodedResource resource) throws IOException {

        var factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());

        Properties properties = Objects.requireNonNull(factory.getObject(),
                "No se pudieron cargar propiedades YAML desde " + resource);

        String sourceName = name != null
                ? name
                : Objects.requireNonNull(resource.getResource().getFilename());

        return new PropertiesPropertySource(sourceName, properties);
    }
}
