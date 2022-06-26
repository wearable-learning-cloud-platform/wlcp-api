package org.wlcp.wlcpapi.archive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wlcp.wlcpapi.datamodel.master.Username;

public interface ArchiveUsernameRepository extends JpaRepository<Username, String> {

}
