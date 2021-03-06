/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.struts.examples.web.employee;

import java.util.List;

import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.examples.dto.EmployeeDto;
import org.seasar.struts.examples.dto.EmployeeSearchDto;

/**
 * @author taedium
 * 
 */
public class ListInitAction extends AbstractAction {

    private EmployeeService employeeService;

    private SearchForm searchForm;

    private List<EmployeeDto> empItems;

    @Binding(bindingType = BindingType.MUST)
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void setSearchForm(SearchForm searchForm) {
        this.searchForm = searchForm;
    }

    public List<EmployeeDto> getEmpItems() {
        return empItems;
    }

    public void init() {
        if (searchForm != null) {
            EmployeeSearchDto searchDto = Beans.createAndCopy(
                    EmployeeSearchDto.class, searchForm).excludesNull()
                    .excludesWhitespace().dateConverter("yyyy/MM/dd",
                            "hiredate").execute();
            empItems = employeeService.getEmployeeDtoList(searchDto);
        }
    }
}
