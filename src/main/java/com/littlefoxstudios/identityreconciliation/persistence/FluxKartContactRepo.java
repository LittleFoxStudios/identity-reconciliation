package com.littlefoxstudios.identityreconciliation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface FluxKartContactRepo extends JpaRepository<FluxKartContact, Long> {
    ArrayList<FluxKartContact> findByPhoneNumber(String phoneNumber);
    ArrayList<FluxKartContact> findByEmail(String email);
    FluxKartContact findByLinkedId(long linkedId);
}
