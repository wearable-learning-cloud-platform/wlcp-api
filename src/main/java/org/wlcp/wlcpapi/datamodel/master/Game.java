package org.wlcp.wlcpapi.datamodel.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: Game
 *
 */
@Entity
@Table(name = "GAME")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gameId")
public class Game implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "GAME_ID")
	private String gameId;

	@Column(name = "TEAM_COUNT")
	private Integer teamCount;
	
	@Column(name = "PLAYERS_PER_TEAM")
	private Integer playersPerTeam;
	
	@ManyToOne
	@JoinColumn(name = "USERNAME", nullable = false)
	@JsonIgnoreProperties({"password", "firstName", "lastName", "emailAddress"})
	private Username username;
	
	@Column(name = "VISIBILITY")
	private Boolean visibility;
	
	@Column(name = "STATE_ID_COUNT")
	private Integer stateIdCount;
	
	@Column(name = "TRANSITION_ID_COUNT")
	private Integer transitionIdCount;
	
	@Column(name = "CONNECTION_ID_COUNT")
	private Integer connectionIdCount;
	
	@Column(name = "DATA_LOG")
	private Boolean dataLog;
	
	@JoinTable(name = "GAME_STATES", joinColumns = @JoinColumn(name = "GAME_ID", referencedColumnName = "GAME_ID"), inverseJoinColumns = @JoinColumn(name = "STATE_ID", referencedColumnName = "STATE_ID"))
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<State> states = new ArrayList<State>();
	
	@JoinTable(name = "GAME_CONNECTIONS", joinColumns = @JoinColumn(name = "GAME_ID", referencedColumnName = "GAME_ID"), inverseJoinColumns = @JoinColumn(name = "CONNECTION_ID", referencedColumnName = "CONNECTION_ID"))
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<Connection> connections = new ArrayList<Connection>();
	
	@JoinTable(name = "GAME_TRANSITIONS", joinColumns = @JoinColumn(name = "GAME_ID", referencedColumnName = "GAME_ID"), inverseJoinColumns = @JoinColumn(name = "TRANSITION_ID", referencedColumnName = "TRANSITION_ID"))
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<Transition> transitions = new ArrayList<Transition>();

	public Game() {
		super();
	}
	
	public Game(String gameId, Integer teamCount, Integer playersPerTeam, Username username, Boolean visibility,
			Integer stateIdCount, Integer transitionIdCount, Integer connectionIdCount, Boolean dataLog) {
		super();
		this.gameId = gameId;
		this.teamCount = teamCount;
		this.playersPerTeam = playersPerTeam;
		this.username = username;
		this.visibility = visibility;
		this.stateIdCount = stateIdCount;
		this.transitionIdCount = transitionIdCount;
		this.connectionIdCount = connectionIdCount;
		this.dataLog = dataLog;
	}

	public Game(String gameId, Integer teamCount, Integer playersPerTeam, Username username,
			Boolean visibility, Boolean dataLog) {
		super();
		this.gameId = gameId;
		this.teamCount = teamCount;
		this.playersPerTeam = playersPerTeam;
		this.username = username;
		this.visibility = visibility;
		this.dataLog = dataLog;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Integer getTeamCount() {
		return teamCount;
	}

	public void setTeamCount(Integer teamCount) {
		this.teamCount = teamCount;
	}

	public Integer getPlayersPerTeam() {
		return playersPerTeam;
	}

	public void setPlayersPerTeam(Integer playersPerTeam) {
		this.playersPerTeam = playersPerTeam;
	}

	public Username getUsername() {
		return username;
	}

	public void setUsername(Username username) {
		this.username = username;
	}

	public Boolean getVisibility() {
		return visibility;
	}

	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
	}

	public Integer getStateIdCount() {
		return stateIdCount;
	}

	public void setStateIdCount(Integer stateIdCount) {
		this.stateIdCount = stateIdCount;
	}

	public Integer getTransitionIdCount() {
		return transitionIdCount;
	}

	public void setTransitionIdCount(Integer transitionIdCount) {
		this.transitionIdCount = transitionIdCount;
	}

	public Integer getConnectionIdCount() {
		return connectionIdCount;
	}

	public void setConnectionIdCount(Integer connectionIdCount) {
		this.connectionIdCount = connectionIdCount;
	}

	public Boolean getDataLog() {
		return dataLog;
	}

	public void setDataLog(Boolean dataLog) {
		this.dataLog = dataLog;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}
	
}
