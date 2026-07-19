package com.mzrt.erp_lite.config;

import com.mzrt.erp_lite.adapter.out.persistence.aws.model.AwsModelConfig;
import com.mzrt.erp_lite.adapter.out.persistence.jsonplaceholder.model.JsonPlaceholderConfigModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties({AwsModelConfig.class, JsonPlaceholderConfigModel.class})
@PropertySource(value = "classpath:aws.yaml", factory = YamlPropertySourceFactory.class)
@PropertySource(value = "classpath:jsonplaceholder.yaml", factory = YamlPropertySourceFactory.class)
public class YamlConfig {

}
