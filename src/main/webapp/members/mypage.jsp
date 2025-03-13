<%--    --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Mypage</title>
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
</head>

<body>
	<form action="/update.members" method="post" id="frm">
	<input type="hidden" name="id" value="${dto.getId()}">
	<input type="hidden" name="name" id="hiddenName">
	<input type="hidden" name="email" id="hiddenEmail">
	<input type="hidden" name="tel" id="hiddenTel">	
	<input type="hidden" name="post" id="hiddenPost">	
	<input type="hidden" name="address1" id="hiddenAddress1">	
	<input type="hidden" name="address2" id="hiddenAddress2">	
	
	
    <button id="back" style="display: block; margin:0 auto;">뒤로가기</button>
    <table border="1" style="margin: auto;">
        <tr>    
            <th>ID</th>
            <th>NAME</th>
            <th>EMAIL</th>
            <th>PHONE</th>
            <th>POSTCODE</th>
            <th>Address</th>
            <th>Detail Address</th>
            <th>Create Account Date</th>
            <th></th>
        </tr>
            <tr>
                <td>${dto.getId()}</td>
                <td class="editable" id="name">${dto.getName()}</td>
                <td class="editable" id="email">${dto.getEmail()}</td>
                <td class="editable" id="tel">${dto.getTel()}</td>
                <td class="editable" id="post">${dto.getPost()}</td>
                <td class="editable" id="address1">${dto.getAddress1()}</td>
                <td class="editable" id="address2">${dto.getAddress2()}</td>
                <td>${dto.getDate()}</td>
                <td class="btn-area"><button id="update" type="button" style="width:100%; heigh:100%;">수정하기</button> </td>
            </tr>
    </table>
    </form>
    
    <script>
		$("#update").on("click", function() {
   			$(".editable").attr("contenteditable","true");
   			$(".editable").focus();
   			
   			$("#update").css("display","none");
   			
   			let updateOk = $("<button>");
   			updateOk.html("수정완료");
   			
   			let updateCancel = $("<button>").attr("type","button");
   			updateCancel.html("취소");
   			
   			updateCancel.on("click", function() {
   				location.reload();
   			});
   			$(".btn-area").append(updateOk,updateCancel);
    		
    	});
		$("#frm").on("submit",function() {
			$("#hiddenName").val($("#name").html());
			$("#hiddenEmail").val($("#email").html());
			$("#hiddenTel").val($("#tel").html());
			$("#hiddenPost").val($("#post").html());
			$("#hiddenAddress1").val($("#address1").html());
			$("#hiddenAddress2").val($("#address2").html());

		});
		$("#back").on("click", function() {
			location.href = "/";
    	});
		
		
    </script>

</body>
</html>