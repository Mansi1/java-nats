package nats.client.spring.beans.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nats.client.Message;
import nats.client.Nats;
import nats.client.spring.beans.annotations.subscribe.JSONSubscribeQueueGroup;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static nats.client.spring.beans.AnnotationConfigBeanPostProcessor.invoke;


@Component
public class JSONSubscribeQueueGroupProcessor implements IProcessor<JSONSubscribeQueueGroup> {


    private final Nats nats;
    private final ObjectMapper objectMapper;

    public JSONSubscribeQueueGroupProcessor(Nats nats, ObjectMapper objectMapper) {
        this.nats = nats;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Method method, JSONSubscribeQueueGroup annotation, Object bean, String beanName) {

        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 2 || !parameterTypes[0].equals(Message.class)) {
            throw new BeanInitializationException(String.format(
                    "Method '%s' on bean with name '%s' must have 2 parameter of type (%s, JSON CLASS) when using the @%s annotation.",
                    method.toGenericString(),
                    beanName,
                    Message.class.getName(),
                    JSONSubscribeQueueGroup.class.getName()
            ));
        }
        nats.subscribe(annotation.subject(), annotation.queueGroup())
                .addMessageHandler(message -> invoke(method, objectMapper, bean, message, parameterTypes[1]));
    }


    @Override
    public JSONSubscribeQueueGroup getAnnotation(Method method) {
        return AnnotationUtils.findAnnotation(method, JSONSubscribeQueueGroup.class);
    }
}
