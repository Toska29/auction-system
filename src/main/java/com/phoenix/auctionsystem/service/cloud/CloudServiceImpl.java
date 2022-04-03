package com.phoenix.auctionsystem.service.cloud;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudServiceImpl implements CloudService{

    private Cloudinary cloudinary;

    @Override
    public Map<?, ?> upload(byte[] bytes, Map<?, ?> params) throws IOException {
        return cloudinary.uploader().upload(bytes, params);
    }
}
