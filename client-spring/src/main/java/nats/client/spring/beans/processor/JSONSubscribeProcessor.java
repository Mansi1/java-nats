package nats.client.spring.beans.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nats.client.Message;
import nats.client.Nats;
import nats.client.spring.beans.annotations.subscribe.JSONSubscribe;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static nats.client.spring.beans.AnnotationConfigBeanPostProcessor.invoke;

@Component
public class JSONSubscribeProcessor implements IProcessor<JSONSubscribe> {


    private final Nats nats;
    private final ObjectMapper objectMapper;

    public JSONSubscribeProcessor(Nats nats, ObjectMapper objectMapper) {
        this.nats = nats;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Method method, JSONSubscribe annotation, Object bean, String beanName) {

        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 2 || !parameterTypes[0].equals(Message.class)) {
            throw new BeanInitializationException(String.format(
                    "Method '%s' on bean with name '%s' must have a single parameter of type %s when using the @%s annotation.",
                    method.toGenericString(),
                    beanName,
                    Message.class.getName(),
                    JSONSubscribe.class.getName()
            ));
        }
        nats.subscribe(annotation.value())
                .addMessageHandler(message -> invoke(method, objectMapper, bean, message, parameterTypes[1]));
    }


    @Override
    public JSONSubscribe getAnnotation(Method method) {
        return AnnotationUtils.findAnnotation(method, JSONSubscribe.class);
    }
}
