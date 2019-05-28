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

<form:form action="${requestURI}" modelAttribute="swap">

	<form:hidden path="id"/>
	<form:hidden path="receiver"/>
	
<%-- 	<acme:textbox code="swap.sender" path="sender"
		readonly="true" /> --%>
	<acme:textbox code="swap.phone" path="receiver.phone"
		readonly="true" />
	<acme:textarea code="swap.description" path="description"
		readonly="${!send}" />
	<br>
	<jstl:if test="${send}">
	<spring:message code="swap.comission" />:
		<jstl:out value="${swap.comission.name}"></jstl:out>
	</jstl:if>
	<br>
	
<%-- 	<acme:textbox code="swap.receiver" path="receiver"
		readonly="true" /> --%>
	<jstl:if test="${send}">
			<spring:message code="swap.status" />:
			<jstl:out value="${swap.status}" />
		</jstl:if>
		<br>
	<jstl:if test="${!send}">
			<form:select path="status">
				<form:option value="0" label="---" />
				<form:options items="${statusCol}" />
			</form:select>
			<form:errors path="status" cssClass="error" />
		</jstl:if>
	<jstl:if test="${!view}">
			<acme:submit name="save" code="swap.save" />
	</jstl:if>

</form:form>
<br>
<acme:cancel url="${requestCancel}" code="swap.cancel" />