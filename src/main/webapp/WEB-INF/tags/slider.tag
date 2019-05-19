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

<%@ attribute name="items" required="true" type="java.util.Collection"%>

<style>
#slider {
	position: relative;
	overflow: hidden;
	border-radius: 4px;
}

#slider ul {
	position: relative;
	margin: 0;
	padding: 0;
	height: 200px;
	list-style: none;
}

#slider ul li {
	position: relative;
	display: block;
	float: left;
	margin: 0;
	padding: 0;
	width: 200px;
	height: 150px;
	text-align: center;
	line-height: 300px;
}
</style>

<script>
	jQuery(document).ready(function($) {

		setInterval(function() {
			moveRight();
		}, 3000);

		var slideCount = $('#slider ul li').length;
		var slideWidth = $('#slider ul li').width();
		var slideHeight = $('#slider ul li').height();
		var sliderUlWidth = slideCount * slideWidth;

		$('#slider').css({
			width : slideWidth,
			height : slideHeight
		});

		$('#slider ul').css({
			width : sliderUlWidth,
			marginLeft : -slideWidth
		});

		$('#slider ul li:last-child').prependTo('#slider ul');

		function moveLeft() {
			$('#slider ul').animate({
				left : +slideWidth
			}, 200, function() {
				$('#slider ul li:last-child').prependTo('#slider ul');
				$('#slider ul').css('left', '');
			});
		}
		;

		function moveRight() {
			$('#slider ul').animate({
				left : -slideWidth
			}, 200, function() {
				$('#slider ul li:first-child').appendTo('#slider ul');
				$('#slider ul').css('left', '');
			});
		}
		;

	});
</script>

<div id="slider">
	<ul>
		<jstl:forEach items="${items}" var="s">
			<li><img src="${s.banner}" height="150" width="200" /></li>
		</jstl:forEach>
	</ul>
</div>