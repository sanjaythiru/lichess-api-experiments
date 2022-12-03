package com.sanjaythiru.service.lichessapiexperiments.domain.enums;

public enum SpeedEnum {
    ULTRABULLET("ultrabullet"),
    BULLET("bullet"),
    BLITZ("blitz"),
    RAPID("rapid"),
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
