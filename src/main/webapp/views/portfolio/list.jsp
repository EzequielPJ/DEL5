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


<jstl:if test="${!port}">
<p><spring:message code="portfolio.info"/></p>
<input type="button" name="initial" value="<spring:message code="portfolio.crear"/>"
onclick="javascript:relativeRedir('portfolio/create.do');"/>
</jstl:if>


<jstl:if test="${!lista}">
<display:table name="portfolio" id="row"
	requestURI="${requestURI}" pagesize="5"
	class="displaytag">
	
	<display:column titleKey="portfolio.title">
		<jstl:out value="${row.title}" />
	</display:column>
	<display:column titleKey="portfolio.fullName">
		<jstl:out value="${row.fullName}" />
	</display:column>
	<display:column titleKey="portfolio.moment">
		<jstl:out value="${row.moment}" />
	</display:column>
	<display:column titleKey="portfolio.address">
		<jstl:out value="${row.address}" />
	</display:column>
	<display:column titleKey="portfolio.phone">
		<jstl:out value="${row.phone}" />
	</display:column>
	
	
		<display:column titleKey="portfolio.update">
		<a href="portfolio/edit.do?id=${row.id}"><spring:message
				code="portfolio.update" /></a>
		</display:column>
</display:table>
<h2><spring:message code="portfolio.wr" /></h2>


<display:table name="WorkReport" id="row"
	requestURI="${requestURI}" pagesize="5"
	class="displaytag">
	
	<display:column titleKey="portfolio.title">
		<jstl:out value="${row.title}" />
	</display:column>
	<display:column titleKey="portfolio.wp.bussinesName">
		<jstl:out value="${row.businessName}" />
	</display:column>
		<display:column titleKey="portfolio.moment">
		<jstl:out value="${row.moment}" />
	</display:column>
		<display:column titleKey="portfolio.wp.startDate">
		<jstl:out value="${row.startDate}" />
	</display:column>
	<display:column titleKey="portfolio.wp.endDate">
		<jstl:out value="${row.endDate}" />
	</display:column>
	<display:column titleKey="portfolio.text">
		<jstl:out value="${row.text}" />
	</display:column>
	
			<display:column titleKey="portfolio.showMore">
		<a href="workReport/show.do?id=${row.id}"><spring:message
				code="portfolio.showMore" /></a>
		</display:column>
	
		<display:column titleKey="portfolio.delete">
		<a href="workReport/delete.do?id=${row.id}"><spring:message
				code="portfolio.delete" /></a>
		</display:column>
		
		<display:column titleKey="portfolio.update">
		<a href="workReport/edit.do?id=${row.id}"><spring:message
				code="portfolio.update" /></a>
		</display:column>
	
</display:table>

<p><spring:message code="workReport.info"/></p>
<input type="button" name="initial" value="<spring:message code="wr.crear"/>"
onclick="javascript:relativeRedir('workReport/create.do');"/>


<h2><spring:message code="portfolio.st" /></h2>
<display:table name="StudyReport" id="row"
	requestURI="${requestURI}" pagesize="5"
	class="displaytag">
	
	<display:column titleKey="portfolio.title">
		<jstl:out value="${row.title}" />
	</display:column>
	<display:column titleKey="portfolio.moment">
		<jstl:out value="${row.moment}" />
	</display:column>
	<display:column titleKey="portfolio.st.course">
		<jstl:out value="${row.course}" />
	</display:column>
	<display:column titleKey="portfolio.wp.startDate">
		<jstl:out value="${row.startDate}" />
	</display:column>
	<display:column titleKey="portfolio.wp.endDate">
		<jstl:out value="${row.endDate}" />
	</display:column>
	<display:column titleKey="portfolio.st.average">
		<jstl:out value="${row.average}" />
	</display:column>
	<display:column titleKey="portfolio.st.percentajeCredits">
		<jstl:out value="${row.percentajeCredits}" />
	</display:column>
	
				<display:column titleKey="portfolio.showMore">
		<a href="studyReport/show.do?id=${row.id}"><spring:message
				code="portfolio.showMore" /></a>
		</display:column>
	
		<display:column titleKey="portfolio.delete">
		<a href="studyReport/delete.do?id=${row.id}"><spring:message
				code="portfolio.delete" /></a>
		</display:column>
		
		<display:column titleKey="portfolio.update">
		<a href="studyReport/edit.do?id=${row.id}"><spring:message
				code="portfolio.update" /></a>
		</display:column>
	
</display:table>
<p><spring:message code="studyReport.info"/></p>
<input type="button" name="initial" value="<spring:message code="st.crear"/>"
onclick="javascript:relativeRedir('studyReport/create.do');"/>


<h2><spring:message code="portfolio.ms" /></h2>
<display:table name="MiscellaneousReport" id="row"
	requestURI="${requestURI}" pagesize="5"
	class="displaytag">
	
	<display:column titleKey="portfolio.title">
		<jstl:out value="${row.title}" />
	</display:column>
	<display:column titleKey="portfolio.moment">
		<jstl:out value="${row.moment}" />
	</display:column>
	<display:column titleKey="portfolio.text">
		<jstl:out value="${row.text}" />
	</display:column>
	<display:column titleKey="portfolio.ms.attachments">
		<jstl:out value="${row.attachments}" />
	
	</display:column>
					<display:column titleKey="portfolio.showMore">
		<a href="miscellaneousReport/show.do?id=${row.id}"><spring:message
				code="portfolio.showMore" /></a>
		</display:column>
	
		<display:column titleKey="portfolio.delete">
		<a href="miscellaneousReport/delete.do?id=${row.id}"><spring:message
				code="portfolio.delete" /></a>
		</display:column>
		
		<display:column titleKey="portfolio.update">
		<a href="miscellaneousReport/edit.do?id=${row.id}"><spring:message
				code="portfolio.update" /></a>
		</display:column>
	
	
</display:table>
<p><spring:message code="miscellaneousReport.info"/></p>
<input type="button" name="initial" value="<spring:message code="ms.crear"/>"
onclick="javascript:relativeRedir('miscellaneousReport/create.do');"/>

</jstl:if>
