package com.groupon.getaways.persistence;

import com.groupon.getaways.annotations.Persist;
import com.groupon.getaways.util.Parameter;
import com.groupon.getaways.util.Reflection;
import org.redisson.RedissonClient;
import org.redisson.core.RSet;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class RedisHandler {

    public static Object invoke(final RedissonClient redisClient, final Method method, final Object[] args) throws Throwable {

        if (method.isAnnotationPresent(Persist.class)) {
            return persist(redisClient, method, args);
        }
        throw new AbstractMethodError("No handler logic for method: " + method.toString());
    }

    public static Object persist(final RedissonClient redisClient, final Method method, final Object[] args) throws Throwable {
        final Iterable<Parameter> params = Reflection.params(method, args);
        final Parameter parameter = params.iterator().next();

        if (parameter.getValue() == null)
            throw new Exception(parameter.getType().getSimpleName() + " object is null");

        RSet set = redisClient.getSet("books");
        set.add(parameter.getValue());
        set.expire(1, TimeUnit.MINUTES);
        return parameter.getValue();
    }
}
