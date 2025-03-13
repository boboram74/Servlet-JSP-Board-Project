<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원게시판</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<style>
	.paging {
		border:1px solid black;
		cursor:pointer;
		display:inline-block;
		width:20px;
		height:20px;
		margin: auto;
		padding: auto;
	}
</style>

<script>
	$(function() {
		let cpage = localStorage.getItem("page") || 1;
		$.ajax({
			url:"/ajax_list.board",
			data: { cpage: cpage }
		}).done(function(resp) {
			resp = JSON.parse(resp);
			console.log(resp);
			let list = resp.list;
			
			for(let i = 0; i < list.length; i++) {
				let tr = $("<tr>");
				
				let seq = $("<td>");
				seq.append(list[i].seq);
				
				let titleLink = $("<a>").attr("href", "detail.board?seq=" + list[i].seq + "&rpage=1").text(list[i].title);
				
				let title = $("<td>").append(titleLink);
				
				let writer = $("<td>");
				writer.append(list[i].writer);
				
				let date = $("<td>");
				date.append(list[i].date);
				console.log(list[i].date);
				
				let view = $("<td>");
				view.append(list[i].view);
				
				tr.append(seq,title,writer,date,view);
				$("#list_tr").append(tr);
			}
		});
	}); //페이지가 실행 된 후
</script>

</head>
<body>
	<table border="1" width="800px" align="center" id="boardTable">
		<tr>
			<th colspan="5">자유게시판</th>
		</tr>
		<tr align="center">
			<th width="5%">no.</th>
			<th width="50%">제목</th>
			<th width="10%">작성자</th>
			<th width="10%">날짜</th>
			<th width="10%">조회수</th>
		</tr>
		<tbody id="list_tr"> <%-- 동적생성 tr추가를 위하여 tbody 추가--%>
    	</tbody>
		
		<tr>
			<td colspan="5" align="center">
				<c:if test="${needPrev}">
					<span class="paging" page="${startNavi-1}">< </span>
				</c:if> 
				<c:forEach var="i" begin="${startNavi}" end="${endNavi}">
					<span class="paging" page="${i}"> ${i}</span>
				</c:forEach> 
					<c:if test="${needNext}">
					<span class="paging" page="${endNavi+1}"> ></span>
				</c:if></td>
		</tr>
		
		<tr>
			<td colspan="5" align="right">
				<button id="btn2" type="button" onclick="location.href='/index.jsp'">홈으로</button>
				<button id="btn1" type="button">작성하기</button>
			</td>
		</tr>
	</table>
	<script>
    let check = function() { //게시글 작성 전 로그인 검증
    	let result = true;
    	if(${dto == null}) {
    		result = false;
    	}
    	return result;
    }
   	$("#btn1").on("click",function() {
    	if (check() == false) {
    		alert("로그인을 먼저 해주세요.");
    		location.href = "/index.jsp";
    	return false;
    	} else {
    		location.href = "toWriter.board";	
    	}
    });
   	$(".paging").on("click",function() {
   		let pageNum = $(this).attr("page");
   		localStorage.setItem("page",pageNum);
   		location.href="/list.board?cpage="+pageNum;
   	});

    </script>
</body>
</html>