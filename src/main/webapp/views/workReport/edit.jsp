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

<form:form action="${requestURI}" modelAttribute="workReport">

	<form:hidden path="id"/>
	<acme:textbox code="workreport.title" path="title" readonly="${view}" />
	
	<acme:textbox code="workreport.moment" path="moment" readonly="true" />
	<acme:date code="workreport.startDate" path="startDate" id="startDate"  readonly="${view}" />
	<acme:date code="workreport.endDate" path="endDate" id="endDate" readonly="${view}" />
	<acme:textbox code="workreport.businessName" path="businessName" readonly="${view}" />
	<acme:textbox code="studyreport.text" path="text" readonly="${view}" />
	
<jstl:if test="${!view}">
		<input type="submit" name="save"
			value="<spring:message code="wr.save" />" />
	</jstl:if>
</form:form>
<acme:cancel code="cancel" url="${requestCancel}" />
