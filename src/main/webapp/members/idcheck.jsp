<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>idcheck</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

</head>
<body>

	<c:choose>
	<c:when test="${result == true}">
			<table border="1" style="width:100%; height:150px;">
				<tr>
					<th>중복체크 결과
				</tr>
				
				<tr>
					<td>이미 사용중인 ID입니다.
				</tr>
				
				<tr>
					<td align="center"><button id="close">닫기</button> </td>
				</tr>
			</table>
	</c:when>
	
	<c:otherwise>
			<table border="1" style="width:100%; height:150px;">
				<tr>
					<th colspan="2">중복체크 결과
				</tr>
				
				<tr>
					<td colspan="2">사용 가능한 ID입니다. 사용하시겠습니까?
				</tr>
				
				<tr>
					<td align="center"><button id="use">사용</button> </td>
					<td align="center"><button id="cancel">취소</button> </td>
				</tr>
			</table>
	</c:otherwise>
	
	</c:choose>
	
	<script>
		/*ID가 중복일때 opner(signup의 id val을 지움)*/
		$("#close").on("click",function() {
			opener.document.getElementById("id").value="";
			window.close();
		});
		
		$("#use").on("click",function() {
			window.close();
		});
		
		$("#cancel").on("click",function() {
			opener.document.getElementById("id").value="";
			window.close();
		});
	</script>

</body>
</html>