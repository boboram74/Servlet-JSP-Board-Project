package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.files")
public class FilesController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		String cmd = request.getRequestURI();
		try {
			if (cmd.equals("/download.files")) {
				// 다운로드에 필요한 정보 취합
				String fileName = request.getParameter("filename");
				String originName = request.getParameter("oriname");
				String path = request.getServletContext().getRealPath("upload");

				// 다운로드할 대상 파일의 내용을 byte[]에 로딩하기위해 저장소 준비
				File target = new File(path + "/" + fileName);
				byte[] fileContents = new byte[(int) target.length()];

				// 오리지널 파일 이름 인코딩 처리
				// 크롬은 파일 다운로드시 이름을 ISO-8859-1 로 처리함.
				// 브라우저별로 인코딩 처리가 다르지만 우린 크롬 기준으로 제작
				originName = new String(originName.getBytes("utf8"), "ISO-8859-1");

				response.reset(); // response의 기본 동작을 초기화(기본동작-클라이언트가 브라우저에 실행시키는 동작)
				response.setHeader("Content-Disposition", "attachment; filename=" + originName); // 헤더에 데이터타입을 첨부파일이라고명시

				try (DataInputStream dis = new DataInputStream(new FileInputStream(target)); // 빨대를 꽂다
						ServletOutputStream sos = response.getOutputStream();) {

					dis.readFully(fileContents); // 파일 내용을 RAM으로 로딩
					sos.write(fileContents); // Response의 Stream을 통해 byte[] 출력
					sos.flush(); // 발송
				}

				// 권한 확인하기
				// 기록 남기기
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
