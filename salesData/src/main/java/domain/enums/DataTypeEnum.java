package domain.enums;

public enum DataTypeEnum implements Enumeration{

	SALESMAN_DATA("001", "SALESMAN_DATA"), CLIENT_DATA("002", "CLIENT_DATA"), SALE_DATA("003", "SALE_DATA");

	private String id;
	private String description;

	private DataTypeEnum(String id, String description) {
		this.id = id;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
