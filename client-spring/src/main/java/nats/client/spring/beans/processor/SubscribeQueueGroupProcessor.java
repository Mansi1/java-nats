package nats.client.spring.beans.processor;

import nats.client.Message;
import nats.client.Nats;
import nats.client.spring.beans.annotations.subscribe.SubscribeQueueGroup;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static nats.client.spring.beans.AnnotationConfigBeanPostProcessor.invoke;

@Component
public class SubscribeQueueGroupProcessor implements IProcessor<SubscribeQueueGroup> {


    private final Nats nats;

    public SubscribeQueueGroupProcessor(Nats nats) {
        this.nats = nats;
    }

    @Override
    public void process(Method method, SubscribeQueueGroup annotation, Object bean, String beanName) {

        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1 || !parameterTypes[0].equals(Message.class)) {
            throw new BeanInitializationException(String.format(
                    "Method '%s' on bean with name '%s' must have a single parameter of type %s when using the @%s annotation.",
                    method.toGenericString(),
                    beanName,
                    Message.class.getName(),
                    SubscribeQueueGroup.class.getName()
            ));
        }
        nats.subscribe(annotation.subject(), annotation.queueGroup())
                .addMessageHandler(message -> invoke(method, bean, message));
    }


    @Override
    public SubscribeQueueGroup getAnnotation(Method method) {
        return AnnotationUtils.findAnnotation(method, SubscribeQueueGroup.class);
    }
}
