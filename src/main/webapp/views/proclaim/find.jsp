<%--
 * action-2.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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

<form:form action="${requestURI}" modelAttribute="finder">

	<form:hidden path="id" />

	<acme:textbox code="finder.singleKey" path="singleKey" />
	<security:authorize access="hasRole('ROOKIE')">
		<acme:date code="finder.deadline" path="deadline" id="datepicker1" />
		<acme:textbox code="finder.minSalary" path="minSalary" />
		<acme:textbox code="finder.maxSalary" path="maxSalary" />
<!--  		
		<a href="position/clear.do"><spring:message code="finder.clear"></spring:message></a>
		<br>
		-->
	</security:authorize>
	<acme:submit name="search" code="finder.search" />
</form:form>

<acme:cancel url="${requestCancel}" code="finder.cancel" />
