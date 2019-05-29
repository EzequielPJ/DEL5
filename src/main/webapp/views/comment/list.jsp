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

<display:table name="comments" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column titleKey="comment.description">
		<jstl:out value="${row.description}" />
	</display:column>

	<display:column titleKey="comment.attachments">
		<jstl:out value="${row.attachments}" />
	</display:column>

	<display:column titleKey="comment.author">
		<jstl:out value="${row.actor.name}" />
	</display:column>

	<display:column titleKey="update">
		<security:authorize access="hasRole('MEMBER')">
			<a href="comment/member/update.do?id=${row.id}"><spring:message
					code="comment.update"></spring:message></a>
		</security:authorize>
		<security:authorize access="hasRole('STUDENT')">
			<a href="comment/student/update.do?id=${row.id}"><spring:message
					code="comment.update"></spring:message></a>
		</security:authorize>

	</display:column>

	<display:column titleKey="delete">
		<security:authorize access="hasRole('MEMBER')">
			<a href="comment/member/delete.do?id=${row.id}"><spring:message
					code="comment.delete"></spring:message></a>
		</security:authorize>
		<security:authorize access="hasRole('STUDENT')">
			<a href="comment/student/delete.do?id=${row.id}"><spring:message
					code="comment.delete"></spring:message></a>
		</security:authorize>

	</display:column>

</display:table>
