package com.github.vatbub.safeAPIKeyStore.common;

/*-
 * #%L
 * com.github.vatbub.common
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.esotericsoftware.kryo.Kryo;

/**
 * For internal use only.
 */
public class KryoCommon {
    private KryoCommon(){
        throw new IllegalStateException("Class may not be instantiated");
    }

    public static void registerClasses(Kryo kryo) {
        kryo.setReferences(true);
        kryo.register(APIKeyRequest.class);
        kryo.register(APIKeyResponse.class);
        kryo.register(BadRequestExceptionInternalImpl.class);
        kryo.register(InternalServerExceptionInternalImpl.class);
        kryo.register(MultipleRequestsWithSameRSAKeyExceptionInternalImpl.class);
        kryo.register(byte[].class);
        kryo.register(String.class);
    }
}
