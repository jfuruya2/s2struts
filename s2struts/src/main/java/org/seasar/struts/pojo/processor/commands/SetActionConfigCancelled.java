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
package org.seasar.struts.pojo.processor.commands;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.chain.commands.ActionCommandBase;
import org.apache.struts.chain.contexts.ActionContext;
import org.apache.struts.chain.contexts.ServletActionContext;
import org.seasar.framework.log.Logger;
import org.seasar.struts.pojo.processor.ActionMappingWrapper;
import org.seasar.struts.util.S2StrutsContextUtil;

/**
 * Submitタグ、Imageタグでcancel=trueとして登録されたプロパティ名がrequestに含まれている場合、ActionConfigのvalidate属性をvalidateを行わないためにfalseに上書する。
 * 
 * @author Katsuhiko Nagashima
 * 
 */
public class SetActionConfigCancelled extends ActionCommandBase {

    private static final Logger log = Logger.getLogger(SetActionConfigCancelled.class);

    public boolean execute(ActionContext actionContext) throws Exception {
        ActionMapping mapping = (ActionMapping) actionContext.getActionConfig();
        ServletActionContext saContext = (ServletActionContext) actionContext;
        HttpServletRequest request = saContext.getRequest();

        if (S2StrutsContextUtil.isCancel(request, mapping)) {
            log.debug(" Cancelled transaction, skipping validation");
            ActionMappingWrapper wrapper = new ActionMappingWrapper(mapping);
            wrapper.setValidate(false);
            actionContext.setActionConfig(wrapper);
        }
        return false;
    }

}
