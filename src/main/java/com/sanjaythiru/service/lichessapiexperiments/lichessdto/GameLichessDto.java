package com.sanjaythiru.service.lichessapiexperiments.lichessdto;

import com.sanjaythiru.service.lichessapiexperiments.domain.enums.SpeedEnum;
import com.sanjaythiru.service.lichessapiexperiments.lichessdto.subdto.OpeningLichessDto;
import lombok.Data;

@Data
public class GameLichessDto implements IDto {
    private String id;
    private SpeedEnum speed;
    private OpeningLichessDto opening;
}
