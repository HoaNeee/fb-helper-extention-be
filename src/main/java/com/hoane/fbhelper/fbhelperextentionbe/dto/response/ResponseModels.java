package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

public class ResponseModels {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record AdminStatisticDeviceResponse(
            Integer countDeviceActive,
            Integer countDeviceInactive
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record AdminStatisticIpAddressResponse(
            Integer countIpActive,
            Integer countIpInactive
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record AdminStatisticUserResponse(
            Integer countUserActive,
            Integer countUserInactive,
            Integer countUserLocked,
            Integer countUserPending
    ) {
    }
}
