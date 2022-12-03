package com.sanjaythiru.service.lichessapiexperiments.lichessdto;

import lombok.Data;

import java.util.List;

@Data
public class LeaderboardLichessDto {
    List<UserLichessDto> users;
}
