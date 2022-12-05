package com.sanjaythiru.service.lichessapiexperiments.controller;

import com.sanjaythiru.service.lichessapiexperiments.domain.enums.SpeedEnum;
import com.sanjaythiru.service.lichessapiexperiments.dto.TopOpeningsDto;
import com.sanjaythiru.service.lichessapiexperiments.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppService appService;

    @GetMapping("/top/openings/{speed}")
    public TopOpeningsDto getTopOpenings(@PathVariable String speed,
                                         @RequestParam(name = "nbr", required = false, defaultValue = "100") Long nbr) {
        LOGGER.info("Request - getTopOpenings, speed : {}, nbr : {}", speed, nbr);
        // TODO : Return sorted response
        return appService.getTopOpenings(SpeedEnum.getValueOf(speed), nbr);
    }
}
