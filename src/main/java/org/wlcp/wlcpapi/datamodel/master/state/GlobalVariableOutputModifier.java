package org.wlcp.wlcpapi.datamodel.master.state;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GlobalVariableOutputModifier {
	
	@Column(name = "VARIABLE_NAME")
	private String variableName;
	
	@Column(name = "OPERATOR")
	private String operator;
	
	@Column(name = "VALUE")
	private String value;
	
	public GlobalVariableOutputModifier() {
		super();
	}

	public GlobalVariableOutputModifier(String variableName, String operator, String value) {
		super();
		this.variableName = variableName;
		this.operator = operator;
		this.value = value;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
