<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<div style="align-content: center">
	<br>
    <h1>Hello world!</h1>

	<form method="post" action="/makeThread">
		<input type="text" name="name" placeholder="thread name" value="">
		<input type="submit" value="제출">
	</form>

	<table>
		<thead>
		<tr>
			<th>이름</th>
			<th>상태</th>
			<th>중지버튼</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="map" items="${mapList}" varStatus="status">
			<tr>
				<td>${map.name}</td>
				<td>${map.status}</td>
				<td><button onclick="location.href = '/stopThread?name=${map.name}'">중지</button></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
</body>
</html>
