package com.github.vatbub.safeAPIKeyStore.common;

import com.esotericsoftware.kryo.Kryo;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class KryoCommonTest {
    @Test
    public void kryoCommonInstantiationTest() {
        try {
            Constructor<KryoCommon> constructor = KryoCommon.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            Assert.fail("Exception expected");
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void registerTest() {
        Kryo kryo = new Kryo();
        KryoCommon.registerClasses(kryo);
        Assert.assertNotNull(kryo.getRegistration(0));
    }
}
