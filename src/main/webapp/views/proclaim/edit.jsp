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

	<acme:textbox code="proclaim.title" path="title" />
	<acme:textbox code="proclaim.description" path="description" />
	<acme:textarea code="proclaim.attachments" path="attachments" />

	<security:authorize access="hasRole('MEMBER')">
		<acme:textbox code="proclaim.reason" path="reason" />
	</security:authorize>

	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="proclaim.cancel" />
		<form:checkbox path="cancel" />
	</security:authorize>

	<security:authorize access="hasRole('STUDENT')">
		<acme:textbox code="proclaim.studentCard.centre"
			path="studentCard.centre" />
		<acme:textbox code="proclaim.studentCard.code" path="studentCard.code" />

		<acme:select items="${categories}" itemLabel="name"
			code="proclaim.category" path="category" />
		<spring:message code="proclaim.finalMode" />
		<form:checkbox path="finalMode" />
	</security:authorize>

	<acme:submit name="save" code="proclaim.save" />
	<jstl:if test="${proclaim.id != 0}">
		<acme:submit name="delete" code="proclaim.delete" />
	</jstl:if>
</form:form>

<acme:cancel url="" code="proclaim.cancel" />