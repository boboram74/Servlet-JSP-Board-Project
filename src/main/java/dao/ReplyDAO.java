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

import dto.ReplyDTO;

public class ReplyDAO {
	private static ReplyDAO INSTANCE;

	public synchronized static ReplyDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ReplyDAO();
		}
		return INSTANCE;
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}

	public void createByReply(ReplyDTO dto) throws Exception {
		String sql = "insert into reply (id, writer, contents, parent_seq)values (reply_seq.nextval, ?, ?, ?)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getWriter());
			pstat.setString(2, dto.getContents());
			pstat.setInt(3, dto.getParent_seq());
			pstat.executeUpdate();
		}
	}

	public List<ReplyDTO> selectAll(int seq) throws Exception {
		String sql = "select * from reply where parent_seq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery()) {
				List<ReplyDTO> list = new ArrayList<>();
				while (rs.next()) {
					int id = rs.getInt("id");
					String writer = rs.getString("writer");
					String contents = rs.getString("contents");
					Timestamp date = rs.getTimestamp("write_date");
					int parent_seq = rs.getInt("parent_seq");
					list.add(new ReplyDTO(id, writer, contents, date, parent_seq));
				}
				return list;
			}
		}
	}

	public List<ReplyDTO> selectFromtoReply(int start, int end, int seq) throws Exception {
		String sql = "SELECT * FROM ( SELECT reply.*, ROW_NUMBER() OVER (ORDER BY id DESC) AS rnum FROM reply WHERE parent_seq = ? ) WHERE rnum BETWEEN ? AND ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq); // 부모 게시글 ID
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try (ResultSet rs = pstat.executeQuery();) {
				List<ReplyDTO> list = new ArrayList<>();
				while (rs.next()) {
					int id = rs.getInt("id");
					String writer = rs.getString("writer");
					String contents = rs.getString("contents");
					Timestamp date = rs.getTimestamp("write_date");
					int parent_seq = rs.getInt("parent_seq");
					list.add(new ReplyDTO(id, writer, contents, date, parent_seq));
				}
				return list;
			}
		}
	}

	public int getRecordTotalCount(int seq) throws Exception {
		String sql = "select count(*) from reply where parent_seq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	public int updateById(ReplyDTO dto) throws Exception {
		String sql = "update reply set contents = ?, write_date = sysdate  where id = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getContents());
			pstat.setInt(2, dto.getId());
			return pstat.executeUpdate();
		}
	}

	public int deleteById(int id) throws Exception {
		String sql = "delete from reply WHERE id = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, id);
			return pstat.executeUpdate();
		}
	}

}
