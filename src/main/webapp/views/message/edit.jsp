<%--
 * action-1.jsp
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<script type="text/javascript">
	function checkAll(o) {
		var all = document.querySelectorAll('input[type="checkbox"]');

		for ( var i = 0; i < all.length; i++) {

			if (all[i] != o) {
				all[i].checked = o.checked;
				all[i].onclick = function() {
					return !o.checked;
				}
			}

		}
	}
</script>

<form:form action="${requestURI}" modelAttribute="messageEntity">

	<form:hidden path="id" />

	<acme:textbox code="message.subject" path="subject" readonly="${view}" />
	<acme:textbox code="message.momentsent" path="momentsent"
		readonly="true" />
	<acme:textarea code="message.tags" path="tags" readonly="${view}" />

	<jstl:if test="${view}">
		<spring:message code="message.priority" />
		<jstl:out value=" --> ${messageEntity.priority}" />
	</jstl:if>
	<jstl:if test="${view eq 'false'}">
		<form:label path="priority">
			<spring:message code="message.priority" />
		</form:label>
		<form:select path="priority">
			<form:option value="NONE" label="--- Select ---" />
			<jstl:forEach items="${priorities}" var="p">
				<form:option value="${p}" label="${p}" />
			</jstl:forEach>

		</form:select>
		<form:errors path="priority" cssClass="error" />
	</jstl:if>

	<security:authorize access="hasRole('ADMIN')">
		<jstl:if test="${not view}">
			<input type="checkbox" name="broadcast" onclick="checkAll(this);" />
			<spring:message code="message.broadcast" />
		</jstl:if>
	</security:authorize>


	<div
		style="width: 500px; height: 100px; overflow-y: scroll; border-style: solid; border-color: initial;">
		<jstl:if test="${view}">
			<%-- 			<form:hidden path="receiver" /> --%>
			<jstl:forEach items="${rece}" var="actor">
				<%-- 				<input type="checkbox" name="receiver" onpause="checkAll(this);" value="${actor.id}"
					disabled="${view}" /> --%>
				<jstl:out value="${actor.name} ${actor.surname} ( ${actor.email} )" />
				<br>
			</jstl:forEach>
		</jstl:if>

		<jstl:if test="${view eq 'false'}">
			<jstl:forEach items="${actors}" var="actor">
				<input type="checkbox" name="receiver" value="${actor.id}"
					readonly="${view}" />
				<jstl:out value="${actor.name} ${actor.surname} ( ${actor.email} )" />
				<br>
			</jstl:forEach>
		</jstl:if>
	</div>


	<acme:textarea code="message.body" path="body" readonly="${view}" />

	<jstl:if test="${not view}">
		<acme:submit name="send" code="message.save" />
	</jstl:if>
	<jstl:if test="${view}">
		<a href="message/dbox.do?boxId=${trash}&mess=${messageEntity.id}"><spring:message
				code="message.move" /></a>
	</jstl:if>
	<jstl:if test="${view}">
		<br>
		<spring:message code="message.boxes" />
		<div
			style="width: 500px; height: 100px; overflow-y: scroll; border-style: solid; border-color: initial;">
			<br>
			<jstl:forEach items="${boxesOptional}" var="var">
				<spring:message code="message.boxes" />: 
				<a href="message/dbox.do?boxId=${var.id}&mess=${messageEntity.id}"><jstl:out
						value="${var.name}">
					</jstl:out></a>
				<br>
			</jstl:forEach>
			<br>
		</div>

	</jstl:if>
</form:form>
