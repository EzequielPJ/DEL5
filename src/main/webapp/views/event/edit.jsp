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

<form:form action="${requestURI}" modelAttribute="event">

	<form:hidden path="id"/>
	
	<spring:message code="event.score" />
	<jstl:out value="${score}" />
	<br>
	<br> 
	
	<acme:textbox code="event.title" path="title"
		readonly="${view}" />
	<acme:textarea code="event.description" path="description"
		readonly="${view}" />
	<acme:textbox code="event.moment" path="moment"
		readonly="true" />
	<jstl:if test="${view}">
			<spring:message code="event.status" />:
			<jstl:out value="${event.status}" />
		</jstl:if>
		<br>
	<security:authorize access="hasRole('MEMBER')">
		<jstl:if test="${!view}">
			<form:select path="status">
				<form:option value="0" label="---" />
				<form:options items="${statusCol}" />
			</form:select>
		</jstl:if>
	</security:authorize>
	<form:label path="finalMode">
			<spring:message code="event.finalMode" />
		</form:label>
	<form:checkbox path="finalMode"
			disabled="${view or comission.finalMode}" />
	<jstl:if test="${!event.finalMode and !view}">
				<acme:submit name="save" code="event.save" />
			</jstl:if>
</form:form>
<br>
<acme:cancel url="${requestCancel}" code="event.cancel" />