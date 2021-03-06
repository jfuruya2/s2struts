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
package org.seasar.struts.lessconfig.config.rule;

import org.seasar.framework.util.StringUtil;

/**
 * 共通の命名ルールのためのユーティリティクラスです。
 * 
 * @author Satoshi Kimura
 */
public abstract class CommonNamingRule {

    /**
     * <code>name</code>が「Impl」で終わっている場合、「Impl」を取り除き、先頭文字を小文字にして返します。
     * 
     * @param name
     * @return
     */
    public static String decapitalizeName(String name) {
        if (StringUtil.isEmpty(name)) {
            return name;
        } else {
            name = name.replaceFirst("Impl$", "");
        }

        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
