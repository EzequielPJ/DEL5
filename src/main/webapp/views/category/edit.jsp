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

<security:authorize access="hasRole('ADMIN')">
	<form:form action="category/administrator/edit.do?parent=${parent}"
		modelAttribute="category">

		<form:hidden path="id" />
		
		<acme:textbox code="category.name" path="name" />
		<br>	
		<acme:submit name="save" code="category.save" />
		
	</form:form>
	
	<input type="submit" name="cancel"
		value="<spring:message code ="category.cancel"/>"
		onclick="javascript: relativeRedir('category/administrator/list.do');" />
</security:authorize>