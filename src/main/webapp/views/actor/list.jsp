<%--
 * action-1.jsp
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

<p>
	<spring:message code="actor.action.list" />
</p>

<display:table name="actors" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column titleKey="actor.name">
		<jstl:out value="${row.name}" />
	</display:column>
	<display:column titleKey="actor.surname">
		<jstl:out value="${row.surname}" />
	</display:column>
	<display:column titleKey="actor.photo">
		<jstl:out value="${row.photo}" />
	</display:column>
	<display:column titleKey="actor.email">
		<jstl:out value="${row.email}" />
	</display:column>
	<display:column titleKey="actor.phone">
		<jstl:out value="${row.phone}" />
	</display:column>
	<display:column titleKey="actor.adress">
		<jstl:out value="${row.adress}" />
	</display:column>
	<display:column titleKey="actor.user">
		<jstl:out value="${row.account.username}" />
	</display:column>
	<display:column titleKey="actor.enabled">
		<jstl:out value="${row.account.enabled}" />
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:if test="${ban && row.account.enabled}">
				<a href="actor/ban.do?id=${row.id}" class="btn"><spring:message
						code="actor.ban" /></a>
			</jstl:if>
		</display:column>
		<display:column>
			<jstl:if test="${!ban}">
				<a href="actor/unban.do?id=${row.id}" class="btn"><spring:message
						code="actor.unban" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>