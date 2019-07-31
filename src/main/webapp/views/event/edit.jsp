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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<form:form action="${requestURI}" modelAttribute="event">

	<form:hidden path="id"/>
	<jstl:if test="${palShow}">
	<spring:message code="event.score" />
	</jstl:if>
	<jstl:out value="${score}" />
	<br>
	<br> 
	
	<acme:textbox code="event.title" path="title"
		readonly="${view or mem}" />
	<acme:textarea code="event.description" path="description"
		readonly="${view or mem}" />
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
			<form:errors path="status" cssClass="error" />
		</jstl:if>
	</security:authorize>
	<br>
	<div>
	<jstl:if test="${not fn:containsIgnoreCase(fn:toLowerCase(linkBanner),'ipsum')}">
	<img src="${linkBanner}">
	</jstl:if>
	</div>

	<jstl:if test="${!event.finalMode and !view}">
				<acme:submit name="save" code="event.save" />
			</jstl:if>
</form:form>
<br>
<acme:cancel url="${requestCancel}" code="event.cancel" />