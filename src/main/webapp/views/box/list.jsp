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

<display:table name="boxes" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column titleKey="box.name">
		<jstl:out value="${row.name}" />
	</display:column>
	<display:column titleKey="box.fromSystem">
		<jstl:out value="${row.fromSystem}" />
	</display:column>
	<display:column titleKey="box.boxes">
		<jstl:forEach items="${row.boxes}" var="bx">
			<jstl:out value="${bx.name}," />
		</jstl:forEach>
	</display:column>
	<display:column titleKey="box.messages">
		<a href="message/list.do?boxId=${row.id}"><spring:message
				code="box.messages" /></a>
	</display:column>
	<display:column titleKey="box.edit">
		<jstl:if test="${not row.fromSystem}">

			<a href="box/update.do?id=${row.id}"><spring:message
					code="box.edit" /></a>

		</jstl:if>
	</display:column>
	<display:column titleKey="box.delete">
		<jstl:if test="${not row.fromSystem}">

			<a href="box/delete.do?id=${row.id}"><spring:message
					code="box.delete" /></a>

		</jstl:if>
	</display:column>
	<display:column titleKey="box.addsubbox">
		<a href="box/create.do?parent=${row.id}"><spring:message
				code="box.addsubbox" /></a>
	</display:column>
</display:table>

<a href="box/create.do"><spring:message code="box.create" /></a>
