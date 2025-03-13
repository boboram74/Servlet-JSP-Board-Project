# 📌 Servlet/JSP 게시판 프로젝트

## 🔍 프로젝트 소개  
**Servlet과 JSP만을 사용하여 구현한 웹 게시판 프로젝트**입니다.  
프레임워크 없이 순수 Java EE 기반으로 제작되었으며,  
기본 회원 CRUD, **게시글 작성 시 첨부파일 업로드**,  
게시판 기본 CRUD 기능 (글쓰기, 목록, 수정, 삭제, 상세보기)를 제공합니다.  

MVC 아키텍처 구조를 기반으로 **Controller - DAO - DTO 계층 분리 설계**를 적용하였으며,  
**jQuery AJAX 기반 비동기 댓글 처리 및 ID 중복 검사**도 구현되어 있습니다.

---

## 💻 사용 기술 스택

| 구분            | 기술                                               |
|----------------|----------------------------------------------------|
| Language        | Java 11                                            |
| IDE             | Eclipse                                            |
| Build Tool      | Maven                                              |
| Front-End       | JSP, HTML, CSS, jQuery, AJAX                       |
| Database        | Oracle DB                                          |
| ORM/DB Access   | JDBC 직접 처리                                     |
| File Upload     | cos.jar                         |
| WAS             | Apache Tomcat 9                                    |

---

## 📂 프로젝트 디렉토리 구조
```plaintext
BoardProject  
└── src  
    └── main  
        ├── java  
        │   └── com  
        │       └── kedu  
        │           ├── commons  
        │           │   ├── EncryptionUtils.java  
        │           │   ├── OAuthToken.java  
        │           │   ├── ReplyStatic.java  
        │           │   ├── Statics.java  
        │           │   └── VerificationUtil.java  
        │           ├── controller  
        │           │   ├── BoardController.java  
        │           │   ├── FilesController.java  
        │           │   ├── FrontController.java  
        │           │   └── ReplyController.java  
        │           ├── dao  
        │           │   ├── BoardDAO.java  
        │           │   ├── FilesDAO.java  
        │           │   ├── MemberDAO.java  
        │           │   └── ReplyDAO.java  
        │           ├── dto  
        │           │   ├── BoardDTO.java  
        │           │   ├── FilesDTO.java  
        │           │   ├── MemberDTO.java  
        │           │   └── ReplyDTO.java  
        └── webapp  
            ├── META-INF  
            ├── board  
            │   ├── detail.jsp  
            │   ├── list.jsp  
            │   └── writer.jsp  
            ├── members  
            │   ├── idcheck.jsp  
            │   ├── mypage.jsp  
            │   └── signup.jsp  
            └── index.jsp  
.gitignore
