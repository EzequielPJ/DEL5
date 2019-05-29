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

<form:form action="${requestURI}" modelAttribute="portfolio">

	<form:hidden path="id"/>
	<form:hidden path="studyReport"/>
	
	<form:hidden path="miscellaneousReport"/>
	<form:hidden path="workReport"/>
	<acme:textbox code="portfolio.title" path="title" readonly="${view}" />
	<acme:textbox code="portfolio.fullName" path="fullName" readonly="true" />
	<acme:textbox code="portfolio.moment" path="moment" readonly="true" />
	<acme:textbox code="portfolio.address" path="address" readonly="true" />
	<acme:textbox code="portfolio.phone" path="phone" readonly="true" />
<jstl:if test="${!view}">
		<input type="submit" name="save"
			value="<spring:message code="portfolio.save" />" />
	</jstl:if>
</form:form>
<acme:cancel code="actor.cancel" url="${requestCancel}" />

