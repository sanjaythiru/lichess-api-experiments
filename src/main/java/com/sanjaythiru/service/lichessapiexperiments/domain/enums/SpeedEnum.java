package com.sanjaythiru.service.lichessapiexperiments.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SpeedEnum {

    @JsonProperty("correspondence")
    CORRESPONDENCE("correspondence"),

    @JsonProperty("ultraBullet")
    ULTRABULLET("ultrabullet"),

    @JsonProperty("bullet")
    BULLET("bullet"),

    @JsonProperty("blitz")
    BLITZ("blitz"),

    @JsonProperty("rapid")
    RAPID("rapid"),

    @JsonProperty("classical")
    CLASSICAL("classical");

    public final String name;

    SpeedEnum(String name) {
        this.name = name;
    }

    public static SpeedEnum getValueOf(String name) {
        for(SpeedEnum speedEnum : SpeedEnum.values()) {
            if(speedEnum.name.equals(name)) {
                return speedEnum;
            }
        }
        return null;
    }
}
