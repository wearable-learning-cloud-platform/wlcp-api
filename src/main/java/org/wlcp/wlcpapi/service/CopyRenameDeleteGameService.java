package org.wlcp.wlcpapi.service;

import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;

public interface CopyRenameDeleteGameService {
	
	public String copyGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	public String renameGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);
	public String deleteGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto);

}
