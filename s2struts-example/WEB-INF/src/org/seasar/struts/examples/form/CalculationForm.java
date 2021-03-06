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
package org.seasar.struts.examples.form;

import org.apache.struts.validator.ValidatorForm;

/**
 * @author Satoshi Kimura
 */
public class CalculationForm extends ValidatorForm {
	private static final long serialVersionUID = 7914771274544612470L;
	private int arg1_;
    private int arg2_;
    private int result_;

    public CalculationForm() {
    }

    public int getArg1() {
        return arg1_;
    }

    public void setArg1(int arg1) {
        arg1_ = arg1;
    }

    public int getArg2() {
        return arg2_;
    }

    public void setArg2(int arg2) {
        arg2_ = arg2;
    }

    public int getResult() {
        return result_;
    }

    public void setResult(int result) {
        result_ = result;
    }
    
    @Override
    public String toString() {
		return "arg1=" + arg1_ + " arg2=" + arg2_ + " result=" + result_;
	}
}