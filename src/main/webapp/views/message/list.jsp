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

<p>
	<jstl:out value="${boxName}"></jstl:out>
</p>

<display:table name="messages" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column titleKey="message.subject">
		<jstl:out value="${row.subject}" />
	</display:column>
	<display:column titleKey="message.body">
		<jstl:out value="${row.body}" />
	</display:column>
	<display:column titleKey="message.tags">
		<jstl:forEach items="${row.tags}" var="tag">
			<jstl:out value="${tag}"></jstl:out>
		</jstl:forEach>
	</display:column>
	<display:column titleKey="message.priority">
		<jstl:out value="${row.priority}" />
	</display:column>
	<display:column titleKey="message.momentsent">
		<jstl:out value="${row.momentsent}" />
	</display:column>

	<display:column>
		<a href="message/show.do?id=${row.id}"><spring:message
				code="message.viewmore" /></a>
	</display:column>
	<jstl:if test="${boxName == 'Trash Box'}">
		<display:column>

			<a href="message/inbox.do?id=${row.id}"><spring:message
					code="message.inbox" /></a>

		</display:column>
	</jstl:if>
	<jstl:if test="${boxName == 'Trash Box'}">
		<display:column>

			<a href="message/delete.do?id=${row.id}"><spring:message
					code="message.delete" /></a>

		</display:column>
	</jstl:if>

</display:table>

<!-- <a href="message/create.do"><spring:message code="message.create" /></a> -->
