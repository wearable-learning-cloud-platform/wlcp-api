package org.wlcp.wlcpapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wlcp.wlcpapi.datamodel.master.ObjectStore;

public interface ObjectStoreRepository extends JpaRepository<ObjectStore, String> {

}
