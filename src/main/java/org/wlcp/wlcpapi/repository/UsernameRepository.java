package org.wlcp.wlcpapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wlcp.wlcpapi.datamodel.master.Username;

public interface UsernameRepository extends JpaRepository<Username, String> {

}
