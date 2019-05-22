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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<display:table name="categories" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column titleKey="category.name">
		<jstl:out value="${row.name}" />
	</display:column>
	
	<display:column titleKey="category.categories">
		<jstl:forEach items="${row.categories}" var="cat">
				<jstl:out value="${cat.name}" />
		</jstl:forEach>
	</display:column>
	
	<display:column titleKey="category.edit">

		<a href="category/administrator/update.do?id=${row.id}"><spring:message
				code="category.edit" /></a>

	</display:column>
	<display:column titleKey="category.delete">
	<jstl:if test="${!fn:contains(colRep, row)}">
		<a href="category/administrator/delete.do?cat=${row.id}"><spring:message
				code="category.delete" /></a>
	</jstl:if>
	</display:column>
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="category/administrator/create.do?parent=${row.id}"><spring:message
					code="category.child" /></a>
		</display:column>
	</security:authorize>

</display:table>