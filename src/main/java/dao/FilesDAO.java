package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.FilesDTO;

public class FilesDAO {
	private static FilesDAO INSTANCE;

	public synchronized static FilesDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FilesDAO();
		}
		return INSTANCE;
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}

	public int insert(FilesDTO dto) throws Exception {
		String sql = "insert into files values(files_seq.nextval, ?, ?, ?)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getOriginName());
			pstat.setString(2, dto.getSysName());
			pstat.setInt(3, dto.getParent_seq());
			return pstat.executeUpdate();
		}
	}

	public List<FilesDTO> findById(int seq) throws Exception {
		String sql = "select * from files where parent_seq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery();) {
				List<FilesDTO> list = new ArrayList<>();
				while (rs.next()) {
					int id = rs.getInt("id");
					String oriname = rs.getString("oriname");
					String name = rs.getString("sysname");
					int parent_seq = rs.getInt("parent_seq");
					list.add(new FilesDTO(id, oriname, name, parent_seq));
				}
				return list;
			}
		}
	}
}
