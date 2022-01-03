package org.wlcp.wlcpapi.datamodel.master.transition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "GLOBAL_VARIABLE_INPUT")
public class GlobalVariableInput implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GLOBAL_VARIABLE_ID")
	private String globalVariableId;

	@ManyToOne
	@JoinColumn(name = "TRANSITION_ID")
	private Transition transition;

	@Column(name = "SCOPE")
	private String scope;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="GLOBAL_VARIABLE_INPUT_MODIFIERS")
	@Fetch(FetchMode.SELECT)
	private List<GlobalVariableInputModifier> globalVariableInputModifiers = new ArrayList<GlobalVariableInputModifier>();

	public GlobalVariableInput() {
		super();
	}

	public GlobalVariableInput(String globalVariableId, Transition transition, String scope,
			List<GlobalVariableInputModifier> globalVariableInputModifiers) {
		super();
		this.globalVariableId = globalVariableId;
		this.transition = transition;
		this.scope = scope;
		this.globalVariableInputModifiers = globalVariableInputModifiers;
	}

	public String getGlobalVariableId() {
		return globalVariableId;
	}

	public void setGlobalVariableId(String globalVariableId) {
		this.globalVariableId = globalVariableId;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<GlobalVariableInputModifier> getGlobalVariableInputModifiers() {
		return globalVariableInputModifiers;
	}

	public void setGlobalVariableInputModifiers(List<GlobalVariableInputModifier> globalVariableInputModifiers) {
		this.globalVariableInputModifiers = globalVariableInputModifiers;
	}

}