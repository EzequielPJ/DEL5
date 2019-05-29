<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<script type="text/JavaScript">

$(document).ready(
    function() {
      $("#form").submit(
function checkStuff()
{
   var check1=confirm("Are you sure you want to save it?");
   if (check1)
   {
      var check2=confirm("Are you 100% sure?");
      if (check2)
      {
         return true;
      }
      else return false;
   }
   else return false;
}
    );
    });

</script>
<form:form action="${requestURI}" modelAttribute="notes" id="form">

	<form:hidden path="id"/>
	<form:hidden path="event"/>

	<acme:textbox code="notes.note" path="note" readonly="${view}" />
	
	<acme:textbox code="notes.description" path="description"
		readonly="${view}" />
		
	<jstl:if test="${view}">
		<spring:message code="notes.actor" />
		<jstl:out value=" --> ${notes.actor.name}" />
	</jstl:if>
	
	<jstl:if test="${!view}">
		<acme:submit name="save" code="user.save" />
	</jstl:if>

</form:form>

<acme:cancel url="${requestCancel}" code="notes.cancel" />
