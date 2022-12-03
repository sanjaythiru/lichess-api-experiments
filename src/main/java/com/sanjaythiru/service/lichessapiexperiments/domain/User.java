package com.sanjaythiru.service.lichessapiexperiments.domain;

import com.sanjaythiru.service.lichessapiexperiments.domain.enums.TitleEnum;
import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    private Long rating;
    private boolean online;
    private TitleEnum title;
}
