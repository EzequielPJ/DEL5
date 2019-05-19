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
		<td><spring:message code="custom.positionPerCompany" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAVGDttvPositionPerCompany")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAVGDttvPositionPerCompany")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAVGDttvPositionPerCompany")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAVGDttvPositionPerCompany")[3]}</td>
	</tr>
	<tr>
		<td><spring:message code="custom.applicationPerRookie" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfApplicationPerRookie")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfApplicationPerRookie")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfApplicationPerRookie")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfApplicationPerRookie")[3]}</td>
	</tr>

	<tr>
		<td><spring:message code="custom.aboutSalaries" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfSalary")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfSalary")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfSalary")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgDttvOfSalary")[3]}</td>
	</tr>

	<tr>
		<td><spring:message code="custom.curriculaPerRookie" /></td>
		<td>${marcadorNumericoArray.get("MinMaxAvgSttdvOfCurriculaPerRookie")[2]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgSttdvOfCurriculaPerRookie")[0]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgSttdvOfCurriculaPerRookie")[1]}</td>
		<td>${marcadorNumericoArray.get("MinMaxAvgSttdvOfCurriculaPerRookie")[3]}</td>
	</tr>



	<tr>
		<td><spring:message code="custom.NumberOfResultFinder" /></td>
		<td>${marcadorNumericoArray.get("NumberOfResultFinder")[2]}</td>
		<td>${marcadorNumericoArray.get("NumberOfResultFinder")[0]}</td>
		<td>${marcadorNumericoArray.get("NumberOfResultFinder")[1]}</td>
		<td>${marcadorNumericoArray.get("NumberOfResultFinder")[3]}</td>
	</tr>
</table>

<table border=1>

	<tr>
		<th></th>
		<th><spring:message code="custom.company" /></th>
	</tr>

	<tr>
		<td><spring:message code="custom.CompanyMorePositions" /></td>
		<td>${CompanyRookies.get("CompanyMorePositions")}</td>
	</tr>

</table>

<table border=1>

	<tr>
		<th></th>
		<th><spring:message code="custom.rookie" /></th>
	</tr>

	<tr>
		<td><spring:message code="custom.RookiesMoreApplication" /></td>
		<td>${CompanyRookies.get("RookiesMoreApplication")}</td>
	</tr>

</table>

<table border=1>

	<tr>
		<th></th>
		<th><spring:message code="custom.position" /></th>
	</tr>

	<tr>
		<td><spring:message code="custom.BestPosition" /></td>
		<td>${BestWorstPositionSalary.get("BestPosition")}</td>
	</tr>

	<tr>
		<td><spring:message code="custom.WorstPosition" /></td>
		<td>${BestWorstPositionSalary.get("WorstPosition")}</td>
	</tr>

</table>


<table border=1>

	<tr>
		<th></th>
		<th><spring:message code="custom.finder" /></th>
	</tr>

	<tr>
		<td><spring:message code="custom.EmptyVsNotEmpty" /></td>
		<td>${marcadorNumerico.get("EmptyVsNotEmpty")}</td>
	</tr>


</table>




