package dto;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ReplyDTO {

	private int id;
	private String writer;
	private String contents;
	private Timestamp write_date;
	private int parent_seq;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getWrite_date() {
		ZonedDateTime dbTime = write_date.toLocalDateTime().atZone(ZoneId.of("UTC")); // 넘어온 시간
		ZonedDateTime koreaTime = dbTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); // 현재시간
		long timeDifference = now.toEpochSecond() - koreaTime.toEpochSecond(); // 시차
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");// yyyy/MM/dd 형식 반환
		String formattedDate = koreaTime.format(formatter);
		String result = "";

		if (timeDifference < (60)) // 1분 이내
			result = "방금 전";
		else if (timeDifference < (60 * 5)) // 5분 이내
			result = "5분 전";
		else if (timeDifference < (60 * 30)) // 1시간 이내
			result = "30분 전";
		else if (timeDifference < (60 * 60))
			result = "1시간 전";
		else
			result = formattedDate;

		return result;
	}

	public void setWrite_date(Timestamp write_date) {
		this.write_date = write_date;
	}

	public int getParent_seq() {
		return parent_seq;
	}

	public void setParent_seq(int parent_seq) {
		this.parent_seq = parent_seq;
	}

	public ReplyDTO() {
		super();
	}

	public ReplyDTO(int id, String writer, String contents, Timestamp write_date, int parent_seq) {
		super();
		this.id = id;
		this.writer = writer;
		this.contents = contents;
		this.write_date = write_date;
		this.parent_seq = parent_seq;
	}

	public ReplyDTO(int id, String contents) { // 댓글수정
		super();
		this.id = id;
		this.contents = contents;
	}
}