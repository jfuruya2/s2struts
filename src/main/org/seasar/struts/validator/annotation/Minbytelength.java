package org.seasar.struts.validator.annotation;

/**
 * @author Katsuhiko Nagashima
 */
public interface Minbytelength extends CommonValidator {

    int value();
    
    String charset();
}