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
package org.seasar.struts.lessconfig.factory;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * バリデーション用のアノテーションハンドラを作成するファクトリです。
 * 
 * @author Katsuhiko Nagashima
 */
public class ValidatorAnnotationHandlerFactory {

    private static final String TIGER_ANNOTATION_HANDLER_CLASS_NAME = "org.seasar.struts.lessconfig.factory.TigerValidatorAnnotationHandler";

    private static final String BACKPORT175_ANNOTATION_HANDLER_CLASS_NAME = "org.seasar.struts.lessconfig.factory.Backport175ValidatorAnnotationHandler";

    private static ValidatorAnnotationHandler annotationHandler;

    static {
        Class clazz = ConstantValidatorAnnotationHandler.class;
        try {
            clazz = ClassUtil.forName(TIGER_ANNOTATION_HANDLER_CLASS_NAME);
        } catch (ClassNotFoundRuntimeException ignore) {
            try {
                clazz = ClassUtil.forName(BACKPORT175_ANNOTATION_HANDLER_CLASS_NAME);
            } catch (ClassNotFoundRuntimeException ignore2) {
            }
        }
        annotationHandler = (ValidatorAnnotationHandler) ClassUtil.newInstance(clazz);
    }

    protected ValidatorAnnotationHandlerFactory() {
    }

    /**
     * {@link ValidatorAnnotationHandler}を返します。
     * 
     * @return
     */
    public static ValidatorAnnotationHandler getAnnotationHandler() {
        return annotationHandler;
    }

    /**
     * {@link ValidatorAnnotationHandler}を設定します。
     * 
     * @param handler
     */
    public static void setAnnotationHandler(ValidatorAnnotationHandler handler) {
        annotationHandler = handler;
    }

}
