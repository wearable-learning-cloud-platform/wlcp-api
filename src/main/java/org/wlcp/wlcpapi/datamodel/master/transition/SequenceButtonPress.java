package org.wlcp.wlcpapi.datamodel.master.transition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Entity implementation class for Entity: SequenceButtonPress
 *
 */

@Entity
@Table(name = "SEQUENCE_BUTTON_PRESS")
public class SequenceButtonPress implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SEQUENCE_BUTTON_PRESS_ID")
	private String sequenceButtonPressId;
	
	@ManyToOne
	@JoinColumn(name = "TRANSITION_ID")
	private Transition transition;
	
	@Column(name = "SCOPE")
	private String scope;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="SEQUENCE_BUTTON_PRESSES", joinColumns=@JoinColumn(name="SEQUENCE_BUTTON_PRESS_ID"))
	@Column(name="SEQUENCES")
	@Fetch(FetchMode.SELECT)
	private List<String> sequences = new ArrayList<String>();

	public SequenceButtonPress() {
		super();
	}

	public SequenceButtonPress(String sequenceButtonPressId, Transition transition, String scope, List<String> sequences) {
		super();
		this.sequenceButtonPressId = sequenceButtonPressId;
		this.transition = transition;
		this.scope = scope;
		this.sequences = sequences;
	}

	public String getSequenceButtonPressId() {
		return sequenceButtonPressId;
	}

	public void setSequenceButtonPressId(String sequenceButtonPressId) {
		this.sequenceButtonPressId = sequenceButtonPressId;
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

	public List<String> getSequences() {
		return sequences;
	}

	public void setSequences(List<String> sequences) {
		this.sequences = sequences;
	}

}
