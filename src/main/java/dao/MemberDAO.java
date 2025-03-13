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

import dto.MemberDTO;

public class MemberDAO {
	private static MemberDAO INSTANCE;

	public synchronized static MemberDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MemberDAO();
		}
		return INSTANCE;
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}

	public MemberDTO login(String id, String pw) throws Exception {
		String sql = "select * from members where id = ? and pw = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setString(1, id);
			pstat.setString(2, pw);
			try (ResultSet rs = pstat.executeQuery()) {
				if (rs.next()) {
					String name = rs.getString("name");
					String email = rs.getString("email");
					String phone = rs.getString("phone");
					int post = rs.getInt("postcode");
					String address1 = rs.getString("address1");
					String address2 = rs.getString("address2");
					Timestamp date = rs.getTimestamp("signup_date");
					MemberDTO mypage = new MemberDTO(id, name, email, phone, post, address1, address2, date);
					return mypage;
				}
			}
		}
		return null;
	}

	public boolean idVali(String id) throws Exception {// ID검증
		String sql = "select id from members where id = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setString(1, id);
			try (ResultSet rs = pstat.executeQuery()) {
				return rs.next();
			}
		}
	}

	public void createByMember(MemberDTO dto) throws Exception {
		String sql = "insert into members (id, pw, name, email, phone, postcode, address1, address2) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getId());
			pstat.setString(2, dto.getPw());
			pstat.setString(3, dto.getName());
			pstat.setString(4, dto.getEmail());
			pstat.setString(5, dto.getTel());
			pstat.setInt(6, dto.getPost());
			pstat.setString(7, dto.getAddress1());
			pstat.setString(8, dto.getAddress2());

			pstat.executeUpdate();
		}
	}

	public List<MemberDTO> selectAll() throws Exception {
		String sql = "select * from members";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {
			List<MemberDTO> list = new ArrayList<>();
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String tel = rs.getString("tel");
				String email = rs.getString("email");
				int post = rs.getInt("post");
				String address1 = rs.getString("address1");
				String address2 = rs.getString("address2");
				Timestamp date = rs.getTimestamp("signup_date");
				list.add(new MemberDTO(id, name, tel, email, post, address1, address2, date));
			}
			return list;
		}
	}

	public MemberDTO findById(String id) throws Exception {
		String sql = "select * from members where id = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, id);
			try (ResultSet rs = pstat.executeQuery()) {
				if (rs.next()) {
					String name = rs.getString("name");
					String email = rs.getString("email");
					String tel = rs.getString("phone");
					int post = rs.getInt("postcode");
					String address1 = rs.getString("address1");
					String address2 = rs.getString("address2");
					Timestamp date = rs.getTimestamp("signup_date");
					return new MemberDTO(id, name, email, tel, post, address1, address2, date);
				}
			}
		}
		return null;
	}

	public int updateById(MemberDTO dto) throws Exception {
		String sql = "update members set name =?, email=?, phone=?, postcode=?, address1=?, address2=? where id = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getName());
			pstat.setString(2, dto.getEmail());
			pstat.setString(3, dto.getTel());
			pstat.setInt(4, dto.getPost());
			pstat.setString(5, dto.getAddress1());
			pstat.setString(6, dto.getAddress2());
			pstat.setString(7, dto.getId());

			return pstat.executeUpdate();
		}
	}

	public int deleteById(String id) throws Exception {
		String sql = "delete from members where id = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, id);
			int result = pstat.executeUpdate();
			return result;
		}
	}

}
