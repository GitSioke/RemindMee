package remind.me;

public class RemindTask {
	private String image;
	private Integer id;
	private String name;
	
	public RemindTask(Integer id, String name){
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		
		return this.image;
	}
}
