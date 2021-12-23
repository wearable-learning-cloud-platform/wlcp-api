package org.wlcp.wlcpapi.datamodel.master.transition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: Transition
 *
 */
@Entity
@Table(name = "TRANSITION")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "transitionId")
public class Transition implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TRANSITION_ID")
	private String transitionId;
	
	@ManyToOne
	@JoinColumn(name = "GAME")
	private Game game;
	
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "CONNECTION")
	private Connection connection;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ACTIVE_TRANSITIONS")
    @MapKeyColumn(name = "SCOPE")
	@Fetch(FetchMode.SELECT)
	private Map<String, String> activeTransitions = new HashMap<String, String>();
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "SINGLE_BUTTON_PRESS")
    @MapKeyColumn(name = "SCOPE")
	@Fetch(FetchMode.SELECT)
	private Map<String, SingleButtonPress> singleButtonPresses = new HashMap<String, SingleButtonPress>();
	
	@OneToMany(mappedBy="transition", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@MapKey(name = "scope")
	private Map<String, SequenceButtonPress> sequenceButtonPresses = new HashMap<String, SequenceButtonPress>();
	
	@OneToMany(mappedBy="transition", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@MapKey(name = "scope")
	private Map<String, KeyboardInput> keyboardInputs = new HashMap<String, KeyboardInput>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "TIMER_DURATION")
	@MapKeyColumn(name = "SCOPE")
	@Fetch(FetchMode.SELECT)
	private Map<String, TimerDuration> timerDurations = new HashMap<String, TimerDuration>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "RANDOM")
	@MapKeyColumn(name = "SCOPE")
	@Fetch(FetchMode.SELECT)
	private Map<String, Randoms> randoms = new HashMap<String, Randoms>();

	public Transition() {
		super();
	}

	public Transition(String transitionId, Game game, Connection connection, Map<String, String> activeTransitions,
			Map<String, SingleButtonPress> singleButtonPresses,
			Map<String, SequenceButtonPress> sequenceButtonPresses,
			Map<String, KeyboardInput> keyboardInputs,
			Map<String, TimerDuration> timerDurations,
			Map<String, Randoms> randoms) {
		super();
		this.transitionId = transitionId;
		this.game = game;
		this.connection = connection;
		this.activeTransitions = activeTransitions;
		this.singleButtonPresses = singleButtonPresses;
		this.sequenceButtonPresses = sequenceButtonPresses;
		this.keyboardInputs = keyboardInputs;
		this.timerDurations = timerDurations;
		this.randoms = randoms;
	}

	public String getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public Map<String, String> getActiveTransitions() {
		return activeTransitions;
	}

	public void setActiveTransitions(Map<String, String> activeTransitions) {
		this.activeTransitions = activeTransitions;
	}

	public Map<String, SingleButtonPress> getSingleButtonPresses() {
		return singleButtonPresses;
	}

	public void setSingleButtonPresses(Map<String, SingleButtonPress> singleButtonPresses) {
		this.singleButtonPresses = singleButtonPresses;
	}

	public Map<String, SequenceButtonPress> getSequenceButtonPresses() {
		return sequenceButtonPresses;
	}

	public void setSequenceButtonPresses(Map<String, SequenceButtonPress> sequenceButtonPresses) {
		this.sequenceButtonPresses = sequenceButtonPresses;
	}

	public Map<String, KeyboardInput> getKeyboardInputs() {
		return keyboardInputs;
	}

	public void setKeyboardInputs(Map<String, KeyboardInput> keyboardInputs) {
		this.keyboardInputs = keyboardInputs;
	}

	public Map<String, TimerDuration> getTimerDurations() {
		return timerDurations;
	}

	public void setTimerDurations(Map<String, TimerDuration> timerDurations) {
		this.timerDurations = timerDurations;
	}

	public Map<String, Randoms> getRandoms() {
		return randoms;
	}

	public void setRandoms(Map<String, Randoms> randoms) {
		this.randoms = randoms;
	}

}
