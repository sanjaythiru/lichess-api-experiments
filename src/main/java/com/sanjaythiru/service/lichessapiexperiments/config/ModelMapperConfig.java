package com.sanjaythiru.service.lichessapiexperiments.config;

import com.sanjaythiru.service.lichessapiexperiments.domain.User;
import com.sanjaythiru.service.lichessapiexperiments.lichessdto.subdto.UserLichessDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper userModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(UserLichessDto.class, User.class).addMappings(
                mapper -> {
                    mapper.map(src -> src.getPerfs().getBullet().getRating(),
                            ((destination, value) -> destination.setRating((Long) value)));
                }
        );
        return modelMapper;
    }
}
