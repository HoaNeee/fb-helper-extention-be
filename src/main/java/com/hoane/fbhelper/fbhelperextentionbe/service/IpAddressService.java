package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.entity.IpAddress;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.IpStatus;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.IpAddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IpAddressService {

    @Autowired
    private IpAddressRepository ipAddressRepository;

    @Autowired
    private GeoIpService geoIpService;

    public IpAddress findByIpAddress(String ipAddress) {
        return ipAddressRepository.findIpAddressById(ipAddress);
    }

    public Integer countIpActive() {
        return ipAddressRepository.countByStatus(IpStatus.ACTIVE);
    }

    public Integer countIpInactive() {
        return ipAddressRepository.countByStatus(IpStatus.INACTIVE);
    }

    @Transactional
    public IpAddress createIpAddress(String ipAddress) {
        IpAddress ipAddressEntity = ipAddressRepository.findIpAddressById(ipAddress);
        if (ipAddressEntity == null) {
            ipAddressEntity = new IpAddress();
            ipAddressEntity.setId(ipAddress);

            Map<String, String> location = geoIpService.getLocation(ipAddress);
            if (!location.isEmpty()) {
                if (location.get("error") == null) {
                    String country = location.get("country");
                    String city = location.get("city");
                    ipAddressEntity.setAddress(city + ", " + country);
                } else {
                    System.out.println("Error getting location for IP " + ipAddress + ": " + location.get("error"));
                }
            }

            ipAddressEntity = ipAddressRepository.save(ipAddressEntity);
        }
        return ipAddressEntity;
    }

    @Transactional
    public IpAddress findByIpAddressOrCreate(String ipAddress) {
        IpAddress ipAddressEntity = findByIpAddress(ipAddress);
        if (ipAddressEntity != null) {
            return ipAddressEntity;
        }
        return createIpAddress(ipAddress);
    }

    public IpAddress findByIpAddressOrThrow(String ipAddress) {
        return ipAddressRepository.findById(ipAddress).orElseThrow(() -> new ResourceNotFoundException("ip", "IP Address not found with id: " + ipAddress));
    }

    public Page<IpAddress> getAllIpAddresses(Pageable pageable, String status, String keyword) {
        IpStatus ipStatus = status != null && !status.trim().isEmpty() ? IpStatus.valueOf(status.trim()) : null;
        keyword = keyword != null && !keyword.trim().isEmpty() ? keyword.trim() : null;

        return ipAddressRepository.findAll(pageable, keyword, ipStatus);
    }

    //maybe dont need
    @Transactional
    public void deleteIpAddress(String ipAddress) {
        ipAddressRepository.deleteById(ipAddress);
    }

    @Transactional
    public void updateStatusIpAddress(String ip, IpStatus status) {
        IpAddress ipAddress = findByIpAddressOrThrow(ip);
        ipAddress.setStatus(status);
        ipAddressRepository.save(ipAddress);
    }


}
