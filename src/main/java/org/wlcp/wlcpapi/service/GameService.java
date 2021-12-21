package org.wlcp.wlcpapi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.dto.GameDto;
import org.wlcp.wlcpapi.dto.SaveDto;

public interface GameService {
	
	public List<GameDto> getPrivateGames(String usernameId);
	
	public List<GameDto> getPublicGames();
	
	public Game loadGame(String gameId);
	
	public Game loadGameVersion(String gameId);
	
	public Game saveGame(SaveDto saveDto);
	
	public Game copyGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public Game copyArchivedGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public Game renameGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public void deleteGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	
	public void importGame(MultipartFile file);
	
	public String exportGame(String gameId);

}
