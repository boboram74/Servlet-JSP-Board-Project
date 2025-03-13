package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import commons.Statics;
import dto.BoardDTO;

public class BoardDAO {
	private static BoardDAO INSTANCE;

	public synchronized static BoardDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BoardDAO();
		}
		return INSTANCE;
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}

	public int getNextVal() throws Exception {
		String sql = "select board_seq.nextval from dual";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {
			rs.next();
			return rs.getInt(1);
		}

	}

	public void createByBoard(BoardDTO dto) throws Exception {
		String sql = "insert into board (seq, title, writer, contents)values (?, ?, ?, ?)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, dto.getSeq());
			pstat.setString(2, dto.getTitle());
			pstat.setString(3, dto.getWriter());
			pstat.setString(4, dto.getContents());
			pstat.executeUpdate();
		}
	}
	/*
	public List<BoardDTO> selectAll() throws Exception {
		String sql = "select * from board";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {
			List<BoardDTO> list = new ArrayList<>();
			while (rs.next()) {
				int seq = rs.getInt("seq");
				String title = rs.getString("title");
				String writer = rs.getString("writer");
				String contents = rs.getString("contents");
				Timestamp date = rs.getTimestamp("write_date");
				int view = rs.getInt("view_count");
				list.add(new BoardDTO(seq, title, writer, contents, date, view));
			}
			return list;
		}
	}
	*/

	public List<BoardDTO> selectFromto(int start, int end) throws Exception {
		String sql = "SELECT * FROM ( SELECT board.*, ROW_NUMBER() OVER (ORDER BY seq DESC) AS rnum FROM board ) WHERE rnum BETWEEN ? AND ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try (ResultSet rs = pstat.executeQuery();) {
				List<BoardDTO> list = new ArrayList<>();
				while (rs.next()) {
					int seq = rs.getInt("seq");
					String title = rs.getString("title");
					String writer = rs.getString("writer");
					Timestamp date = rs.getTimestamp("write_date");
					int view = rs.getInt("view_count");
					list.add(new BoardDTO(seq, title, writer, date, view));
				}
				return list;
			}
		}
	}

	public BoardDTO findById(int seq) throws Exception {
		String sql = "select * from board where seq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {

			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery()) {
				if (rs.next()) {
					int seqNum = rs.getInt("seq");
					String title = rs.getString("title");
					String writer = rs.getString("writer");
					String contents = rs.getString("contents");
					Timestamp date = rs.getTimestamp("write_date");
					int view = rs.getInt("view_count");

					return new BoardDTO(seqNum, title, writer, contents, date, view);
				}
			}
		}
		return null;
	}

	public int updateById(BoardDTO dto) throws Exception {
		String sql = "update board set title =?, contents=? where seq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getTitle());
			pstat.setString(2, dto.getContents());
			pstat.setInt(3, dto.getSeq());

			return pstat.executeUpdate();
		}
	}

	public int delete(int seq) throws Exception {
		String sql = "delete from board WHERE seq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, seq);
			return pstat.executeUpdate();
		}
	}

	public void viewCountUp(int seq) throws Exception {
		String sql = "update board set view_count = view_count + 1 where seq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			pstat.executeUpdate();
		}
	}

	public int getRecordTotalCount() throws Exception {
		String sql = "select count(*) from board";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {
			rs.next();
			return rs.getInt(1);
		}
	}

	public String getPageNavi(int currentPage) throws Exception {
		// 전체 글의 갯수가 몇개인가?
		int recordTotalCount = this.getRecordTotalCount();
		// 한 페이지당 몇개씩 글을 보여줄것인가?
		int recordCountPerpage = Statics.recordCountPerPage;
		// 페이지에 노출될 네비게이터 갯수
		int naviCountPerpage = Statics.naaviCountPerPage;
		// 전체 글의 갯수를 알고 한 페이지당 10개씩 보여지려면 전체 페이지의 갯수를 구할수있음
		int pageTotalCount = 0;

		if (recordTotalCount % recordCountPerpage > 0) {
			pageTotalCount = recordTotalCount / recordCountPerpage + 1; // 만약 나머지(즉, recordTotalCount %
																		// recordCountPerpage)가 0보다 크다면, 추가로 글이 남아있는
																		// 것이므로 정수 나눗셈의 결과에 1을 더해 전체 페이지 수를 계산
		} else {
			pageTotalCount = recordTotalCount / recordCountPerpage; // 나머지가 0이면 정확히 페이지에 맞게 배분된 것이므로 몫만이 전체 페이지 수가 된다
		}

		// 총 페이지의 갯수 확인
		// int currentPage = 13;

		if (currentPage < 1) { // 페이지 검증
			currentPage = 1;
		} else if (currentPage > pageTotalCount) {
			currentPage = pageTotalCount;
		}

		// 내가 현재 14페이지에 있다고 가정할 때 네비게이터 시작값과 끝 값을 구하는 수식
		int startNavi = (currentPage - 1) / naviCountPerpage * naviCountPerpage + 1; // 내가 현재 위치한 페이지의 첫번째 페이지
																						// ex) 17 페이지에 위치할 경우 시작 페이지는 11
		int endNavi = startNavi + naviCountPerpage - 1;

		if (endNavi > pageTotalCount) { // 페이지 검증
			endNavi = pageTotalCount;
		}

		// 이전 다음 버튼
		boolean needPrev = true;
		boolean needNext = true;

		if (startNavi == 1) {
			needPrev = false;
		}
		if (endNavi == pageTotalCount) {
			needNext = false;
		}
		StringBuilder navi = new StringBuilder();

		if (needPrev) {
			navi.append("<a href='/list.board?cpage=" + (startNavi - 1) + "'> <</a> ");
		}
		for (int i = startNavi; i <= endNavi; i++) {
			navi.append("<a href='/list.board?cpage=" + i + "'>" + i + "</a> ");
		}
		if (needNext) {
			navi.append("<a href='/list.board?cpage=" + (endNavi + 1) + "'>></a> ");
		}
		return navi.toString();
	}

//	public static void main(String[] args) throws Exception {
//		String sql = "insert into board values(board_seq.nextval, ?, ?, ?, sysdate, 0)";
//		try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "exam", "exam")) {
//			PreparedStatement pstat = con.prepareStatement(sql);
//
//			for (int i = 1; i <= 144; i++) {
//				pstat.setString(1, "writer" + i);
//				pstat.setString(2, "title" + i);
//				pstat.setString(3, "contents" + i);
//				pstat.executeUpdate();
//
//				Thread.sleep(300);
//				System.out.println(i + 1 + "번째 데이터 입력");
//			}
//		}
//	}
}