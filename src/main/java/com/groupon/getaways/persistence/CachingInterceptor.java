package com.groupon.getaways.persistence;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@Interceptor
@Cacheable
public class CachingInterceptor implements Serializable {

    @AroundInvoke
    public Object perform(final InvocationContext ctx) throws Exception {
        System.out.println("WOHOOOOOOO");

        return ctx.proceed();
    }
}
