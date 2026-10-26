package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.IpAddress;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.IpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IpAddressRepository extends JpaRepository<IpAddress, String> {
    Boolean existsIpAddressById(String address);

    IpAddress findIpAddressById(String ip);

    @Query("SELECT ip FROM IpAddress ip " +
            "WHERE (:keyword IS NULL OR ip.id LIKE %:keyword% OR LOWER(ip.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR ip.status = :status)")
    Page<IpAddress> findAll(Pageable pageable, String keyword, IpStatus status);

    Integer countByStatus(IpStatus status);
}
