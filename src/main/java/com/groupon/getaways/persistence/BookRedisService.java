package com.groupon.getaways.persistence;

import com.groupon.getaways.annotations.Persist;
import com.groupon.getaways.entities.Book;
import org.redisson.RedissonClient;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Stateless
public abstract class BookRedisService implements Serializable, InvocationHandler {

    @Inject
    private RedissonClient redisClient;

    @Persist
    public abstract Book createBook(final Book book);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return RedisHandler.invoke(redisClient, method, args);
    }
}
