package org.wlcp.wlcpapi.dto;

import org.wlcp.wlcpapi.datamodel.master.Username;

public class CreateGameDto {
	public String gameId;
	public Integer teamCount;
	public Integer playersPerTeam;
	public Integer stateIdCount;
	public Integer transitionIdCount;
	public Integer connectionIdCount;
	public Username username;
	public Boolean visibility;
	public Boolean dataLog;
}
