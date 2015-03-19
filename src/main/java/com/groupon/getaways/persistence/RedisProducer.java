package com.groupon.getaways.persistence;

import org.redisson.Redisson;
import org.redisson.RedissonClient;

import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;

import static javax.ejb.LockType.READ;

@Lock(READ)
@Singleton
public class RedisProducer {

    private RedissonClient redissonClient = Redisson.create();

    @Produces
    public RedissonClient produce() {
        return this.redissonClient;
    }
}
