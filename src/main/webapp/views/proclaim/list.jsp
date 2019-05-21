<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="proclaims" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column titleKey="proclaim.status" sortable="true">
		<jstl:out value="${row.status}" />
	</display:column>

	<display:column titleKey="proclaim.show">
		<a href=""><spring:message code="proclaim.show" /></a>
	</display:column>

	<display:column titleKey="proclaim.title">
		<jstl:out value="${row.title}" />
	</display:column>

	<display:column titleKey="proclaim.category" sortable="true">
		<jstl:out value="${row.category.name}" />
	</display:column>
	<display:column titleKey="proclaim.edit">
		<a href=""><spring:message code="proclaim.edit" /></a>
	</display:column>
</display:table>

<script>
	var table = document.getElementById("row");
	var tbody = table.getElementsByTagName("tbody")[0];
	var row = tbody.getElementsByTagName("tr");

	for (i = 0; i < row.length; i++) {
		var value = row[i].getElementsByTagName("td")[0].firstChild.nodeValue;
		if (value == 'ACCEPTED') {
			row[i].style.backgroundColor = "#00FF80";
		} else if (value == 'REJECTED') {
			row[i].style.backgroundColor = "#FF8000";
		} else if (value == 'PENDING') {
		} else if (value == 'SUBMITTED') {
			row[i].style.backgroundColor = "#9C9C9C";
		}
	}
</script>