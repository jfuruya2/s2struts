/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.struts.validator.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.Msg;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.util.ConstantValueUtil;

/**
 * @author Katsuhiko Nagashima
 */
public class ConstantValidatorAnnotationHandler extends AbstractValidatorAnnotationHandler {

    private static final String VALIDATOR_SUFFIX = "_VALIDATOR";

    private static final String ARGS_SUFFIX = "_VALIDATOR_ARGS";

    private static final String MESSAGE_SUFFIX = "_VALIDATOR_MESSAGE";

    private static final String TYPE = "type";

    private static final String NO_VALIDATE = "noValidate";

    protected Field createField(Form form, BeanDesc beanDesc, PropertyDesc propDesc) {
        if (!propDesc.hasWriteMethod()) {
            return null;
        }

        String propName = propDesc.getPropertyName();
        if (form.getField(propName) != null) {
            // registered
            return null;
        }

        List parameters = getValidatorParameters(beanDesc, propDesc);
        if (noValidate(parameters)) {
            return null;
        }

        Method method = propDesc.getWriteMethod();
        String depends = createDepends(method, parameters);
        if (depends == null) {
            return null;
        }

        Field field = new Field();
        addMessage(field, beanDesc, propDesc);
        addArgs(field, beanDesc, propDesc);
        field.setDepends(depends);
        field.setProperty(propDesc.getPropertyName());
        registerConfig(field, method, parameters);

        return field;
    }

    private List getValidatorParameters(BeanDesc beanDesc, PropertyDesc propDesc) {
        List result = new ArrayList();

        String fieldName = propDesc.getPropertyName() + VALIDATOR_SUFFIX;
        Map parameter = getValidatorParameter(beanDesc, fieldName);
        if (parameter != null) {
            result.add(parameter);
        }

        for (int i = 0; hasValidatorParameter(beanDesc, fieldName, i); i++) {
            parameter = getValidatorParameter(beanDesc, fieldName, i);
            if (parameter != null) {
                result.add(parameter);
            }
        }

        return result;
    }

    private boolean hasValidatorParameter(BeanDesc beanDesc, String fieldName) {
        return beanDesc.hasField(fieldName);
    }

    private Map getValidatorParameter(BeanDesc beanDesc, String fieldName) {
        if (!beanDesc.hasField(fieldName)) {
            return null;
        }
        String value = (String) beanDesc.getFieldValue(fieldName, null);
        return ConstantValueUtil.toMap(value, TYPE);
    }

    private boolean hasValidatorParameter(BeanDesc beanDesc, String fieldName, int index) {
        return hasValidatorParameter(beanDesc, fieldName + "_" + Integer.toString(index));
    }

    private Map getValidatorParameter(BeanDesc beanDesc, String fieldName, int index) {
        return getValidatorParameter(beanDesc, fieldName + "_" + Integer.toString(index));
    }

    private boolean noValidate(List parameters) {
        for (Iterator it = parameters.iterator(); it.hasNext();) {
            Map parameter = (Map) it.next();
            String type = (String) parameter.get(TYPE);
            if (NO_VALIDATE.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    private void registerConfig(Field field, Method method, List parameters) {
        registerAutoTypeValidatorConfig(field, method);

        for (Iterator it = parameters.iterator(); it.hasNext();) {
            Map parameter = (Map) it.next();
            String type = (String) parameter.get(TYPE);
            if (hasConfigRegister(type)) {
                executeConfigRegister(field, type, parameter);
            }
        }
    }

    private String createDepends(Method method, List parameters) {
        StringBuffer depends = new StringBuffer("");

        String autoTypeValidatorName = getAutoTypeValidatorName(method);
        if (!StringUtil.isEmpty(autoTypeValidatorName)) {
            depends.append(autoTypeValidatorName).append(",");
        }

        for (Iterator it = parameters.iterator(); it.hasNext();) {
            Map parameter = (Map) it.next();
            depends.append(parameter.get(TYPE)).append(",");
        }
        if (depends.length() < 1) {
            return null;
        }
        depends.setLength(depends.length() - 1);
        return depends.toString();
    }

    private void addArgs(Field field, BeanDesc beanDesc, PropertyDesc propDesc) {
        String[] keys = { propDesc.getPropertyName() };
        boolean resource = false;
        
        String fieldName = propDesc.getPropertyName() + ARGS_SUFFIX;
        if (beanDesc.hasField(fieldName)) {
            String value = (String) beanDesc.getFieldValue(fieldName, null);
            Map parameter = ConstantValueUtil.toMap(value, "keys");
            keys = toArrays((String) parameter.get("keys"));
            String resourceStr = (String) parameter.get("resource");
            if (!StringUtil.isEmpty(resourceStr)) {
                resource = BooleanConversionUtil.toPrimitiveBoolean(resourceStr);
            }
        }

        for (int i = 0; i < keys.length; i++) {
            Arg arg = new Arg();
            arg.setKey(keys[i]);
            arg.setResource(resource);
            arg.setPosition(i);
            field.addArg(arg);
        }
    }

    private void addMessage(Field field, BeanDesc beanDesc, PropertyDesc propDesc) {
        String fieldName = propDesc.getPropertyName() + MESSAGE_SUFFIX;
        if (!beanDesc.hasField(fieldName)) {
            return;
        }
        String value = (String) beanDesc.getFieldValue(fieldName, null);
        Map parameter = ConstantValueUtil.toMap(value);

        String name = (String) parameter.get("name");
        String key = (String) parameter.get("key");
        String bundle = (String) parameter.get("bundle");
        String resourceStr = (String) parameter.get("resource");
        boolean resource = true;
        if (!StringUtil.isEmpty(resourceStr)) {
            resource = BooleanConversionUtil.toPrimitiveBoolean(resourceStr);
        }

        Msg msg = new Msg();
        msg.setBundle(bundle);
        msg.setKey(key);
        msg.setName(name);
        msg.setResource(resource);
        field.addMsg(msg);
    }

}