package org.wlcp.wlcpapi.archive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;

public interface ArchiveGameRepository extends JpaRepository<Game, String> {

	public List<Game> findByUsername(Username username);
	
}
