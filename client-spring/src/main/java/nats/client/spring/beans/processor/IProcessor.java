package nats.client.spring.beans.processor;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;

import java.lang.reflect.Method;

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public interface IProcessor<ANNOTATION> {


    void process(Method method, ANNOTATION annotation, Object bean, String beanName);

    ANNOTATION getAnnotation(Method method);


}
