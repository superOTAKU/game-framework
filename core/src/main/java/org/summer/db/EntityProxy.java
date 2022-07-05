package org.summer.db;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class EntityProxy implements MethodInterceptor {
    private GenericEntity entity = new GenericEntity();
    private final AsyncDbDispatcher asyncDbDispatcher;
    public EntityProxy(AsyncDbDispatcher asyncDbDispatcher) {
        this.asyncDbDispatcher = asyncDbDispatcher;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //
        return null;
    }
}
