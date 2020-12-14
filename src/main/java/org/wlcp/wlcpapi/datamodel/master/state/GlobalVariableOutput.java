package org.wlcp.wlcpapi.datamodel.master.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GLOBAL_VARIABLE_OUTPUT")
public class GlobalVariableOutput implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "GLOBAL_VARIABLE_ID")
	private String globalVariableId;
	
	@ManyToOne
	@JoinColumn(name = "OUTPUT_STATE_ID")
	private OutputState outputState;
	
	@Column(name = "SCOPE")
	private String scope;
	
	@ElementCollection
	@CollectionTable(name="VARIABLE_MODIFIERS")
	private List<GlobalVariableOutputModifier> variableModifiers = new ArrayList<GlobalVariableOutputModifier>();

	public GlobalVariableOutput() {
		super();
	}

	public GlobalVariableOutput(String globalVariableId, OutputState outputState, String scope,
			List<GlobalVariableOutputModifier> variableModifiers) {
		super();
		this.globalVariableId = globalVariableId;
		this.outputState = outputState;
		this.scope = scope;
		this.variableModifiers = variableModifiers;
	}

	public String getGlobalVariableId() {
		return globalVariableId;
	}

	public void setGlobalVariableId(String globalVariableId) {
		this.globalVariableId = globalVariableId;
	}

	public OutputState getOutputState() {
		return outputState;
	}

	public void setOutputState(OutputState outputState) {
		this.outputState = outputState;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<GlobalVariableOutputModifier> getVariableModifiers() {
		return variableModifiers;
	}

	public void setVariableModifiers(List<GlobalVariableOutputModifier> variableModifiers) {
		this.variableModifiers = variableModifiers;
	}

}
