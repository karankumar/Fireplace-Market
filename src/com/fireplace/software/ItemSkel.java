package com.fireplace.software;

public class ItemSkel {
	private String id;
	private String label;
	private String path;
	
	public ItemSkel(String ID, String Label, String Path) {
		this.id = ID;
		this.label = Label;
		this.path = Path;
	}
	
	public String getId() {
		return id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
