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
package org.seasar.struts.processor;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.struts.processor.support.ActionPathResolver;
import org.seasar.struts.util.S2StrutsContextUtil;

/**
 * {@link ExternalRequestProcessor#processPath(HttpServletRequest,
 * javax.servlet.http.HttpServletResponse)}に対するインターセプタです。
 * <p>
 * Actionのパスを解決します。
 * </p>
 * 
 * @author taedium
 */
public class ProcessPathInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 3408699693087684681L;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        String path = (String) invocation.proceed();
        HttpServletRequest request = S2StrutsContextUtil.getRequest();
        return ActionPathResolver.resolve(request, path);
    }

}
