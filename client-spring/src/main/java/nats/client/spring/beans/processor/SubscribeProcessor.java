package nats.client.spring.beans.processor;

import nats.client.Message;
import nats.client.Nats;
import nats.client.spring.beans.annotations.subscribe.Subscribe;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static nats.client.spring.beans.AnnotationConfigBeanPostProcessor.invoke;

@Component
public class SubscribeProcessor implements IProcessor<Subscribe> {


    private final Nats nats;

    public SubscribeProcessor(Nats nats) {
        this.nats = nats;
    }

    @Override
    public void process(Method method, Subscribe annotation, Object bean, String beanName) {

        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1 || !parameterTypes[0].equals(Message.class)) {
            throw new BeanInitializationException(String.format(
                    "Method '%s' on bean with name '%s' must have a single parameter of type %s when using the @%s annotation.",
                    method.toGenericString(),
                    beanName,
                    Message.class.getName(),
                    Subscribe.class.getName()
            ));
        }
        nats.subscribe(annotation.value()).addMessageHandler(message -> invoke(method, bean, message));
    }


    @Override
    public Subscribe getAnnotation(Method method) {
        return AnnotationUtils.findAnnotation(method, Subscribe.class);
    }
}
