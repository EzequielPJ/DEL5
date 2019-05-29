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

<display:table name="swaps" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<display:column titleKey="swap.status">
		<jstl:out value="${row.status}" />
	</display:column>
	<display:column titleKey="swap.description">
		<jstl:out value="${row.description}" />
	</display:column>
	<jstl:if test="${!pend}">
	<display:column titleKey="swap.comission">
		<jstl:out value="${row.receiver.comission.name}" />
	</display:column>
		<display:column titleKey="swap.receiver">
		<jstl:out value="${row.receiver.name}" />
	</display:column>
	</jstl:if>
	<jstl:if test="${pend}">
	<display:column titleKey="swap.comission">
		<jstl:out value="${row.comission.name}" />
	</display:column>
		<display:column titleKey="swap.receiver">
		<jstl:out value="${row.sender.name}" />
	</display:column>
	<display:column>
		<a href="swap/collaborator/update.do?idSwap=${row.id}"><spring:message
				code="swap.update" /></a>
	</display:column>
	</jstl:if>
	
	


</display:table>
