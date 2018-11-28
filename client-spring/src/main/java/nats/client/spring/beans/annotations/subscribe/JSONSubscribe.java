package nats.client.spring.beans.annotations.subscribe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JSONSubscribe {
    /**
     * The NATS subject to subscribe to.
     *
     * @return the NATS subject.
     */
    String value();

}
