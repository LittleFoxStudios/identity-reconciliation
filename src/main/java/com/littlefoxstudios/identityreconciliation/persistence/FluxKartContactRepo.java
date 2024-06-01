package com.littlefoxstudios.identityreconciliation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FluxKartContactRepo extends JpaRepository<FluxKartContact, Long> {

}
