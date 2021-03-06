/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.httpunit.mock;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Satoshi Kimura
 */
public class MockObjectInterceptorMapping {
    private static Map mapping;

    static {
        mapping = new HashMap();
        mapping.put(MockHttpServletRequest.class, "HttpServletRequestMockObjectInterceptor");
        mapping.put(MockHttpServletResponse.class, "HttpServletResponseMockObjectInterceptor");
    }

    private MockObjectInterceptorMapping() {
    }

    public static String getMockObjectInterceptorName(Class clazz) {
        return (String) mapping.get(clazz);
    }
}