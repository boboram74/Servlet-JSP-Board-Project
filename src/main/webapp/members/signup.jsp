<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
    
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    
    <title>Document</title>

    <style>
        * {
            box-sizing: border-box;
        }

        div {
            /* border: 1px solid black; */
        }

        .container {
            width: 300px;
            height: 600px;
            margin: auto;
            background-color: lightgray;
        }

        .title {
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 20px;
            height: 15%;
        }

        .top {
            width: 100%;
            height: 30%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        #id {
            width: 72%;  
        }

        .top>#result {
            font-size: 9px;
        }

        .middle {
            width: 100%;
            height: 20%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .bottom {
            width: 100%;
            height: 25%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .bottom button {
            background-color: #49b155;
            color: white;
            font-size: 12px;
        }

        button:hover {
            cursor: pointer;
        }

        .footer {
            height: 10%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .footer button {
            margin: 35px;
        }

        input {
            width: 100%;
            height: 100%;
        }
    </style>
</head>

<body>
    <div class="container">
        <form action="/add.members" id="frm" method="post">
            <div class="title">회원가입</div>
            <div class="top">
                <fieldset>
                    <legend>아이디/패스워드</legend>
                    <input type="text" name="id" id="id" placeholder="아이디를 입력하세요"> <button type="button" id="idCheck">중복체크</button>
                    <input type="password" name="pw" id="pw" placeholder="패스워드를 입력하세요">
                    <input type="password" name="rePw" id="rePw" placeholder="패스워드를 다시 입력하세요">
                    <div id="result"></div>
                </fieldset>
            </div>

            <div class="middle">
                <fieldset>
                    <legend>이름/전화번호/이메일</legend>
                    <input type="text" name="name" id="name" placeholder="이름을 입력하세요">
                    <input type="tel" name="tel" id="tel" placeholder="010-1234-5678">
                    <input type="email" name="email" id="email" placeholder="이메일을 입력하세요">
                    <div id="result2"></div>
                </fieldset>
            </div>

            <div class="bottom">
                <fieldset>
                    <legend>주소 </legend>
                    <input type="text" name="post" id="post" placeholder="우편번호를 입력하세요" readonly style="width: 63%;">
                    <button id="search" type="button">우편번호 찾기</button>
                    <input type="text" name="address1" id="address" placeholder="주소를 입력하세요" readonly>
                    <input type="text" name="address2" id="detailAddress" placeholder="상세주소를 입력하세요">
                </fieldset>
            </div>
            <div class="footer">
                <button>가입</button>
                <button type="reset">초기화</button>
            </div>
        </form>
    </div>
    <script>
        let frm = document.getElementById("frm");
        let id = document.getElementById("id");
        let idCheck = document.getElementById("idCheck");

        let pw = document.getElementById("pw");
        let rePw = document.getElementById("rePw");
        let result = document.getElementById("result");

        let name = document.getElementById("name");
        let tel = document.getElementById("tel");
        let email = document.getElementById("email");
        let result2 = document.getElementById("result2");

        let id_val;
        let pw_val;
        let name_val;
        let tel_val;
        let email_val;
        
        //ID 중복검사(javascript방식)
        // document.getElementById("duplCheck").onclick = function() {
        //     window.open("/idCheck.members?id="+$("#id").val(),"","width=400, height=300");

        // };
        //jQuery 방식 (페이지전환 있음)
        $("#idCheck").on("click",function() {
        	if($("#id").val()== "") {
        		alert("ID를 먼저 입력해주세요.")
        		return false;
        	}
        	//window.open("/idCheck.members?id="+$("#id").val(),"","width=400, height=300");
        //ajax : 비동기로 리팩터링
	        $.ajax({
	        	url:"/idCheck.members",
	        	data:{id:$("#id").val()}
	        }).done(function(resp) {
	        	if(resp == "exits") {
	        		result.setAttribute("style", "color: red;  font-size: 12px");
	        		$("#result").html("이미 사용중인 ID입니다.")
	        	} else {
	        		//$("#result").setAttribute("style", "color: blue; font-size: 12px");
	        		$("#result").html("사용 가능한 ID입니다.")
	        	}
        	
        	});
        });
        
        
        // 유효성 검사
        frm.onsubmit = function () {
            if (id.value == "") {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "ID는 필수 입력사항입니다.",
                });
                id.focus();
                return false;
            } else if (!id_val) {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "ID가 유효하지않습니다.",
                });
                id.focus();
                return false;
        
            } else if (pw.value == "") {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "PW는 필수 입력사항입니다.",
                });
                pw.focus();
                return false;

            }else if(!pw_val) {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "PW가 유효하지않습니다.",
                });
                pw.focus();
                return false;
            } else if (pw.value != rePw.value) {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "PW가 일치하지않습니다. 다시 입력해주세요.",
                }); rePw.focus();
                return false;
            } else if (!name_val) {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "이름이 유효하지않습니다.",
                });
                name.focus();
                return false;
            } else if (!tel_val) {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "전화번호가 유효하지않습니다.",
                });
                name.focus();
                return false;
            } else if(!email_val) {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "이메일이 유효하지않습니다.",
                });
                name.focus();
                return false;
            } else if(!idCheck) {
                Swal.fire({
                    icon: "error",
                    title: "실패!",
                    text: "ID중복검사를 클릭해주세요",
                });
                return false;
            }
            return true;
        }

        id.onkeyup = function() {
            let regex = /^[a-z0-9_]{8,20}$/;
            let vali = regex.exec(id.value);
            console.log(vali);
            if (vali == null) {
                result.setAttribute("style", "color: red;  font-size: 12px");
                result.innerHTML = "ID는 영어소문자,숫자 8자리이상 20자리이하로 작성해주세요.";
                id_val = false;
            } else {
                console.log("유효한 ID:", id.value);
                result.setAttribute("style", "color: blue; font-size: 12px");
                result.innerHTML = "유효한 ID입니다.";
                id_val = true;
            }
        }
        pw.onkeyup = function (e) {
            let regex = /^[A-Za-z0-9_]{8,}$/;
            let vali = regex.exec(pw.value);
            if (vali == null) {
                result.setAttribute("style", "color: red;  font-size: 12px");
                result.innerHTML = "유효하지 않는 PW입니다."
                pw_val = false;
            } else {
                console.log("유효한 PW:", id.value);
                result.setAttribute("style", "color: blue; font-size: 12px");
                result.innerHTML = "유효한 PW 입니다.";
                pw_val = true;
            }
        }
        rePw.onkeyup = function (e) {
            console.log(e.key)
            if (pw.value == rePw.value) {
                result.setAttribute("style", "color: blue; font-size: 12px");
                result.innerHTML = "패스워드 일치!";
            } else {
                result.setAttribute("style", "color: red;  font-size: 12px");
                result.innerHTML = "패스워드 일치하지 않음!";
            }
        }

        name.onkeyup = function (e) {
            let regex = /^[가-힣]{2,5}$/;
            let vali = regex.exec(name.value);
            if (vali == null) {
                result2.setAttribute("style", "color: red;  font-size: 12px");
                result2.innerHTML = "유효하지 않는 이름입니다."
                name_val = false;
            } else {
                console.log("유효한 이름:", name.value);
                result2.setAttribute("style", "color: blue; font-size: 12px");
                result2.innerHTML = "유효한 이름 입니다.";
                name_val = true;
            }
        }

        tel.onkeyup = function (e) {
            let regex = /^010[ -]?\d{4}[ -]?\d{4}$/;
            let vali = regex.exec(tel.value);
            if (vali == null) {
                result2.setAttribute("style", "color: red;  font-size: 12px");
                result2.innerHTML = "유효하지 않는 전화번호입니다."
                tel_val = false;
            } else {
                console.log("유효한 이름:", tel.value);
                result2.setAttribute("style", "color: blue; font-size: 12px");
                result2.innerHTML = "유효한 전화번호 입니다.";
                tel_val = true;
            }
        }

        email.onkeyup = function (e) {
            let regex = /^[A-Za-z0-9_]+@[A-Za-z0-9]+\.[a-zA-Z]{3,4}$/;
            let vali = regex.exec(email.value);
            if (vali == null) {
                result2.setAttribute("style", "color: red;  font-size: 12px");
                result2.innerHTML = "유효하지 않는 이메일입니다."
                email_val = false;
            } else {
                console.log("유효한 이름:", email.value);
                result2.setAttribute("style", "color: blue; font-size: 12px");
                result2.innerHTML = "유효한 이메일 입니다.";
                email_val = true;
            }
        }

        document.getElementById("search").onclick = function () {
            new daum.Postcode({
                oncomplete: function (data) {
                    let post = document.getElementById("post");
                    let address = document.getElementById("address");
                    post.value = data.zonecode;
                    address.value = data.roadAddress;
                }
            }).open();
        }

    </script>
</body>
</html>