package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ReplyDAO;
import dto.MemberDTO;
import dto.ReplyDTO;

@WebServlet("*.reply")
public class ReplyController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setContentType("text/html; charset=UTF-8");
		String cmd = request.getRequestURI();
		String ip = request.getRemoteAddr();
		ReplyDAO dao = ReplyDAO.getInstance();
		System.out.println(ip);

		try {
			if (cmd.equals("/create.reply")) {
				String contents = request.getParameter("reply");
				int parent_seq = Integer.parseInt(request.getParameter("parent_seq"));

				MemberDTO dto = (MemberDTO) request.getSession().getAttribute("dto");
				if (dto == null) {
					response.sendRedirect("/index.jsp");
					return;
				}
				String writer = dto.getId();
				dao.createByReply(new ReplyDTO(0, writer, contents, null, parent_seq));
				response.sendRedirect("/detail.board?seq=" + parent_seq);

			} else if (cmd.equals("/update.reply")) {
				int id = Integer.parseInt(request.getParameter("id"));
				String contents = request.getParameter("contents");
				int parent_seq = Integer.parseInt(request.getParameter("parent_seq"));

				int result = dao.updateById(new ReplyDTO(id, contents));
				if (result > 0) {
				} else {
					System.out.println("업데이트 실패!");
				}
				response.sendRedirect("/detail.board?seq=" + parent_seq);

			} else if (cmd.equals("/delete.reply")) {
				int id = Integer.parseInt(request.getParameter("id"));
				dao.deleteById(id);

				int seq = Integer.parseInt(request.getParameter("seq"));
				response.sendRedirect("/detail.board?seq=" + seq);
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
