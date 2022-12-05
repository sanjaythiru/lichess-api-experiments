package com.sanjaythiru.service.lichessapiexperiments.lichessdto.subdto;

import com.sanjaythiru.service.lichessapiexperiments.domain.enums.TitleEnum;
import lombok.Data;

@Data
public class UserLichessDto {
    private String id;
    private String username;
    private UserPerformanceLichessDto perfs;
    private boolean online;
    private TitleEnum title;
}
