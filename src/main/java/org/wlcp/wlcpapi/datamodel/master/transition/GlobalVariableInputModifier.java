package org.wlcp.wlcpapi.datamodel.master.transition;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GlobalVariableInputModifier {

	@Column(name = "VARIABLE_NAME")
	private String variableName;

	@Column(name = "OPERATOR")
	private String operator;

	@Column(name = "VALUE")
	private String value;

	@Column(name="logicalOperator")
	private String logicalOperator;

	public GlobalVariableInputModifier() {
		super();
	}

	public GlobalVariableInputModifier(String variableName, String operator, String value, String logicalOperator) {
		super();
		this.variableName = variableName;
		this.operator = operator;
		this.value = value;
		this.logicalOperator = logicalOperator;
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

	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

}