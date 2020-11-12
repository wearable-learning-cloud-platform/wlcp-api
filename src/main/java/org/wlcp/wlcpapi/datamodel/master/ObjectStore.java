package org.wlcp.wlcpapi.datamodel.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OBJECT_STORE")
public class ObjectStore {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column
	private String id;

	@Column
	private String name;
	
	@Column
	private String type;

	@Lob
	private byte[] data;

	public ObjectStore() {

	}
	
	public ObjectStore(String name, String type, byte[] data) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

}
