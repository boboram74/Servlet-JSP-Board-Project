<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 작성하기</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

</head>
<body>
    <form action="/create.board" method="post" id="frm" enctype="multipart/form-data"> <%-- 파일을 보낼때 2가지 조건 1.post 방식 2.enctype 타입으로 전송 --%>
    <table border="1" width="800px" height="500px" align="center">
        <tr align="center">
            <th colspan="2">글 작성하기</th>
        </tr>
        <tr align="center">
            <td width="80px">
                <select style="width: 100%; height: 100%; border: none;">
               		<option>게시판</option>
                    <option>공지사항</option>
                </select>
            </td>
            <td align="left">
               <input type="text" id="title" name="title" placeholder="제목을 입력하세요." style="width:80%; height: 100%; border: none;">
            </td>
        </tr>
        <tr>
        	<td style="border: none;">
        		<input type="file" name="file1"> 
        		<input type="file" name="file2">
        	</td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <textarea name="contents" id="contents" cols="100" rows="20" placeholder="내용을 입력하세요" style="resize: none; height: 97%; width: 99%; border: none;"></textarea>
            </td>
        </tr>
        <tr>
            <td align="right" colspan="2">
            <button id="complete">작성완료</button>
            <a href="/list.board"><button type="button">목록으로</button> </a>
        </td>
        </tr>
        </table>
	</form>
	<script>
		$("#frm").on("submit",function () {
			if($("#title").val() == "") {
				alert("제목을 입력해주세요!");
				$("#title").focus();
				return false;
			} else if ($("#contents").val() == "") {
				alert("내용을 입력해주세요!")
				$("#contents").focus();
				return false;
			} 
		});
		
	</script>
</body>
</html>