package org.wlcp.wlcpapi.datamodel.master.state;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;

/**
 * Entity implementation class for Entity: StartState
 *
 */
@Entity
@Table(name = "START_STATE")
@PrimaryKeyJoinColumn(referencedColumnName = "STATE_ID")
public class StartState extends State implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public StartState() {
		super();
		setStateType(StateType.START_STATE);
	}
	
	public StartState(String stateId, Game game, StateType stateType, Float positionX, Float positionY, List<Connection> inputConnections, List<Connection> outputConnections) {
		super(stateId, game, stateType, positionX, positionY, inputConnections, outputConnections);
	}
   
}
