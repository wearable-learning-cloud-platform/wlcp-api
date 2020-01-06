package org.wlcp.wlcpapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;

public interface GameRepository extends JpaRepository<Game, String> {

	public List<Game> findByUsername(Username username);
	
}
