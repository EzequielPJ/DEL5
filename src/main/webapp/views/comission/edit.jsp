<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="comission">

	<form:hidden path="id"/>
	
	<acme:textbox code="comission.name" path="name"
		readonly="${view}" />
	<acme:textbox code="comission.description" path="description"
		readonly="${view}" />
	<security:authorize access="hasRole('MEMBER')">
	<form:label path="finalMode">
			<spring:message code="comission.finalMode" />
		</form:label>
	<form:checkbox path="finalMode"
			disabled="${view or comission.finalMode}" />
			<jstl:if test="${!comission.finalMode and !view}">
				<acme:submit name="save" code="comission.save" />
			</jstl:if>
	</security:authorize>
</form:form>
<acme:cancel url="${requestCancel}" code="comission.cancel" />