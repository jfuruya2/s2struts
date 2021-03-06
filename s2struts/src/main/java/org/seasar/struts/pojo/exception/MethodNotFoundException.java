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
package org.seasar.struts.pojo.exception;

/**
 * メソッドが見つからない場合にスローされる実行時例外です。
 * 
 * @author Satoshi Kimura
 */
public class MethodNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5930403637380028788L;

    /**
     * インスタンスを構築します。
     */
    public MethodNotFoundException() {
        super();
    }

    /**
     * インスタンスを構築します。
     * 
     * @param message
     */
    public MethodNotFoundException(String message) {
        super(message);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param message
     * @param cause
     */
    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     */
    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }
}
