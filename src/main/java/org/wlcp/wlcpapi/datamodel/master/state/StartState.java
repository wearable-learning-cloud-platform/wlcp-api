package org.wlcp.wlcpapi.datamodel.master.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.GlobalVariable;
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
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "GLOBAL_VARIABLE")
	@Fetch(FetchMode.SELECT)
	private List<GlobalVariable> globalVariables = new ArrayList<GlobalVariable>();

	public StartState() {
		super();
		setStateType(StateType.START_STATE);
	}
	
	public StartState(String stateId, Game game, StateType stateType, Float positionX, Float positionY, List<Connection> inputConnections, List<Connection> outputConnections) {
		super(stateId, game, stateType, positionX, positionY, inputConnections, outputConnections);
	}

	public List<GlobalVariable> getGlobalVariables() {
		return globalVariables;
	}

	public void setGlobalVariables(List<GlobalVariable> globalVariables) {
		this.globalVariables = globalVariables;
	}
   
}
