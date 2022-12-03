package com.sanjaythiru.service.lichessapiexperiments.domain;

import com.sanjaythiru.service.lichessapiexperiments.domain.enums.SpeedEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Game {
    private String id;
    // TODO : Will add all the fields in later stages of development
    private SpeedEnum speed;
    private String openingName;
    private String openingEco;
}
