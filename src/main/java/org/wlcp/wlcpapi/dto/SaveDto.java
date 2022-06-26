package org.wlcp.wlcpapi.dto;

import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.GameSave;

public class SaveDto {

	public Game game;
	public GameSave gameSave;
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public GameSave getGameSave() {
		return gameSave;
	}
	public void setGameSave(GameSave gameSave) {
		this.gameSave = gameSave;
	}
	
}