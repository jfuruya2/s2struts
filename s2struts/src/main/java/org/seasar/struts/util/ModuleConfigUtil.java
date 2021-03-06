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
package org.seasar.struts.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link ModuleConfig}のためのユーティリティクラスです。
 * 
 * @author Katsuhiko Nagashima
 */
public class ModuleConfigUtil {

    private ModuleConfigUtil() {
    }

    /**
     * デフォルトの{@link ModuleConfig}を返します。
     * 
     * @return
     */
    public static ModuleConfig getModuleConfig() {
        return getModuleConfig(null);
    }

    /**
     * 指定されたモジュール名の{@link ModuleConfig}を返します。
     * 
     * @param module
     * @return
     */
    public static ModuleConfig getModuleConfig(String module) {
        ServletContext context = S2StrutsContextUtil.getServletContext();
        HttpServletRequest request = S2StrutsContextUtil.getRequest();
        return ModuleUtils.getInstance().getModuleConfig(module, request, context);
    }

    /**
     * {@link ActionConfig}を返します。
     * 
     * @param path
     * @return
     */
    public static ActionConfig findActionConfig(String path) {
        return findActionConfig(null, path);
    }

    /**
     * 指定されたモジュール名の{@link ActionConfig}を返します。
     * 
     * @param module
     * @param path
     * @return
     */
    public static ActionConfig findActionConfig(String module, String path) {
        return ModuleConfigUtil.getModuleConfig(module).findActionConfig(path);
    }

    /**
     * 指定された{@link ActionForm}の名前に対応付けられた最初の{@link ActionConfig}を返します。
     * 
     * @param beanName
     * @return
     */
    public static ActionConfig findActionConfigForFormBeanName(String beanName) {
        ModuleConfig config = ModuleConfigUtil.getModuleConfig();
        if (config == null) {
            return null;
        }

        FormBeanConfig beanConfig = config.findFormBeanConfig(beanName);
        if (beanConfig == null) {
            return null;
        }

        ActionConfig[] actionConfigs = config.findActionConfigs();
        for (int i = 0; i < actionConfigs.length; i++) {
            ActionConfig actionConfig = actionConfigs[i];
            if (beanConfig.getName().equals(actionConfig.getName())) {
                return actionConfig;
            }
        }
        return null;
    }

    /**
     * 指定された{@link ActionForm}の名前に対応付けられた複数の{@link ActionConfig}を返します。
     * 
     * @param beanName
     * @return
     */
    public static ActionConfig[] findActionConfigsForFormBeanName(String beanName) {
        ModuleConfig config = ModuleConfigUtil.getModuleConfig();
        if (config == null) {
            return new ActionConfig[0];
        }

        FormBeanConfig beanConfig = config.findFormBeanConfig(beanName);
        if (beanConfig == null) {
            return new ActionConfig[0];
        }

        List result = new ArrayList();
        ActionConfig[] actionConfigs = config.findActionConfigs();
        for (int i = 0; i < actionConfigs.length; i++) {
            ActionConfig actionConfig = actionConfigs[i];
            if (beanConfig.getName().equals(actionConfig.getName())) {
                result.add(actionConfig);
            }
        }
        return (ActionConfig[]) result.toArray(new ActionConfig[result.size()]);
    }

    /**
     * 指定されたActionのコンポーネント名に対応付けられた{@link ActionConfig}を返します。
     * 
     * @param componentName
     * @return
     */
    public static ActionConfig findActionConfigForComponentName(String componentName) {
        return findActionConfigForComponentName(null, componentName);
    }

    /**
     * 指定されたモジュールに属し、指定されたActionのコンポーネント名に対応付けられた{@link ActionConfig}を返します。
     * 
     * @param module
     * @param componentName
     * @return
     */
    public static ActionConfig findActionConfigForComponentName(String module, String componentName) {
        if (!ModuleConfigUtil.getContainer().hasComponentDef(componentName)) {
            return null;
        }
        Class clazz = ModuleConfigUtil.getContainer().getComponentDef(componentName).getComponentClass();

        ModuleConfig config = ModuleConfigUtil.getModuleConfig(module);
        if (config == null) {
            return null;
        }

        ActionConfig[] actionConfigs = config.findActionConfigs();
        for (int i = 0; i < actionConfigs.length; i++) {
            if (actionConfigs[i].getType() == null) {
                continue;
            }
            Class actionClass = ClassUtil.forName(actionConfigs[i].getType());
            if (actionClass.isAssignableFrom(clazz)) {
                return actionConfigs[i];
            }
        }
        return null;
    }

    private static S2Container getContainer() {
        return SingletonS2ContainerFactory.getContainer();
    }

}
