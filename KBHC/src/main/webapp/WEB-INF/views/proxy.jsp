<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

	<%@ include file="/WEB-INF/views/include/common/head.jsp" %>
	
	<body class="sb-nav-fixed">
	
		<%@ include file="/WEB-INF/views/include/common/sb-topnav.jsp" %>

		<div id="layoutSidenav">
		
			<%@ include file="/WEB-INF/views/include/common/layoutSidenav_nav.jsp" %>
			
			<div id="layoutSidenav_content">
				
<main>
	<div class="container-fluid px-4">

    <form id="proxyTestForm">
        <!-- Input field for the 'url' parameter -->
        <label for="urlInput">URL:</label>
        <input type="text" id="urlInput" name="url" required change="handleEnterKey(event)" onfocus="">
        <!-- Button to trigger the AJAX request -->
        <button type="button" onclick="sendProxyTestRequest()">Send Request</button>
        <hr>
    </form>

    <!-- Display the test results -->
    <div id="testResults"></div>
	
<!-- jQuery를 포함하는 CDN 링크 -->
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

<script>
var urlValue;

$("#urlInput").on("propertychange change paste input", function() {
	console.log(urlValue)
	sendProxyTestRequest();
})
function sendProxyTestRequest() {
    // Get the value from the 'urlInput' field
    urlValue = $('#urlInput').val();

    // Build the URL with the 'url' parameter
    var url = '/proxytest?url=' + encodeURIComponent(urlValue);

    // Send an AJAX request to the 'proxytest' endpoint using jQuery
    $.ajax({
        type: 'POST',
        url: url,
        contentType: 'application/x-www-form-urlencoded',
        success: function (data, textStatus, xhr) {
            // Handle the response and display it on the page
            var testResultsDiv = $('#testResults');
            testResultsDiv.html('<h2>Test Results:</h2>');
            //testResultsDiv.append('<p>HTTP Response Code: ' + xhr.status + '</p>');
            testResultsDiv.append('<p>' + data + '</p>');
        },
        error: function (xhr, textStatus, errorThrown) {
            // Handle errors if needed
            console.error('Error occurred during the request');
        }
    });
}
</script>	

	</div>
</main>
				
				<%@ include file="/WEB-INF/views/include/common/footer.jsp" %>
			</div>
		</div>
		<%@ include file="/WEB-INF/views/include/common/script.jsp" %>
	</body>
</html>
