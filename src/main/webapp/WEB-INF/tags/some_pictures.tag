<%@ tag language="java" body-content="empty"%>

<%-- Taglibs --%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%@ attribute name="items" required="true" type="java.util.Collection" %>
<%@ attribute name="titleKey" required="true"%>

<display:column titleKey="${titleKey}">
	<jstl:forEach items="${items}" var="p">
		<img src="${p}" alt="${p}" height="80" width="100" />
	</jstl:forEach>
</display:column>



