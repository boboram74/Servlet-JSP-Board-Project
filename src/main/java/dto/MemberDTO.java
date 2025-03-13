package dto;

import java.sql.Timestamp;

public class MemberDTO {

	private String id;
	private String pw;
	private String name;
	private String email;
	private String tel;
	private int post;
	private String address1;
	private String address2;
	private Timestamp date;

	public Timestamp getDate() {
		return date;
	}

	public MemberDTO() {
		super();

	}

	public MemberDTO(String id, String pw, String name, String email, String tel, int post, String address1,
			String address2) { // 회원가입
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.post = post;
		this.address1 = address1;
		this.address2 = address2;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public MemberDTO(String id, String name, String email, String tel, int post, String address1, String address2,
			Timestamp date) { // 로그인
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.post = post;
		this.address1 = address1;
		this.address2 = address2;
		this.date = date;
	}

	public MemberDTO(String id, String name, String email, String tel, int post, String address1, String address2) { // 회원정보수정
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.post = post;
		this.address1 = address1;
		this.address2 = address2;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getPost() {
		return post;
	}

	public void setPost(int post) {
		this.post = post;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

}
