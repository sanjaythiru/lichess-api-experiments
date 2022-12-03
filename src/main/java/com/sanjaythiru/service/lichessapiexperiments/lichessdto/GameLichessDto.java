package com.sanjaythiru.service.lichessapiexperiments.lichessdto;

import com.sanjaythiru.service.lichessapiexperiments.domain.enums.SpeedEnum;
import lombok.Data;

@Data
public class GameLichessDto {
    private String id;
    private SpeedEnum speed;
    private OpeningLichessDto opening;
}
