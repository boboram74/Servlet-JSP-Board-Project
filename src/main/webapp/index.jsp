<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script> <%-- 카카오 SDK --%>


<title>Index.jsp</title>

    <style>
        * {
            box-sizing: border-box;
        }

        div {
            border: 1px solid black;
        }

        .container {
            width: 200px;
            height: 150px;
            margin: auto;
        }
        form {
            height: 100%;
        }
        form>.title {
            height: 20%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        form>.id {
            height: 15%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        form>div>input {
            width: 100%;
            height: 100%;
        }
        form>.pw {
            height: 15%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        form>.buttons {
            height: 20%;
            display: flex;
            justify-content: space-evenly;
        }
        form>.check {
            height: 30%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        form>.check>input {
            width: auto;
        }

    </style>
</head>
<body> 
	<c:choose>
	<c:when test="${dto != null}">
		<table border="1" align="center">
			<tr>
				<th colspan="4"> ${dto.getName()}님 환영합니다. </th>
			</tr>
			<tr>
				<td>
					<button id="toBoard">회원게시판</button>
				</td>
				<td>
					<button id="mypage">마이페이지</button>
				</td>
				<td align="center">
					<button id="logout">로그아웃</button>
				</td>
				<td align="center">
					<button id="memberout">회원탈퇴</button>
				</td>
			</tr>
		</table>
		<script>
       		$("#logout").on("click", function() {
        		location.href = "/logout.members";
        	});
       		$("#memberout").on("click", function() {
       			if(confirm("정말 탈퇴하시겠습니까?") == true) {
            		location.href = "/memberout.members?id=${dto.getId()}"; //삭제요청한 ID를 세션에서 꺼낸후 request요청
       			} else {
       				location.href = "/";
       			}
        	});
       		$("#mypage").on("click", function() {
        		location.href = "/mypage.members";
        	});
       		$("#toBoard").on("click", function() {
        		location.href = "/list.board?cpage=1";
        	});
       		
		</script>
	</c:when>
	<c:otherwise>
	<div class="container">
        <form action="/login.members" method="post" id="login" >
        <div class="title"> <strong>로그인</strong>
        </div>
        <div class="id">
            <input type="text" name="id" id="id" placeholder="아이디를 입력하세요.">
        </div>
        <div class="pw">
            <input type="password" name="pw" id="pw" placeholder="비밀번호를 입력하세요.">
        </div>
        <div class="buttons">
            <button id=login>로그인</button>
            <button type="button" onclick="location.href='/signup.members'">회원가입</button>
        </div>
        <div class="check">
            <input type="checkbox" id="remember">
            <label for="remember">ID를 기억합니다.</label>
        </div>
	         <button id=login>네이버 로그인</button>
        </form>
    </div>

	</c:otherwise>
	</c:choose>
    <script>
    Kakao.init('0de1428ab0636f47a40fe2688a63ab95');
    console.log(Kakao.isInitialized());
    
    
    $(document).ready(function () {
        // 로컬 스토리지에서 저장된 ID 가져오기
        const rememberId = localStorage.getItem("rememberId");
        if (rememberId) {
            $("#id").val(rememberId);
            $("#remember").prop("checked", true);
        }

        // 체크박스 상태 변경 시 처리
        $("#remember").on("change", function () {
            let idValue = $("#id").val();
            if ($(this).is(":checked")) {
                if (idValue) {
                    localStorage.setItem("rememberId", idValue);
                }
            } else {
                localStorage.removeItem("rememberId");
            }
        });

        //ID와 PW 검증
        $("#login").on("submit", function () {
            if ($("#id").val() == "") {
                alert("ID를 입력해주세요.");
                $("#id").focus();
                return false;
            } else if ($("#pw").val() == "") {
                alert("PW를 입력해주세요.");
                $("#pw").focus();
                return false;
            }
        });
    });
</script> 

</body>
</html>