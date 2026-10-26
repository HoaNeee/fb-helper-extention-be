package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.BasePageRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.PageResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.ResponseModels;
import com.hoane.fbhelper.fbhelperextentionbe.entity.IpAddress;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Pagination;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.IpAddressService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ip-addresses")
public class IpAddressController {
    @Autowired
    private IpAddressService ipAddressService;

    @PreAuthorize("@userService.admin")
    @GetMapping
    public ResponseEntity<PageResponse<List<IpAddress>>> getAllIpAddresses(@Valid @ModelAttribute BasePageRequest basePageRequest, @RequestParam(required = false) String status, @RequestParam(required = false) String keyword) {
        Page<IpAddress> page = ipAddressService.getAllIpAddresses(basePageRequest.toPageable(), status, keyword);
        List<IpAddress> ipAddresses = page.getContent();
        Pagination pagination = new Pagination(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext());

        return ResponseEntity.ok(new PageResponse<>(200, "Get list IP addresses successfully", true, ipAddresses, pagination));
    }

    @PreAuthorize("@userService.admin")
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<ResponseModels.AdminStatisticIpAddressResponse>> getIpAddressStatistics() {

        Integer countIpActive = ipAddressService.countIpActive();
        Integer countIpInactive = ipAddressService.countIpInactive();

        return ApiResponse.success(200, "Get IP address statistics successfully", new ResponseModels.AdminStatisticIpAddressResponse(countIpActive, countIpInactive));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<IpAddress>> getIpAddressDetail(HttpServletRequest rawRequest) {
        String ipAddress = RequestUtils.getClientIp(rawRequest);
        IpAddress ip = ipAddressService.findByIpAddressOrThrow(ipAddress);
        return ApiResponse.success(200, "Get IP address detail successfully", ip);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IpAddress>> createNewIp(HttpServletRequest rawRequest) {
        String ipAddress = RequestUtils.getClientIp(rawRequest);
        IpAddress entity = ipAddressService.createIpAddress(ipAddress);
        return ApiResponse.success(201, "Add IP address successfully", entity);
    }

    @PreAuthorize("@userService.admin")
    @PatchMapping("/update-status")
    public ResponseEntity<ApiResponse<Void>> updateStatusIpAddress(@Valid @RequestBody RequestModels.UpdateStatusIpAddressRequest request) {

        ipAddressService.updateStatusIpAddress(request.ipAddress(), request.status());

        return ApiResponse.success(200, "Update status IP address successfully", null);
    }
}
