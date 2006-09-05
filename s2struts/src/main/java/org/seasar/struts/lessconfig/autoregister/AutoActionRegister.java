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
package org.seasar.struts.lessconfig.autoregister;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FieldUtil;
import org.seasar.struts.lessconfig.config.AutoStrutsConfigRule;
import org.seasar.struts.lessconfig.config.NullStrutsActionConfig;
import org.seasar.struts.lessconfig.config.StrutsActionConfig;
import org.seasar.struts.lessconfig.config.StrutsActionForwardConfig;
import org.seasar.struts.lessconfig.config.rule.ZeroConfigActionRule;
import org.seasar.struts.lessconfig.factory.StrutsConfigAnnotationHandler;
import org.seasar.struts.lessconfig.factory.StrutsConfigAnnotationHandlerFactory;

/**
 * @author Katsuhiko Nagashima
 * @deprecated changes to <code>ActionConfigCreator</code>
 */
public class AutoActionRegister {

    private static final Logger log = Logger.getLogger(AutoActionRegister.class);

    private AutoActionRegister() {
    }

    public static void register(ServletContext servletContext, ModuleConfig config, Collection classes) {
        classes = ClassComparator.sort(classes);

        for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
            Class clazz = (Class) iterator.next();
            registerActionConfig(servletContext, config, clazz);
        }
    }

    private static void registerActionConfig(ServletContext servletContext, ModuleConfig config, Class actionClass) {
        StrutsConfigAnnotationHandler annHandler = StrutsConfigAnnotationHandlerFactory.getAnnotationHandler();
        StrutsActionConfig strutsAction = annHandler.createStrutsActionConfig(actionClass);
        if (strutsAction == null) {
            if (matchesActionClassPattern(actionClass)) {
                strutsAction = new NullStrutsActionConfig();
            } else {
                return;
            }
        }

        if (registeredActionConfig(strutsAction, actionClass, config)) {
            return;
        }

        ActionConfig actionConfig = new ActionMapping();
        actionConfig.setAttribute(getAttribute(strutsAction, actionClass, config));
        actionConfig.setForward(getForward(strutsAction, actionClass, config));
        actionConfig.setInclude(getInclude(strutsAction, actionClass, config));
        actionConfig.setInput(getInput(strutsAction, actionClass, config));
        actionConfig.setModuleConfig(config);
        actionConfig.setName(getName(strutsAction, actionClass, config));
        actionConfig.setParameter(getParameter(strutsAction, actionClass, config));
        actionConfig.setPath(getPath(strutsAction, actionClass, config));
        actionConfig.setPrefix(getPrefix(strutsAction, actionClass, config));
        actionConfig.setRoles(getRoles(strutsAction, actionClass, config));
        actionConfig.setScope(getScope(strutsAction, actionClass, config));
        actionConfig.setSuffix(getSuffix(strutsAction, actionClass, config));
        actionConfig.setType(actionClass.getName());
        actionConfig.setUnknown(getUnknown(strutsAction, actionClass, config));
        actionConfig.setValidate(getValidate(strutsAction, actionClass, config));
        actionConfig.setCancellable(getCancellable(strutsAction, actionClass, config));

        registerForwardConfigs(servletContext, actionConfig, actionClass);

        config.addActionConfig(actionConfig);

        if (log.isDebugEnabled()) {
            log.debug("auto register " + actionConfig);
            ForwardConfig[] forwardConfigs = actionConfig.findForwardConfigs();
            for (int i = 0; i < forwardConfigs.length; i++) {
                log.debug("auto register " + forwardConfigs[i]);
            }
        }
    }

    private static boolean matchesActionClassPattern(Class clazz) {
        return clazz.getName().matches(configRule().getActionClassPattern());
    }

    public static boolean registeredActionConfig(StrutsActionConfig strutsAction, Class actionClass, ModuleConfig config) {
        String path = getPath(strutsAction, actionClass, config);
        ActionConfig[] actionConfigs = config.findActionConfigs();
        for (int i = 0; i < actionConfigs.length; ++i) {
            if (path.equals(actionConfigs[i].getPath())) {
                return true;
            }
        }
        return false;
    }

    private static void registerForwardConfigs(ServletContext servletContext, ActionConfig actionConfig,
            Class actionClass) {
        Field[] fields = actionClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            StrutsConfigAnnotationHandler annHandler = StrutsConfigAnnotationHandlerFactory.getAnnotationHandler();
            StrutsActionForwardConfig actionForward = annHandler.createStrutsActionForwardConfig(fields[i]);
            if (actionForward != null) {
                fields[i].setAccessible(true);
                String name = FieldUtil.get(fields[i], actionClass).toString();
                ForwardConfig forwardConfig = new ActionForward();
                forwardConfig.setName(name);
                forwardConfig.setPath(actionForward.path());
                forwardConfig.setRedirect(actionForward.redirect());
                actionConfig.addForwardConfig(forwardConfig);
            }
        }
        if (matchesActionClassPattern(actionClass)) {
            rule().addForwardConfig(actionClass, actionConfig, servletContext);
        }
    }

    private static AutoStrutsConfigRule configRule() {
        S2Container container = SingletonS2ContainerFactory.getContainer();
        return (AutoStrutsConfigRule) container.getComponent(AutoStrutsConfigRule.class);
    }

    private static ZeroConfigActionRule rule() {
        S2Container container = SingletonS2ContainerFactory.getContainer();
        return (ZeroConfigActionRule) container.getComponent(ZeroConfigActionRule.class);
    }

    private static String getPath(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_PATH.equals(action.path()) ? rule().getPath(actionClass, config) : action
                .path();
    }

    private static String getAttribute(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_ATTRIBUTE.equals(action.attribute()) ? rule().getAttribute(actionClass,
                config) : action.attribute();
    }

    private static String getForward(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_FORWARD.equals(action.forward()) ? rule().getForward(actionClass, config)
                : action.forward();
    }

    private static String getInclude(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_INCLUDE.equals(action.include()) ? rule().getInclude(actionClass, config)
                : action.include();
    }

    private static String getInput(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_INPUT.equals(action.input()) ? rule().getInput(actionClass, config) : action
                .input();
    }

    private static String getName(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_NAME.equals(action.name()) ? rule().getName(actionClass, config) : action
                .name();
    }

    private static String getParameter(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_PARAMETER.equals(action.parameter()) ? rule().getParameter(actionClass,
                config) : action.parameter();
    }

    private static String getPrefix(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_PREFIX.equals(action.prefix()) ? rule().getPrefix(actionClass, config)
                : action.prefix();
    }

    private static String getRoles(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_ROLES.equals(action.roles()) ? rule().getRoles(actionClass, config) : action
                .roles();
    }

    private static String getScope(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_SCOPE.equals(action.scope()) ? rule().getScope(actionClass, config) : action
                .scope();
    }

    private static String getSuffix(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return StrutsActionConfig.DEFAULT_SUFFIX.equals(action.suffix()) ? rule().getSuffix(actionClass, config)
                : action.suffix();
    }

    private static boolean getUnknown(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return action.unknown() == StrutsActionConfig.DEFAULT_UNKNOWN ? rule().getUnknown(actionClass, config) : action
                .unknown();
    }

    private static boolean getValidate(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return action.validate() == StrutsActionConfig.DEFAULT_VALIDATE ? rule().getValidate(actionClass, config)
                : action.validate();
    }

    private static boolean getCancellable(StrutsActionConfig action, Class actionClass, ModuleConfig config) {
        return action.cancellable() == StrutsActionConfig.DEFAULT_CANCELLABLE ? rule().getCancellable(actionClass,
                config) : action.validate();
    }

}