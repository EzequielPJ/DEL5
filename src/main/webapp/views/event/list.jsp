<%--
 * action-2.jsp
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

<display:table name="events" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<display:column>
		<a href="event/showEvent.do?idEvent=${row.id}"><spring:message
				code="event.show" /></a>
	</display:column>
	
	<display:column titleKey="event.title">
		<jstl:out value="${row.title}" />
	</display:column>
	
	<display:column titleKey="event.description">
		<jstl:out value="${row.description}" />
	</display:column>

	<display:column titleKey="event.moment">
		<jstl:out value="${row.moment}" />
	</display:column>
	
	<display:column titleKey="event.listNotes">
	<jstl:if test="${general}">
	<jstl:if test="${row.finalMode and row.status == 'accepted'}">
		<a href="notes/list.do?id=${row.id}"><spring:message
				code="event.listNotes" /></a>
	</jstl:if>
	</jstl:if>
	</display:column>
	
	<security:authorize access="hasAnyRole('COLLABORATOR', 'STUDENT', 'MEMBER')">
	<display:column titleKey="event.createNotes">
	<jstl:if test="${general}">
	<jstl:if test="${row.finalMode and row.status == 'accepted'}">
		<a href="notes/create.do?id=${row.id}"><spring:message
				code="event.createNotes" /></a>
	</jstl:if>
	</jstl:if>
	</display:column>
	</security:authorize>
	
</display:table>
