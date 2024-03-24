package org.wlcp.wlcpapi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.dto.GameTeamPlayerDto;
import org.wlcp.wlcpapi.dto.SaveDto;

public interface GameService {
	
	public List<GameTeamPlayerDto> getPrivateGames(String usernameId);
	
	public List<GameTeamPlayerDto> getPublicGames();
	
	public Game loadGame(String gameId);
	
	public Game loadGameVersion(String gameId);
	
	public Game saveGame(SaveDto saveDto);
	
	public Game revertGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public Game copyGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public Game copyArchivedGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public Game renameGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public void deleteGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public void importGame(MultipartFile file);
	
	public String exportGame(String gameId);

}
