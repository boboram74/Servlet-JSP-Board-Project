package dto;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BoardDTO {
	private String title;
	private String writer;
	private String contents;
	private Timestamp date;
	private int view;
	private int seq;

	public BoardDTO() {

	}

	public BoardDTO(String title, String contents, int seq) { // 게시글 수정
		super();
		this.title = title;
		this.contents = contents;
		this.seq = seq;
	}

	public BoardDTO(String title, String writer, String contents) { // 게시글 상세보기
		super();
		this.title = title;
		this.writer = writer;
		this.contents = contents;
	}

	public BoardDTO(int seq, String title, String writer, String contents) { // 파일업로드 처리
		super();
		this.seq = seq;
		this.title = title;
		this.writer = writer;
		this.contents = contents;
	}

	public BoardDTO(int seq, String title, String writer, Timestamp date, int view) { // 게시글 페이징 처리
		super();
		this.seq = seq;
		this.title = title;
		this.writer = writer;
		this.date = date;
		this.view = view;
	}

	public BoardDTO(int seq, String title, String writer, String contents, Timestamp date, int view) {
		super();
		this.title = title;
		this.writer = writer;
		this.contents = contents;
		this.date = date;
		this.view = view;
		this.seq = seq;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getDate() {
		ZonedDateTime dbTime = date.toLocalDateTime().atZone(ZoneId.of("UTC")); // 넘어온 시간
		ZonedDateTime koreaTime = dbTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); // 현재시간
		long timeDifference = now.toEpochSecond() - koreaTime.toEpochSecond(); // 시차
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");// yyyy/MM/dd 형식 반환
		String formattedDate = koreaTime.format(formatter);

		if (timeDifference < (60)) // 1분 이내
			return "방금 전";
		else if (timeDifference < (60 * 5)) // 5분 이내
			return "5분 전";
		else if (timeDifference < (60 * 30)) // 1시간 이내
			return "30분 전";
		else if (timeDifference < (60 * 60)) {
			return "1시간 전";
		} else
			return formattedDate;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

}