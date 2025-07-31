package org.wlcp.wlcpapi.datamodel.master.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: State
 *
 */
@Entity
@Table(name = "STATE")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="stateType", defaultImpl=State.class)
@JsonSubTypes({@Type(value = StartState.class, name="START_STATE"), @Type(value = OutputState.class, name = "OUTPUT_STATE")})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stateId")
public class State implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "STATE_ID")
	private String stateId;
	
	@ManyToOne
	@JoinColumn(name = "GAME")
	private Game game;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATE_TYPE")
	private StateType stateType;
	
	@Column(name = "POSITION_X")
	private Float positionX;
	
	@Column(name = "POSITION_Y")
	private Float positionY;
	
	@JoinTable(name = "INPUT_CONNECTIONS", joinColumns = @JoinColumn(name = "STATE_ID", referencedColumnName = "STATE_ID"), inverseJoinColumns = @JoinColumn(name = "CONNECTION_ID", referencedColumnName = "CONNECTION_ID"))
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JsonIdentityReference(alwaysAsId = true)
	List<Connection> inputConnections = new ArrayList<Connection>();
	
	@JoinTable(name = "OUTPUT_CONNECTIONS", joinColumns = @JoinColumn(name = "STATE_ID", referencedColumnName = "STATE_ID"), inverseJoinColumns = @JoinColumn(name = "CONNECTION_ID", referencedColumnName = "CONNECTION_ID"))
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JsonIdentityReference(alwaysAsId = true)
	List<Connection> outputConnections = new ArrayList<Connection>();

	public State() {
		super();
	}
	
	public State(String stateId, Game game, StateType stateType, Float positionX, Float positionY,
			List<Connection> inputConnections, List<Connection> outputConnections) {
		super();
		this.stateId = stateId;
		this.game = game;
		this.stateType = stateType;
		this.positionX = positionX;
		this.positionY = positionY;
		this.inputConnections = inputConnections;
		this.outputConnections = outputConnections;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}

	public Float getPositionX() {
		return positionX;
	}

	public void setPositionX(Float positionX) {
		this.positionX = positionX;
	}

	public Float getPositionY() {
		return positionY;
	}

	public void setPositionY(Float positionY) {
		this.positionY = positionY;
	}

	public List<Connection> getInputConnections() {
		return inputConnections;
	}

	public void setInputConnections(List<Connection> inputConnections) {
		this.inputConnections = inputConnections;
	}

	public List<Connection> getOutputConnections() {
		return outputConnections;
	}

	public void setOutputConnections(List<Connection> outputConnections) {
		this.outputConnections = outputConnections;
	}

}
