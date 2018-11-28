package nats.client.spring.beans;

import nats.client.spring.beans.processor.IProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class AnnotationConfigBeanPostProcessor implements BeanPostProcessor {

    private final List<IProcessor> processors;

    public AnnotationConfigBeanPostProcessor(List<IProcessor> processorList) {
        this.processors = processorList;
    }

    public static void invoke(Method method, Object obj, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking subscription method", e);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        final Class<?> clazz = bean.getClass();
        for (final Method method : clazz.getMethods()) {
            for (final IProcessor processor : processors) {
                Object annotation = processor.getAnnotation(method);
                if (null != annotation) {
                    processor.process(method, annotation, bean, beanName);
                }
            }
        }
        return bean;
    }

}
