/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.struts.context.impl;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.Base64Util;
import org.seasar.struts.Constants;
import org.seasar.struts.context.S2StrutsContext;
import org.seasar.struts.util.S2StrutsContextUtil;

/**
 * {@link S2StrutsContext}の実装クラスです。
 * 
 * @author Satoshi Kimura
 * @author Katsuhiko Nagashima
 */
public class S2StrutsContextImpl implements S2StrutsContext {
    private static final long serialVersionUID = -4835702530138078142L;

    private String currentPath;

    private String previousPath;

    public void setPath(String path) {
        if (!path.equals(this.currentPath)) {
            this.previousPath = this.currentPath;
            this.currentPath = path;
        }
    }

    public String getCurrentInputPath() {
        if (isClearPageNameElementValue()) {
            String param = getRequest().getParameter(Constants.PAGE_NAME_ELEMENT_VALUE);
            if (param != null) {
                return new String(Base64Util.decode(param));
            }
        }
        return this.currentPath;
    }

    public String getPreviousInputPath() {
        String param = getRequest().getParameter(Constants.PAGE_NAME_ELEMENT_VALUE);
        if (param != null) {
            return new String(Base64Util.decode(param));
        }
        return this.previousPath;
    }

    public void clearPageNameElementValue() {
        getRequest().setAttribute(Constants.PAGE_NAME_ELEMENT_VALUE_CLEAR_MARK, Boolean.TRUE);
    }

    private boolean isClearPageNameElementValue() {
        return (getRequest().getAttribute(Constants.PAGE_NAME_ELEMENT_VALUE_CLEAR_MARK) == null);
    }

    private static HttpServletRequest getRequest() {
        S2Container container = SingletonS2ContainerFactory.getContainer();
        return S2StrutsContextUtil.getRequest(container);
    }

}
