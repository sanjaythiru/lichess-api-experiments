package com.sanjaythiru.service.lichessapiexperiments.controller;

import com.sanjaythiru.service.lichessapiexperiments.domain.enums.SpeedEnum;
import com.sanjaythiru.service.lichessapiexperiments.dto.TopOpeningsDto;
import com.sanjaythiru.service.lichessapiexperiments.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping("/top/openings/{speed}")
    public TopOpeningsDto getTopOpenings(@PathVariable String speed,
                                         @RequestParam(name = "nbr", required = false, defaultValue = "100") Long nbr) {
        return appService.getTopOpenings(SpeedEnum.getValueOf(speed), nbr);
    }
}
