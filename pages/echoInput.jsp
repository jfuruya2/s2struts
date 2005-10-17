<%@ page contentType="text/html;charset=Windows-31j" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://www.seasar.org/tags-s2struts" prefix="s2struts" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert page="/pages/layout/layout.jsp">
    <tiles:put name="layoutTitle" type="string">
        <bean:message key="echo.title"/>
    </tiles:put>

    <tiles:put name="body" type="string">

        <html:errors/>

        <html:form action="/echo" focus="input">
            <input name="input" type="text">
            <html:submit><bean:message key="echo"/></html:submit>
            <hr/>
            <b>MethodBinding</b><br/>
            button : <s2struts:submit action="#{echoAction.echo}"><bean:message key="echo"/></s2struts:submit>
            <br/>
            image : <s2struts:image src="../images/echo.jpg" action="#{echoAction.echo}"/>
        </html:form>

    </tiles:put>
</tiles:insert>
