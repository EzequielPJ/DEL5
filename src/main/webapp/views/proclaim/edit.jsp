<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="proclaim">

	<form:hidden path="id" />

	<acme:textbox code="proclaim.ticker" path="ticker" readonly="true" />
	<acme:textbox code="proclaim.moment" path="moment" readonly="true" />

	<security:authorize access="hasRole('STUDENT')">
		<acme:textbox code="proclaim.status" path="status" readonly="true" />
	</security:authorize>

	<security:authorize access="hasRole('MEMBER')">
		<form:select path="status" title="proclaim.status">
			<jstl:forEach items="${statusCol}" var="i">
				<form:option value="${i}" />
			</jstl:forEach>
		</form:select>
	</security:authorize>

	<acme:textbox code="proclaim.title" path="title"
		readonly="${proclaim.finalMode or view}" />
	<acme:textbox code="proclaim.description" path="description"
		readonly="${proclaim.finalMode or view}" />
	<acme:textarea code="proclaim.attachments" path="attachments"
		readonly="${proclaim.finalMode or view}" />

	<security:authorize access="hasRole('MEMBER')">
		<acme:textbox code="proclaim.reason" path="reason"
			readonly="${proclaim.finalMode}" />
	</security:authorize>

	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="proclaim.cancel" />
		<form:checkbox path="cancel" disabled="${proclaim.finalMode or view}" />
	</security:authorize>

	<security:authorize access="hasRole('STUDENT')">
		<acme:textbox code="proclaim.studentCard.centre"
			path="studentCard.centre" readonly="${proclaim.finalMode or view}" />
		<acme:textbox code="proclaim.studentCard.code" path="studentCard.code"
			readonly="${proclaim.finalMode or view}" />
		<acme:textbox code="proclaim.studentCard.vat" path="studentCard.vat"
			readonly="${proclaim.finalMode or view}" />

		<jstl:if test="${proclaim.finalMode eq 'false'}">
			<acme:select items="${categories}" itemLabel="name"
				code="proclaim.category" path="category" />
		</jstl:if>
		<div>
			<jstl:if test="${proclaim.finalMode eq 'true' or view}">
				<form:hidden path="category" />
				<acme:textbox code="proclaim.category" path="category.name"
					readonly="true" />
			</jstl:if>
		</div>
		<spring:message code="proclaim.finalMode" />
		<form:checkbox path="finalMode" disabled="${proclaim.finalMode}" />
	</security:authorize>

	<security:authorize access="hasRole('STUDENT')">
		<jstl:if test="${proclaim.finalMode eq 'false'}">
			<acme:submit name="save" code="proclaim.save" />
		</jstl:if>
	</security:authorize>
	<jstl:if test="${view}">
		<security:authorize access="hasRole('MEMBER')">
			<acme:submit name="save" code="proclaim.save" />
		</security:authorize>
	</jstl:if>
	<security:authorize access="hasRole('STUDENT')">
		<jstl:if test="${proclaim.id != 0 and proclaim.finalMode eq 'false'}">
			<acme:submit name="delete" code="proclaim.delete" />
		</jstl:if>
	</security:authorize>

</form:form>

<acme:cancel url="${requestCancel}" code="proclaim.cancel" />