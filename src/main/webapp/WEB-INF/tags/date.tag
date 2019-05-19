<%--
 * select.tag
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty"%>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<link href="styles/date.css" rel="stylesheet">
<%-- Attributes --%>

<%@ attribute name="path" required="true"%>
<%@ attribute name="code" required="true"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="readonly" required="false"%>
<%@ attribute name="start" required="false"%>
<%@ attribute name="end" required="false"%>

<%-- Definition --%>

<script type="text/javascript">
	$(function() {
		var start = "${start}";
		var end = "${end}";

		var id = "#" + "${id}";

		if (start == "" && end == "") {
			$(id).datepicker({
				appendText : "(yy/mm/dd)",
				dateFormat : "yy/mm/dd"
			});
		} else if (start != "" && end == "") {
			$(id).datepicker({
				appendText : "(yy/mm/dd)",
				dateFormat : "yy/mm/dd",
				minDate : new Date(start)
			});
		} else if (start == "" && end != ""){
			$(id).datepicker({
				appendText : "(yy/mm/dd)",
				dateFormat : "yy/mm/dd",
				maxDate : new Date(end)
			});
		} else {
			$(id).datepicker({
				appendText : "(yy/mm/dd)",
				dateFormat : "yy/mm/dd",
				minDate : new Date(start),
				maxDate : new Date(end)
			});
		}

	});
</script>
<div>
	<form:label path="${path}">
		<spring:message code="${code}" />
	</form:label>
	<form:input path="${path}" type="text" id="${id}"
		readonly="${readonly}" />
	<form:errors path="${path}" cssClass="error" />
</div>