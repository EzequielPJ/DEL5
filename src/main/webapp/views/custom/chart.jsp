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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<input type="hidden" id="p" value='${histogram}' />

<div style="width: 25%">
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.bundle.js"></script>

<script type="text/javascript">
	var labels = [];
	var dat = [];

	var pos = $("#p").val();
	console.log(pos);
	var str = pos.split(',');
	var element;

	for ( var e in str) {
		var s = str[e];
		if (s.startsWith('{')) {
			s = s.replace('{', '');
		}
		if (s.endsWith('}')) {
			s = s.replace('}', '');
		}
		element = s.split('=');
		labels.push(element[0]);
		dat.push(element[1]);
	}

	var data = {
		labels : [

		],
		datasets : [
			{
				label : "Positions",
				data : [

				]
			}
		]
	};

	for ( var i = 0; i < labels.length; i++) {
		data["labels"].push(labels[i]);
		data["datasets"][0]["data"].push(dat[i]);
	}

	var options = {
		scales : {
			yAxes : [
				{
					ticks : {
						suggestedMin : 0
					}
				}
			]
		}
	};

	$(document).ready(function() {
		var canvas, context;

		canvas = document.getElementById('canvas');
		context = canvas.getContext('2d');
		window.myBar = new Chart(context, {
			type : 'bar',
			data : data,
			options : options
		});
	});
</script>