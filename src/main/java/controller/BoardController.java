package controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import commons.ReplyStatic;
import commons.Statics;
import dao.BoardDAO;
import dao.FilesDAO;
import dao.ReplyDAO;
import dto.BoardDTO;
import dto.FilesDTO;
import dto.MemberDTO;
import dto.ReplyDTO;

@WebServlet("*.board")
public class BoardController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf8");

		String cmd = request.getRequestURI();
		BoardDAO dao = BoardDAO.getInstance();
		ReplyDAO replyDao = ReplyDAO.getInstance();
		FilesDAO fdao = FilesDAO.getInstance();
		Gson g = new Gson();
		String ip = request.getRemoteAddr();
		System.out.println(ip);

		try {
			if (cmd.equals("/member/list.board")) {
				// 페이징 유효성 검증
				String scpage = (String) request.getParameter("cpage");

				if (scpage == null) {
					scpage = "1";
				}
				int cpage = Integer.parseInt(scpage);

				int recordTotalCount = dao.getRecordTotalCount();

				int pageTotalCount = 0;
				if (recordTotalCount % Statics.recordCountPerPage > 0) {
					pageTotalCount = recordTotalCount / Statics.recordCountPerPage + 1;
				} else {
					pageTotalCount = recordTotalCount / Statics.recordCountPerPage;
				}

				if (cpage < 1) {
					cpage = 1;
				} else if (cpage > pageTotalCount) {
					cpage = pageTotalCount;
				}
				// 게시글 목록 담아오기
				int end = cpage * Statics.recordCountPerPage;
				int start = end - (Statics.recordCountPerPage - 1);

				// ajax로 받아오기
				// List<BoardDTO> list = dao.selectFromto(start, end);

				int startNavi = (cpage - 1) / Statics.naaviCountPerPage * Statics.naaviCountPerPage + 1;
				int endNavi = startNavi + Statics.naaviCountPerPage - 1;

				if (endNavi > pageTotalCount) { // endNavi 값은 페이지 전체 개수보다 클수없음
					endNavi = pageTotalCount;
				}

				boolean needPrev = true;
				boolean needNext = true;
				if (startNavi == 1) {
					needPrev = false;
				}
				if (endNavi == pageTotalCount) {
					needNext = false;
				}

				// request.setAttribute("list", list);
				request.setAttribute("cpage", cpage);
				request.setAttribute("startNavi", startNavi);
				request.setAttribute("endNavi", endNavi);
				request.setAttribute("needPrev", needPrev);
				request.setAttribute("needNext", needNext);

				request.getSession().getAttribute("dto");
				request.getRequestDispatcher("/board/list.jsp").forward(request, response);

			} else if (cmd.equals("/ajax_list.board")) {
				String scpage = (String) request.getParameter("cpage");
				if (scpage == null) {
					scpage = "1";
				}
				int cpage = Integer.parseInt(scpage);

				int recordTotalCount = dao.getRecordTotalCount();

				int pageTotalCount = 0;
				if (recordTotalCount % Statics.recordCountPerPage > 0) {
					pageTotalCount = recordTotalCount / Statics.recordCountPerPage + 1;
				} else {
					pageTotalCount = recordTotalCount / Statics.recordCountPerPage;
				}

				if (cpage < 1) {
					cpage = 1;
				} else if (cpage > pageTotalCount) {
					cpage = pageTotalCount;
				}
				// 게시글 목록 담아오기
				int end = cpage * Statics.recordCountPerPage;
				int start = end - (Statics.recordCountPerPage - 1);

				List<BoardDTO> list = dao.selectFromto(start, end);
				Map<String, Object> dto = new HashMap<>();
				dto.put("list", list);

				String result = g.toJson(dto); // json 직렬화

				response.setContentType("text/html; charset=UTF-8");
				// response.setContentType("charset=UTF-8");
				response.getWriter().append(result);

			} else if (cmd.equals("/toWriter.board")) {
				response.sendRedirect("/board/writer.jsp");
			} else if (cmd.equals("/create.board")) {
				// 로그인 검증
				MemberDTO dto = (MemberDTO) request.getSession().getAttribute("dto");
				if (dto == null) {
					response.sendRedirect("/");
					return;
				}
				// cos 라이브러리로 사용하는 multipart/form-data 파싱 인스턴스 MultipartRequest
				int maxSize = 1024 * 1024 * 10; // 파일 업로드 최대 사이즈(10mb)
				String savePath = request.getServletContext().getRealPath("upload"); // 파일 업로드 경로
				File filePath = new File(savePath);
				if (!filePath.exists()) {
					filePath.mkdir();
				}
				MultipartRequest multi = new MultipartRequest(request, savePath, maxSize, "utf8",
						new DefaultFileRenamePolicy()); // Parameters :
														// 1. request 객체 / 2.파일업로드 경로 / 3.파일업로드 사이즈제한 /
														// 4. 인코딩 처리 / 5. 이름 중복시 이름 변경 규칙
				int seq = dao.getNextVal(); // 게시글을 작성시 Board 테이블의 시퀀스_nextVal을 가져오는 쿼리
				String writer = dto.getId();
				String title = multi.getParameter("title");
				String contents = multi.getParameter("contents");
				dao.createByBoard(new BoardDTO(seq, title, writer, contents));

				Enumeration<String> fileNames = multi.getFileNames(); // Enumeration => List와 같음

				while (fileNames.hasMoreElements()) { // hasMoreElements = boolean
					String name = fileNames.nextElement();
					String originName = multi.getOriginalFileName(name);

					if (originName == null) {
						continue;
					}

					String sysName = multi.getFilesystemName(name);
					fdao.insert(new FilesDTO(0, originName, sysName, seq));

				}
				response.sendRedirect("/list.board?cpage=1");

			} else if (cmd.equals("/detail.board")) { // 게시글 상세보기
				// 게시글 작성자 확인 로직
				int seq = Integer.parseInt(request.getParameter("seq"));
				dao.viewCountUp(seq);
				BoardDTO dto = dao.findById(seq);
				request.setAttribute("boardDto", dto);

				// 댓글 작성자 확인 로직
				MemberDTO member = (MemberDTO) request.getSession().getAttribute("dto");
				if (member == null) {
					response.sendRedirect("/index.jsp");
					return;
				}
				String user = member.getId();
				request.setAttribute("loginUser", user);

				// 댓글 목록 출력 로직
				String rpage_ori = (String) request.getParameter("rpage");
				if (rpage_ori == null) {
					rpage_ori = "1";
				}
				int rpage = Integer.parseInt(rpage_ori);

				int recordTotalCount = replyDao.getRecordTotalCount(seq);

				int pageTotalCount = 0;
				if (recordTotalCount % ReplyStatic.recordCountPerPage > 0) {
					pageTotalCount = recordTotalCount / ReplyStatic.recordCountPerPage + 1;
				} else {
					pageTotalCount = recordTotalCount / ReplyStatic.recordCountPerPage;
				}

				if (rpage < 1) {
					rpage = 1;
				} else if (rpage > pageTotalCount) {
					rpage = pageTotalCount;
				}
				// 댓글 목록 담아오기
				int end = rpage * ReplyStatic.recordCountPerPage;
				int start = end - (ReplyStatic.recordCountPerPage - 1);
				List<ReplyDTO> list = replyDao.selectFromtoReply(start, end, seq);

				int startNavi = (rpage - 1) / ReplyStatic.naaviCountPerPage * ReplyStatic.naaviCountPerPage + 1;
				int endNavi = startNavi + ReplyStatic.naaviCountPerPage - 1;

				if (endNavi > pageTotalCount) { // endNavi 값은 페이지 전체 개수보다 클수없음
					endNavi = pageTotalCount;
				}

				boolean needPrev = true;
				boolean needNext = true;
				if (startNavi == 1) {
					needPrev = false;
				}
				if (endNavi == pageTotalCount) {
					needNext = false;
				}

				request.setAttribute("list", list);
				request.setAttribute("rpage", rpage); // 넣음
				request.setAttribute("seq", seq); // 넣음

				request.setAttribute("startNavi", startNavi);
				request.setAttribute("endNavi", endNavi);
				request.setAttribute("needPrev", needPrev);
				request.setAttribute("needNext", needNext);

				request.setAttribute("reply", list);

				// 파일 이름 출력
				List<FilesDTO> files = fdao.findById(seq);
				request.setAttribute("files", files);

				request.getRequestDispatcher("/board/detail.jsp").forward(request, response);
			} else if (cmd.equals("/delete.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				BoardDTO dto = dao.findById(seq);
				MemberDTO member = (MemberDTO) request.getSession().getAttribute("dto");
				String user = member.getId();

				if (dto != null && dto.getWriter().equals(user)) {
					int result = dao.delete(seq);
					if (result > 0) {
						response.sendRedirect("/list.board?cpage=1");
					} else {
						System.out.println("삭제 실패!");
						response.sendRedirect("/detail.board?seq=" + seq);
					}
				} else {
					System.out.println("삭제 권한 없음");
					response.sendRedirect("detail.board?seq=" + seq);
				}
			} else if (cmd.equals("/update.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				String title = request.getParameter("title");
				String contents = request.getParameter("contents");

				int result = dao.updateById(new BoardDTO(title, contents, seq));
				if (result > 0) {
					response.sendRedirect("/detail.board?seq=" + seq);
				} else {
					System.out.println("수정실패");
					response.sendRedirect("/list.board?cpage=1");
				}
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
