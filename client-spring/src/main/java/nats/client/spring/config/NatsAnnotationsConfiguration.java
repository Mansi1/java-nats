/*
 *   Copyright (c) 2013 Intellectual Reserve, Inc.  All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package nats.client.spring.config;

import nats.client.spring.EnableNatsAnnotations;
import nats.client.spring.beans.annotations.subscribe.Subscribe;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * Java config for providing {@link Subscribe @Subscribe} support.
 *
 * @author Mike Heath
 */
@Configuration
public class NatsAnnotationsConfiguration implements ImportAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private String natsRef;


    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        final Map<String, Object> attributes = importMetadata.getAnnotationAttributes(EnableNatsAnnotations.class.getName());
        natsRef = (String) attributes.get("value");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
