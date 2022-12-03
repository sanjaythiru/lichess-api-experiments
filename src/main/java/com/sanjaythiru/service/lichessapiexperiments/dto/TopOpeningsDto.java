package com.sanjaythiru.service.lichessapiexperiments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class TopOpeningsDto {
    private Map<String, Long> openingsAggregate;
}
