package org.wlcp.wlcpapi.archive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wlcp.wlcpapi.datamodel.enums.SaveType;
import org.wlcp.wlcpapi.datamodel.master.GameSave;

public interface GameSaveRepository extends JpaRepository<GameSave, String> {

	@Query("SELECT max(s.version) FROM GameSave s WHERE s.masterGameId = :masterGameId AND s.type = :saveType")
	public Integer maxMasterGameIdAndSaveType(@Param("masterGameId") String masterGameId, @Param("saveType") SaveType saveType);
	public List<GameSave> findByMasterGameId(String masterGameId);
	public List<GameSave> findByMasterGameIdAndType(String masterGameId, SaveType saveType);
	public GameSave findByMasterGameIdAndVersionAndType(String masterGameId, int version, SaveType type);
	
}
