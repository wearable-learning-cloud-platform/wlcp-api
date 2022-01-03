package org.wlcp.wlcpapi.datamodel.master.state;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.wlcp.wlcpapi.datamodel.enums.VariableType;

@Embeddable
public class GlobalVariable {

	@Column(name = "NAME")
	private String name;

	@Column(name = "TYPE")
	@Enumerated(EnumType.ORDINAL)
	private VariableType type;

	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;

	public GlobalVariable() {
		super();
	}

	public GlobalVariable(String name, VariableType type, String defaultValue) {
		super();
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VariableType getType() {
		return type;
	}

	public void setType(VariableType type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}