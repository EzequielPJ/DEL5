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

	<acme:textbox code="proclaim.title" path="title"
		readonly="${proclaim.finalMode or view}" />
	<acme:textbox code="proclaim.description" path="description"
		readonly="${proclaim.finalMode or view}" />
	<acme:textarea code="proclaim.attachments" path="attachments"
		readonly="${proclaim.finalMode or view}" />

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

	<security:authorize access="hasRole('STUDENT')">
		<jstl:if test="${proclaim.finalMode eq 'false'}">
			<acme:submit name="save" code="proclaim.save" />
		</jstl:if>
	</security:authorize>
	<security:authorize access="hasRole('MEMBER')">
		<div>
			<spring:message code="proclaim.status" />
			<form:select path="status" onchange="check(this)" id="status1"
				disabled="${proclaim.status != 'PENDING' or 'PENDIENTE'}">
				<jstl:forEach items="${statusCol}" var="i">
					<form:option value="${i}" />
				</jstl:forEach>
			</form:select>
		</div>
		<div id="law1">
			<acme:textarea code="proclaim.law" path="law"
				readonly="${proclaim.status == 'ACCEPTED' or 'ACEPTADO'}" />
		</div>

		<div id="reason1">
			<acme:textarea code="proclaim.reason" path="reason"
				readonly="${proclaim.status == 'REJECTED' or 'RECHAZADO'}" />
		</div>
		<div>
			<spring:message code="proclaim.closed" />
			<form:checkbox path="closed" />
		</div>

		<acme:submit name="save" code="proclaim.save" />

	</security:authorize>
	<security:authorize access="hasRole('STUDENT')">
		<jstl:if test="${proclaim.id != 0 and proclaim.finalMode eq 'false'}">
			<acme:submit name="delete" code="proclaim.delete" />
		</jstl:if>
	</security:authorize>

</form:form>
<acme:cancel url="${requestCancel}" code="proclaim.cancel" />

<script>
	var status = $("#status1").val();

	if (status == 'PENDING' || status == 'PENDIENTE') {
		$("#reason1").show();
		$("#law1").show();
	}

	if (status == 'ACCEPTED' || status == 'ACEPTADO') {
		$("#reason1").hide();
		$("#law1").show();
	}

	if (status == 'REJECTED' || status == 'RECHAZADO') {
		$("#law1").hide();
		$("#reason1").show();
	}

	function check(o) {
		var statusCheck = o.value;

		if (statusCheck == 'PENDING' || statusCheck == 'PENDIENTE') {
			$("#reason1").show();
			$("#law1").show();
		}

		if (statusCheck == 'ACCEPTED' || statusCheck == 'ACEPTADO') {
			$("#reason1").hide();
			$("#law1").show();
		}

		if (statusCheck == 'REJECTED' || statusCheck == 'RECHAZADO') {
			$("#law1").hide();
			$("#reason1").show();
		}

	}
</script>