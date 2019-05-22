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

<display:table name="comissions" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<security:authorize access="hasRole('MEMBER')">
	<display:column>
		<a href="comission/member/show.do?idComission=${row.id}"><spring:message
				code="comission.show" /></a>
	</display:column>
	</security:authorize>
	<display:column titleKey="comission.moment">
		<jstl:out value="${row.moment}" />
		</display:column>
	<display:column titleKey="comission.name">
		<jstl:out value="${row.name}" />
		</display:column>
	<display:column titleKey="comission.description">
		<jstl:out value="${row.description}" />
		</display:column>
	<display:column titleKey="comission.finalMode">
		<jstl:out value="${row.finalMode}" />
		</display:column>
	<security:authorize access="hasRole('MEMBER')">
	<display:column>
	<jstl:if test="${!row.finalMode}">
		<a href="comission/member/update.do?idComission=${row.id}"><spring:message
				code="comission.update" /></a>
				</jstl:if>
	</display:column>
	<display:column>
	<jstl:if test="${!row.finalMode}">
		<a href="comission/member/delete.do?idComission=${row.id}"><spring:message
				code="comission.delete" /></a>
	</jstl:if>
	</display:column>
	</security:authorize>

</display:table>
