package com.phoenix.auctionsystem.util.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${CLOUDNAME}")
    private String cloudName;

    @Value("${APISECRET}")
    private String apiSecret;

    @Value("${APIKEY}")
    private String apiKey;

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;

    }

    public Cloudinary cloudinary(){
        return  new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_secret", apiSecret,
                "api_key", apiKey,
                "secret", true
        ));
    }
}
