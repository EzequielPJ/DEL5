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
	<spring:message code="sponsorship.title" />
</p>

<display:table name="sponsorships" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">


	<display:column titleKey="sponsorship.banner">
		<img src="${row.banner}" height="50" width="75" />
	</display:column>

	<display:column titleKey="sponsorship.target">
		<a href="${row.target}">${row.target}</a>
	</display:column>


	<display:column>
		<a href="sponsorship/sponsor/show.do?idSponsorship=${row.id}"><spring:message
				code="sponsorship.showmore" /></a>

	</display:column>

	<display:column>
		<a href="sponsorship/sponsor/update.do?idSponsorship=${row.id}"><spring:message
				code="sponsorship.edit" /></a>

	</display:column>

	<jstl:if test="${isActive}">
		<display:column>
			<a href="sponsorship/sponsor/desactive.do?idSponsorship=${row.id}"><spring:message
					code="sponsorship.desactive" /></a>

		</display:column>


	</jstl:if>

	<jstl:if test="${isNotActive}">
		<display:column>
			<a href="sponsorship/sponsor/reactive.do?idSponsorship=${row.id}"><spring:message
					code="sponsorship.reactive" /></a>
		</display:column>
	</jstl:if>

</display:table>