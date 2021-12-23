package org.wlcp.wlcpapi.dto;

import org.wlcp.wlcpapi.datamodel.enums.SaveType;

public class CopyRenameDeleteGameDto {
	
	public String usernameId;
	public String oldGameId;
	public String newGameId;
	public Boolean visibility;
	public SaveType saveType;

}
