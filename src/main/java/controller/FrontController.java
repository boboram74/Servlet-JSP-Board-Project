package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import commons.EncryptionUtils;
import dao.MemberDAO;
import dto.MemberDTO;
import dto.OAuthTokenDTO;

@WebServlet("/member/*")
public class FrontController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		String cmd = request.getRequestURI();
		MemberDAO dao = MemberDAO.getInstance();
		String ip = request.getRemoteAddr();
		try {
			if (cmd.equals("/add.members")) {
				String id = request.getParameter("id");
				String pw = request.getParameter("pw");
				String encryptPw = EncryptionUtils.encrypt(pw);
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				String tel = request.getParameter("tel");
				int post = Integer.parseInt(request.getParameter("post"));
				String address1 = request.getParameter("address1");
				String address2 = request.getParameter("address2");
				System.out.println("가입요청 : " + ip);
				dao.createByMember(new MemberDTO(id, encryptPw, name, email, tel, post, address1, address2));
				response.sendRedirect("/index.jsp");
			} else if (cmd.equals("/signup.members")) {
				response.sendRedirect("/members/signup.jsp");

			} else if (cmd.equals("/login.members")) {
				String id = request.getParameter("id");
				String pw = request.getParameter("pw");
				String encryptPw = EncryptionUtils.encrypt(pw);
				MemberDTO result = dao.login(id, encryptPw);
				if (result != null) {
					request.getSession().setAttribute("dto", result); // 로그인한 계정 dto에 담아서 세션에 저장
				} else {
					System.out.println("로그인 실패!");
				}
				response.sendRedirect("/index.jsp");

			} else if (cmd.equals("/idCheck.members")) {
				String id = request.getParameter("id");
				boolean result = dao.idVali(id);
				if (result == true) {
					response.getWriter().append("exits");
				}

			} else if (cmd.equals("/logout.members")) {
				request.getSession().invalidate(); // 현재 세션의 값 지우기
				response.sendRedirect("/index.jsp");

			} else if (cmd.equals("/update.members")) {
				String id = request.getParameter("id");
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				String tel = request.getParameter("tel");
				int post = Integer.parseInt(request.getParameter("post"));
				String address1 = request.getParameter("address1");
				String address2 = request.getParameter("address2");

				int result = dao.updateById(new MemberDTO(id, name, email, tel, post, address1, address2));
				if (result > 0) {
					MemberDTO dto = dao.findById(id);// 업데이트후 변경된 정보를 새로 세션에 담기
					request.getSession().setAttribute("dto", dto);
				} else {
					System.out.println("수정실패!");
				}
				response.sendRedirect("/");

			} else if (cmd.equals("/memberout.members")) {
				String id = request.getParameter("id"); // 삭제버튼 누르면 url로 삭제요청한 id가 만들어진다. 만들어진 id를 읽어서 삭제
				int result = dao.deleteById(id);
				if (result > 0) {
					request.getSession().invalidate();
				}
				response.sendRedirect("/index.jsp");
			} else if (cmd.equals("/mypage.members")) { // 마이페이지
				request.getSession().getAttribute("dto");
				request.getRequestDispatcher("/members/mypage.jsp").forward(request, response);
			} else if (cmd.equals("/kakao.members")) {
				// URL 쿼리스트링에서 code 파라미터 추출
				String code = request.getParameter("code");
				System.out.println("code: " + code);

				// 카카오 토큰 발급 URL 설정
				String pUrl = "https://kauth.kakao.com/oauth/token";

				// POST 요청 시 전달할 파라미터 구성 (본인 설정에 맞게 수정)
				String bodyData = "grant_type=authorization_code&";
				bodyData += "client_id=ebe966bc92ceffb571ce79af8392b892&"; // 실제 앱의 REST API 키로 변경
				bodyData += "redirect_uri=http://10.10.55.9/kakao.members&"; // 등록한 리다이렉트 URI
				bodyData += "code=" + code;

				URL url = new URL(pUrl);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
				con.setDoOutput(true);

				// 요청 본문 전송
				try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"))) {
					bw.write(bodyData);
					bw.flush();
				}

				// 응답 읽기
				StringBuilder sb = new StringBuilder();
				try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
					String input;
					while ((input = br.readLine()) != null) {
						sb.append(input);
					}
				}

				// JSON 응답 파싱 (Gson 라이브러리 사용)
				Gson gson = new Gson();
				OAuthTokenDTO oAuthToken = gson.fromJson(sb.toString(), OAuthTokenDTO.class);
				// 예: access token 출력 (필요시 사용자 정보 요청에 사용)
				System.out.println("Access Token: " + oAuthToken.getAccess_token());

				// access token을 활용해 사용자 정보 요청 (추가 구현 가능)
				// 예를 들어, 별도의 GET 요청을 보내 https://kapi.kakao.com/v2/user/me로 사용자 정보를 받아올 수 있습니다.

				// **5. access token을 사용하여 사용자 정보 요청 (GET)
				String accessToken = oAuthToken.getAccess_token();
				URL userInfoUrl = new URL("https://kapi.kakao.com/v2/user/me");
				HttpURLConnection userCon = (HttpURLConnection) userInfoUrl.openConnection();
				userCon.setRequestMethod("GET");
				userCon.setRequestProperty("Authorization", "Bearer " + accessToken);
				userCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

				int responseCode = userCon.getResponseCode();
				BufferedReader userBr;
				if (responseCode == HttpURLConnection.HTTP_OK) {
					userBr = new BufferedReader(new InputStreamReader(userCon.getInputStream(), "UTF-8"));
				} else {
					userBr = new BufferedReader(new InputStreamReader(userCon.getErrorStream(), "UTF-8"));
				}
				StringBuilder userSb = new StringBuilder();
				String line;
				while ((line = userBr.readLine()) != null) {
					userSb.append(line);
				}
				userBr.close();
				String userJson = userSb.toString();
				System.out.println("User Info JSON: " + userJson);

				// 6. JSON 응답에서 필요한 필드 추출 (Gson의 JsonObject 사용)
				// Kakao API 기본 응답 예시:
				// {
				// "id": 123456789,
				// "kakao_account": {
				// "email": "user@example.com",
				// "gender": "male",
				// "profile": {
				// "nickname": "홍길동",
				// "profile_image_url": "http://...",
				// "thumbnail_image_url": "http://..."
				// }
				// }
				// }

				com.google.gson.JsonObject jsonObj = gson.fromJson(userJson, com.google.gson.JsonObject.class);
				com.google.gson.JsonObject kakaoAccount = jsonObj.getAsJsonObject("kakao_account");
				com.google.gson.JsonObject profile = null;
				if (kakaoAccount.has("profile") && !kakaoAccount.get("profile").isJsonNull()) {
					profile = kakaoAccount.getAsJsonObject("profile");
				}

				// profile_nickname: profile 객체에 "nickname"이 있을 경우 사용, 없으면 kakao_account의 "name"
				// 사용
				String profileNickname = "정보없음";
				if (profile != null && profile.has("nickname") && !profile.get("nickname").isJsonNull()) {
					profileNickname = profile.get("nickname").getAsString();
				} else if (kakaoAccount.has("name") && !kakaoAccount.get("name").isJsonNull()) {
					profileNickname = kakaoAccount.get("name").getAsString();
				}

				// profile_image: profile 객체의 "profile_image_url"
				String profileImage = "정보없음";
				if (profile != null && profile.has("profile_image_url")
						&& !profile.get("profile_image_url").isJsonNull()) {
					profileImage = profile.get("profile_image_url").getAsString();
				}

				// account_email: kakao_account의 "email"
				String accountEmail = kakaoAccount.has("email") && !kakaoAccount.get("email").isJsonNull()
						? kakaoAccount.get("email").getAsString()
						: "정보없음";

				// name: kakao_account의 "name"
				String nameField = kakaoAccount.has("name") && !kakaoAccount.get("name").isJsonNull()
						? kakaoAccount.get("name").getAsString()
						: "정보없음";

				// gender: kakao_account의 "gender"
				String genderField = kakaoAccount.has("gender") && !kakaoAccount.get("gender").isJsonNull()
						? kakaoAccount.get("gender").getAsString()
						: "정보없음";

				// birthyear: kakao_account의 "birthyear"
				String birthyear = kakaoAccount.has("birthyear") && !kakaoAccount.get("birthyear").isJsonNull()
						? kakaoAccount.get("birthyear").getAsString()
						: "정보없음";

				// phone_number: kakao_account의 "phone_number"
				String phoneNumber = kakaoAccount.has("phone_number") && !kakaoAccount.get("phone_number").isJsonNull()
						? kakaoAccount.get("phone_number").getAsString()
						: "정보없음";

				// shipping_address
				String shipping_address = kakaoAccount.has("shipping_address")
						&& !kakaoAccount.get("shipping_address").isJsonNull()
								? kakaoAccount.get("shipping_address").getAsString()
								: "정보없음";

				// 콘솔 출력 (각 값이 "정보없음"이 아닐 경우에만 출력)
				if (!"정보없음".equals(profileNickname)) {
					System.out.println("profile_nickname: " + profileNickname);
				}
				if (!"정보없음".equals(profileImage)) {
					System.out.println("profile_image: " + profileImage);
				}
				if (!"정보없음".equals(accountEmail)) {
					System.out.println("account_email: " + accountEmail);
				}
				if (!"정보없음".equals(nameField)) {
					System.out.println("name: " + nameField);
				}
				if (!"정보없음".equals(genderField)) {
					System.out.println("gender: " + genderField);
				}
				if (!"정보없음".equals(birthyear)) {
					System.out.println("birthyear: " + birthyear);
				}
				if (!"정보없음".equals(phoneNumber)) {
					System.out.println("phone_number: " + phoneNumber);
				}
				if (!"정보없음".equals(shipping_address)) {
					System.out.println("shipping_address: " + shipping_address);
				}

				MemberDTO kakaoUserDTO = new MemberDTO();
				kakaoUserDTO.setName(profileNickname); // 추가 정보를 DTO에 담고 싶으면 setter 추가
				kakaoUserDTO.setEmail(accountEmail); // 추가 정보를 DTO에 담고 싶으면 setter 추가
				request.getSession().setAttribute("dto", kakaoUserDTO);
				response.sendRedirect("/");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

}
