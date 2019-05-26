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

<table border="1">
	<tr>
		<th></th>
		<th><spring:message code="custom.min" /></th>
		<th><spring:message code="custom.avg" /></th>
		<th><spring:message code="custom.max" /></th>
		<th><spring:message code="custom.stddev" /></th>
	</tr>

	<tr>
		<td><spring:message code="custom.proclaimsByStudents" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByStudents")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByStudents")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByStudents")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByStudents")[3]}</td>
	</tr>
	<tr>
		<td><spring:message code="custom.proclaimsByMembers" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByMembers")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByMembers")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByMembers")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevProclaimsByMembers")[3]}</td>
	</tr>

	<tr>
		<td><spring:message code="custom.finderResults" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevFinderResults")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevFinderResults")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevFinderResults")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevFinderResults")[3]}</td>
	</tr>

	<tr>
		<td><spring:message code="custom.collaboratorPerComissions" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevCollaboratorPerComissions")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevCollaboratorPerComissions")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevCollaboratorPerComissions")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevCollaboratorPerComissions")[3]}</td>
	</tr>



	<tr>
		<td><spring:message code="custom.notesPerEvent" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevNotesPerEvent")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevNotesPerEvent")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevNotesPerEvent")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgStdevNotesPerEvent")[3]}</td>
	</tr>
</table>

<table border=1>

	<tr>
		<th></th>
		<th><spring:message code="custom.events" /></th>
	</tr>

	<tr>
		<td><spring:message code="custom.nearestEvents" /></td>
		<td>${nearestEvents.get("nearestEvents")}</td>
	</tr>

</table>

<table border=1>

	<tr>
		<th></th>
		<th><spring:message code="custom.ratio" /></th>
	</tr>

	<tr>
		<td><spring:message code="custom.ratioMemberComission" /></td>
		<td>${marcadorNumerico.get("ratioMemberComission")}</td>
	</tr>


</table>




