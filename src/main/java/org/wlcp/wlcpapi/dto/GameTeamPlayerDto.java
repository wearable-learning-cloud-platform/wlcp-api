package org.wlcp.wlcpapi.dto;

public class GameTeamPlayerDto {

	public String gameId;
	public int teamCount;
	public int playerCount;
	
	public GameTeamPlayerDto(String gameId, int teamCount, int playerCount) {
		this.gameId = gameId;
		this.teamCount = teamCount;
		this.playerCount = playerCount;
	}
}
