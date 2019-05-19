<%--
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<link
	href="https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css"
	rel="stylesheet">

<script>
	$(function() {
		$("#datepicker-1").datepicker({
			changeMonth : true,
			changeYear : true,
			showButtonPanel : true,
			minDate : new Date(),
			appendText : "(yy/mm)",
			dateFormat : "yy/mm",
			onClose : function(dateText, inst) {
				$(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
			}
		});
	});

	$(document).ready(
			function() {
				$("#form").submit(
						function(event) {
							var phone = $("#phone").val();
							var regex = new RegExp(
									"(((\\+){0,1}\\s{0,1}([1-9]|[1-9][0-9]|[1-9][0-9][0-9])){0,1}\\s{0,1}(\\(([1-9]|[1-9][0-9]|[1-9][0-9][0-9])\\)){0,1}\\s{0,1}([1-9]{4,9}$))",
									"m");

							if (regex.test(phone) == false) {
								if (confirm("<spring:message code = 'phone.confirm1' />")) {
									var res = confirm("<spring:message code = 'phone.confirm2' />");
									return res;
								} else {
									return false;
								}
							}

							if (phone.startsWith("+") == false) {
								var pref = "${prefix}";
								var fix = "+".concat(pref + " ", phone);
								$("#phone").val(fix);
							}
						});
			});
</script>
<style>
.ui-datepicker-calendar {
	display: none;
}
</style>
<form:form action="actor/edit.do" modelAttribute="actor" id="form">

	<form:hidden path="id" id="id" />
	<form:hidden path="authority" />
	<acme:textbox code="actor.name" path="name" readonly="${view}" />
	<acme:textbox code="actor.surname" path="surname" readonly="${view}" />
	<acme:textbox code="actor.legalName" path="vat" readonly="${view}" />
	<acme:textbox code="actor.phone" path="phone" id="phone"
		readonly="${view}" />
	<acme:textbox code="actor.email" path="email" readonly="${view}" />
	<acme:textbox code="actor.adress" path="adress" readonly="${view}" />
	<acme:textbox code="actor.photo" path="photo" readonly="${view}" />

	<jstl:if test="${view eq 'false'}">
		<acme:textbox code="actor.account.username" path="account.username" />
		<form:label path="account.password">
			<spring:message code="actor.account.password" />
		</form:label>
		<form:password path="account.password" />
		<form:errors path="account.password" cssClass="error" />
	</jstl:if>

	<jstl:if test="${view eq 'false'}">
		<br>
		<form:label path="password2">
			<spring:message code="actor.account.password2" />
		</form:label>
		<form:password path="password2" />
		
		<jstl:if test="${authority eq 'COMPANY'}">
			<acme:textbox code="actor.commercialName" path="commercialName" />
		</jstl:if>
		
		<jstl:if test="${authority eq 'PROVIDER'}">
			<acme:textbox code="actor.provider.make" path="make" />
		</jstl:if>

	</jstl:if>

	<jstl:if test="${!view}">
		<h3>
			<spring:message code="actor.creditCard"></spring:message>
		</h3>
		<acme:textbox code="creditCard.holder" path="creditCard.holder"
			readonly="${view}" />
		<spring:message code="creditCard.make"></spring:message>
		<form:select path="creditCard.make" disabled="${view}">
			<form:option value="0" label="---" disabled="${view}" />
			<form:options items="${makes}" disabled="${view}" />
		</form:select>
		<acme:textbox code="creditCard.number" path="creditCard.number"
			readonly="${view}" />
		<acme:textbox code="creditCard.date" path="creditCard.expiration"
			id="datepicker-1" readonly="${view}" />
		<acme:textbox code="creditCard.cvv" path="creditCard.cvv"
			readonly="${view}" />
	</jstl:if>
	<br>
	<jstl:if test="${actor.terms}">
		<form:hidden path="terms" />
	</jstl:if>
	<jstl:if test="${!actor.terms}">
		<form:checkbox path="terms" value="" />
		<a href="about-us/terms.do"><spring:message
				code="actor.terms.check" /></a>
	</jstl:if>
	<br>
	
	<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${actor.id != 0 and view}">
	<spring:message code="actor.spam"></spring:message> : <jstl:out value="${spammer }"></jstl:out>
	</jstl:if>
	</security:authorize>


	<jstl:if test="${actor.id != 0 and !view}">
		<h3>
			<spring:message code="actor.delete"></spring:message>
		</h3>
		<a href="actor/delete.do?id=${actor.id}"><spring:message
				code="actor.delete.link"></spring:message></a>

		<br>
		<a href="actor/export.do"><spring:message code="actor.export" /></a>
		<br>
	</jstl:if>
	<jstl:if test="${!view}">
		<input type="submit" name="save"
			value="<spring:message code="actor.save" />" />
	</jstl:if>
</form:form>
<acme:cancel code="actor.cancel" url="" />