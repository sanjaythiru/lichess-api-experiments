package com.sanjaythiru.service.lichessapiexperiments.lichessdto;

import com.sanjaythiru.service.lichessapiexperiments.lichessdto.subdto.UserLichessDto;
import lombok.Data;

import java.util.List;

@Data
public class LeaderboardLichessDto implements IDto {
    List<UserLichessDto> users;
}
