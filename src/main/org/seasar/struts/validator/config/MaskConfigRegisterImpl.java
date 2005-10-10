package org.seasar.struts.validator.config;

import java.lang.reflect.Method;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.Msg;
import org.apache.commons.validator.Var;
import org.codehaus.backport175.reader.Annotation;
import org.codehaus.backport175.reader.Annotations;
import org.seasar.struts.validator.annotation.Mask;

/**
 * @author Satoshi Kimura
 */
public class MaskConfigRegisterImpl implements ConfigRegister {

    public void regist(Field field, Method method) {
        Annotation annotation = Annotations.getAnnotation(Mask.class, method);
        if (annotation == null) {
            return;
        }

        Var var = new Var();
        var.setName("mask");
        var.setValue(String.valueOf(((Mask) annotation).pattern()));
        field.addVar(var);

        Msg message = new Msg();
        message.setName("mask");
        message.setKey(((Mask) annotation).messageKey());
        field.addMsg(message);
    }

}