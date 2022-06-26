package org.wlcp.wlcpapi.archive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.wlcp.wlcpapi.datamodel.enums.SaveType;
import org.wlcp.wlcpapi.datamodel.master.GameSave;

public interface GameSaveRepository extends JpaRepository<GameSave, String> {

	public List<GameSave> findByMasterGameIdOrderByTimeStampAsc(@Param("masterGameId") String masterGameId);
	public List<GameSave> findByMasterGameId(String masterGameId);
	public List<GameSave> findByMasterGameIdAndType(String masterGameId, SaveType saveType);
	public GameSave findByReferenceGameId(String referenceGameId);
	
}
