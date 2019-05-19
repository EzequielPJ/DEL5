<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="profile">
	<form:hidden path="id" />
	<acme:textbox code="user.nick" path="nick" readonly="${view}" />
	<acme:textbox code="user.socialNetworkName" path="socialNetworkName"
		readonly="${view}" />
	<acme:textbox code="user.link" path="link" readonly="${view}" />
	<jstl:if test="${!view}">
		<acme:submit name="save" code="user.save" />
	</jstl:if>
</form:form>

<acme:cancel url="${requestCancel}" code="profile.cancel" />