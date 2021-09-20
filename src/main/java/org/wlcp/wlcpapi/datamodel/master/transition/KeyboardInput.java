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

/**
 * Entity implementation class for Entity: KeyboardInput
 *
 */
@Entity
@Table(name = "KEYBOARD_INPUT")
public class KeyboardInput implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "KEYBOARD_INPUT_ID")
	private String keyboardInputId;
	
	@ManyToOne
	@JoinColumn(name = "TRANSITION_ID")
	private Transition transition;
	
	@Column(name = "SCOPE")
	private String scope;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="KEYBOARD_INPUTS", joinColumns=@JoinColumn(name="KEYBOARD_INPUT_ID"))
	@Column(name="KEYBOARD_INPUT")
	@Fetch(FetchMode.SELECT)
	private List<String> keyboardInputs = new ArrayList<String>();

	public KeyboardInput() {
		super();
	}

	public KeyboardInput(String keyboardInputId, Transition transition, String scope, List<String> keyboardInputs) {
		super();
		this.keyboardInputId = keyboardInputId;
		this.transition = transition;
		this.scope = scope;
		this.keyboardInputs = keyboardInputs;
	}

	public String getKeyboardInputId() {
		return keyboardInputId;
	}

	public void setKeyboardInputId(String keyboardInputId) {
		this.keyboardInputId = keyboardInputId;
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

	public List<String> getKeyboardInputs() {
		return keyboardInputs;
	}

	public void setKeyboardInputs(List<String> keyboardInputs) {
		this.keyboardInputs = keyboardInputs;
	}
   
}
