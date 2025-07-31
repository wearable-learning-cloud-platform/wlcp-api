package org.wlcp.wlcpapi.datamodel.master.connection;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;



/**
 * Entity implementation class for Entity: Connection
 *
 */
@Entity
@Table(name = "CONNECTION")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "connectionId")
public class Connection implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "CONNECTION_ID")
	private String connectionId;
	
	@ManyToOne
	@JoinColumn(name = "GAME")
	private Game game;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "CONNECTION_FROM")
	private State connectionFrom;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "CONNECTION_TO")
	private State connectionTo;
	
	@Column(name = "BACKWARDS_LOOP")
	private Boolean backwardsLoop;
	
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "TRANSITION")
	@JsonIdentityReference(alwaysAsId = true)
	private Transition transition;

	public Connection() {
		super();
	}

	public Connection(String connectionId, Game game, State connectionFrom, State connectionTo, Boolean backwardsLoop,
		   Transition transition) {
		super();
		this.connectionId = connectionId;
		this.game = game;
		this.connectionFrom = connectionFrom;
		this.connectionTo = connectionTo;
		this.backwardsLoop = backwardsLoop;;
		this.transition = transition;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public State getConnectionFrom() {
		return connectionFrom;
	}

	public void setConnectionFrom(State connectionFrom) {
		this.connectionFrom = connectionFrom;
	}

	public State getConnectionTo() {
		return connectionTo;
	}

	public void setConnectionTo(State connectionTo) {
		this.connectionTo = connectionTo;
	}

	public Boolean getBackwardsLoop() {
		return backwardsLoop;
	}

	public void setBackwardsLoop(Boolean backwardsLoop) {
		this.backwardsLoop = backwardsLoop;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}
	
}
