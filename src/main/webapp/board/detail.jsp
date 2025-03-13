<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 상세보기</title>
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
</head>
<body>
	<form action="/update.board" method="post" id="frm">
	<input type="hidden" name="seq" value="${boardDto.getSeq()}">
	<input type="hidden" name="title" id="hiddenTitle">
	<input type="hidden" name="contents" id="hiddenContents">
	
    <table border="1" width="800px" height="300px" align="center">
        <tr>
            <th colspan="6">게시글 상세보기</th>
        </tr>
        <tr>
			<td colspan="1" style="width: 15%;">작성자</td>
			<td colspan="5" style="width: 85%;">${boardDto.writer}</td>
        </tr>
        <tr>
            <td>작성일</td>
            <td colspan="3">${boardDto.date}</td>
        </tr>
        <tr>
            <td>조회수</td>
            <td colspan="3">${boardDto.view}</td>
        </tr>
        <tr>
            <td>제목</td>
            <td class="editable" id="title" colspan="3">${boardDto.title}</td>
        </tr>
        <tr>
        	<td>첨부파일</td>
        		<td colspan="5">
		        	<c:forEach var="i" items="${files}">
        				<a href="/download.files?filename=${i.sysName}&oriname=${i.originName}">${i.originName}</a>
        			</c:forEach>
        		</td>
        </tr>
        <tr>
            <td>내용</td>
            <td>
				<textarea id="contents" class="editable" rows="10" style="width: 100%; box-sizing: border-box; resize: none;" readonly>${boardDto.contents}</textarea>
			</td>
        </tr>

         <tr>
            <td colspan="3" align="right" class="btn-area">
            <c:if test="${boardDto.getWriter() == dto.getId()}">
            		<button id="delete-btn" type="button">삭제하기</button>
            		<button id="update-btn" type="button">수정하기</button>
            	<script>
           		$("#delete-btn").on("click", function() {
           			if(confirm("정말 삭제하시겠습니까?") == true) {
                		location.href = "/delete.board?seq=${boardDto.getSeq()}";
           			} else {
           				location.href = "/detail.board?seq=${boardDto.getSeq()}";
           			}
            	});
           		$("#update-btn").on("click", function() {
           			$(".editable").attr("contenteditable","true");
           			$(".editable").focus();
           			$("textarea").removeAttr("readonly");
           			
           			$("#update-btn,#delete-btn,#back").css("display","none");
           			
           			let updateOk = $("<button>");
           			updateOk.html("수정완료");
           			
           			let updateCancel = $("<button>").attr("type","button");
           			updateCancel.html("취소");
           			
           			updateCancel.on("click", function() {
           				location.reload();
           			});
           			$(".btn-area").append(updateOk,updateCancel);
           		});
            	</script>
             </c:if>
            <button type="button" id="back">목록으로</button>
              </td>
        </tr>
    </table>
    </form>

<form action="/create.reply" method="post" id="frm2">
    <input type="hidden" name="parent_seq" value="${boardDto.getSeq()}">
    	<table border="1" width="800px" height="50px" align="center">
	    	<tr class="replyContainer" style="height: 70px;">        
				<td class="writer">작성자</td>
				<td class="reply" style="height: 100%; width: 70%;"> 
					<textarea id="replyText" name="reply" class="replyContents" placeholder="댓글을 입력하세요" style="width: 100%; height: 100%; box-sizing: border-box; resize: none; border: none;"></textarea>
	        	</td>
	        	<td class="reply_2" style="height: 100%; width: 15%;">
					<button type="submit" id="replyBtn" style="width: 100%; height: 100%;">Reply</button>
	        	</td>
	        </tr>
	        
    	</table>
</form>

<form action="/update.reply" method="post" id="reply_frm">

	<input type="hidden" name="id" id="hiddenReplyId">
	<input type="hidden" name="contents" id="hiddenReplyContents">
	<input type="hidden" name="parent_seq" value="${boardDto.getSeq()}">
	
	<table border="1" width="800px" height="50px" align="center">
	    <thead>
        	<tr>
        		<th>no.</th>
	            <th style="width: 15%;">작성자</th>
	            <th style="width: 70%;">내용</th>
	            <th style="width: 15%;">작성일</th>
	            <th></th>
			</tr>
    	</thead>
    	<tbody>
			<c:forEach var="item" items="${reply}">
				<tr>
	            	<td>${item.id}</td>
	        		<td>${item.writer}</td>
	        		<td class="reply_contents-${item.id}">${item.contents}</td>
	        		<td>${item.write_date}</td>
	        		<c:if test="${item.writer == loginUser}">
	        		<td class="reply_button_area-${item.id}">
                    	<button type="button" id="update_reply_btn-${item.id}">수정</button>
                        <button type="button" id="delete_reply_btn-${item.id}">삭제</button>
                        <script>
                   		$(`#delete_reply_btn-${item.id}`).on("click", function() {
                   			if(confirm("정말 삭제하시겠습니까?") == true) {
                        		location.href = "/delete.reply?id=${item.id}&seq=${boardDto.seq}";
                   			} else {
                   				location.href = "/detail.board?seq=${boardDto.seq}";
                   			}
                    	});
                   		$(`#update_reply_btn-${item.id}`).on("click", function() {
                   			$(".reply_contents-${item.id}").attr("contenteditable","true");
                   			$(".reply_contents-${item.id}").focus();
                   			
                   			$("#update_reply_btn-${item.id},#delete_reply_btn-${item.id}").css("display","none");
                   			
                   	        let updateOk = $("<button>").attr("type", "submit").html("완료");
                   	        let updateCancel = $("<button>").attr("type", "button").html("취소");
                   	        
                   			updateCancel.on("click", function() {
                   				location.reload();
                   			});
                   			
                   			$(".reply_button_area-${item.id}").append(updateOk,updateCancel);
                   			
	                   		updateOk.on("click", function() {
                                $("#hiddenReplyContents").val($(`.reply_contents-${item.id}`).html());
	               				$("#hiddenReplyId").val("${item.id}");
	               				$("#reply_frm").submit();
	               				});
                   		});
                        </script>
	        		</td>
	        		</c:if>
        		</tr>
        	</c:forEach>
        	
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
		</tbody>
	</table>
</form>
    <script>
		$("#frm").on("submit",function() {
			$("#hiddenTitle").val($("#title").html());
			$("#hiddenContents").val($("#contents").val());
		});
		$("#frm2").on("submit",function() {
			if($("#replyText").val() == "") {
				$("#replyText").focus();
				alert("댓글 내용을 입력해주세요!");
				return false;
			}
		});
		$("#back").on("click",function() {
			let page = localStorage.getItem("page");
 			location.href="/list.board?cpage="+page;
		});
	   	$(".paging").on("click",function() {
	   		let pageNum = $(this).attr("page");
	   		location.href="/detail.board?seq=${seq}&rpage="+pageNum;
	   	});
	</script>
</body>
</html>