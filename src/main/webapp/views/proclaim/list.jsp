<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<display:table name="proclaims" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	<jstl:if test="${!finderColumns}">
		<display:column titleKey="proclaim.status" sortable="true">
			<jstl:out value="${row.status}" />
		</display:column>
	</jstl:if>
	<jstl:if test="${finderColumns}">
		<display:column titleKey="proclaim.status">
			<jstl:out value="${row.status}" />
		</display:column>
	</jstl:if>
	<display:column titleKey="proclaim.show">
		<security:authorize access="hasRole('STUDENT')">
			<a href="proclaim/student/show.do?id=${row.id}"><spring:message
					code="proclaim.show" /></a>
		</security:authorize>
		<security:authorize access="hasRole('MEMBER')">
			<jstl:if test="${boton}">
				<a href="proclaim/member/show.do?id=${row.id}"><spring:message
						code="proclaim.show" /></a>
			</jstl:if>
		</security:authorize>
	</display:column>

	<display:column titleKey="proclaim.title">
		<jstl:out value="${row.title}" />
	</display:column>

	<jstl:if test="${finderColumns}">
		<display:column titleKey="proclaim.category">
			<jstl:out value="${row.category.name}" />
		</display:column>
	</jstl:if>
	<jstl:if test="${!finderColumns}">
		<display:column titleKey="proclaim.category" sortable="true">
			<jstl:out value="${row.category.name}" />
		</display:column>
	</jstl:if>
	<display:column titleKey="proclaim.edit">
		<security:authorize access="hasRole('STUDENT')">
			<jstl:if test="${row.finalMode eq 'false'}">
				<a href="proclaim/student/edit.do?id=${row.id}"><spring:message
						code="proclaim.edit" /></a>
			</jstl:if>
		</security:authorize>
		<security:authorize access="hasRole('MEMBER')">
			<jstl:if test="${boton}">
				<a href="proclaim/member/edit.do?id=${row.id}"><spring:message
						code="proclaim.edit" /></a>
			</jstl:if>
		</security:authorize>
	</display:column>
	<jstl:if test="${startAssignation}">
		<display:column titleKey="proclaim.assign">
			<jstl:if test="${!fn:contains(ass,row)}">
				<security:authorize access="hasRole('MEMBER')">
					<a href="proclaim/member/assign.do?id=${row.id}"><spring:message
							code="proclaim.assign" /></a>
				</security:authorize>
			</jstl:if>
		</display:column>
	</jstl:if>


	<jstl:if test="${row.status == 'ACCEPTED' or row.status == 'ACEPTADO'}">

		<display:column titleKey="proclaim.commentaries">
			<security:authorize access="hasRole('MEMBER')">
				<a href="comment/member/list.do?id=${row.id}"><spring:message
						code="proclaim.commentaries" /></a>
			</security:authorize>
			<security:authorize access="hasRole('STUDENT')">
				<jstl:if test="${!proclaim.closed}">
					<a href="comment/student/list.do?id=${row.id}"><spring:message
							code="proclaim.commentaries" /></a>
				</jstl:if>
			</security:authorize>
		</display:column>

		<display:column titleKey="proclaim.createCommentary">
			<security:authorize access="hasRole('MEMBER')">
				<a href="comment/member/create.do?id=${row.id}"><spring:message
						code="proclaim.createCommentary" /></a>
			</security:authorize>
			<security:authorize access="hasRole('STUDENT')">
				<jstl:if test="${!proclaim.closed}">
					<a href="comment/student/create.do?id=${row.id}"><spring:message
							code="proclaim.createCommentary" /></a>
				</jstl:if>
			</security:authorize>

		</display:column>

	</jstl:if>

</display:table>

<jstl:out value="${asignedYet}" />

<script>
	var table = document.getElementById("row");
	var tbody = table.getElementsByTagName("tbody")[0];
	var row = tbody.getElementsByTagName("tr");

	for (i = 0; i < row.length; i++) {

		var value = trim(row[i].getElementsByTagName("td")[0].firstChild.nodeValue);
		console.log(value);
		if (value == 'ACCEPTED' || value == 'ACEPTADO') {
			row[i].style.backgroundColor = "#00FF80";
		} else if (value == 'REJECTED' || value == 'RECHAZADO') {
			row[i].style.backgroundColor = "#FF8000";
		} else if (value == 'PENDING' || value == 'PENDIENTE') {
			row[i].style.backgroundColor = "#33E6FF";
		} else if (value == 'SUBMITTED' || value == 'ENVIADO') {
			row[i].style.backgroundColor = "#9C9C9C";
		}
	}

	function trim(cadena) {
		// USO: Devuelve un string como el
		// parametro cadena pero quitando los
		// espacios en blanco de los bordes.

		var retorno = cadena.replace(/^\s+/g, '');
		retorno = retorno.replace(/\s+$/g, '');
		return retorno;
	}
</script>