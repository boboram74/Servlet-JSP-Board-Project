package dto;

public class FilesDTO {

	private int id;
	private String originName;
	private String sysName;
	private int parent_seq;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public int getParent_seq() {
		return parent_seq;
	}

	public void setParent_seq(int parent_seq) {
		this.parent_seq = parent_seq;
	}

	public FilesDTO(int id, String originName, String sysName, int parent_seq) {
		super();
		this.id = id;
		this.originName = originName;
		this.sysName = sysName;
		this.parent_seq = parent_seq;
	}

	public FilesDTO() {
		super();

	}

}