package com.example.kp3coutsourcingproject.redis.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ModelMapper modelMapper;

    // key값으로 value를 저장한다
    // 만약 만료 시간을 지정하는 경우엔 time 과 단위를 함께 세팅한다
    public void put(String key, Object value, Long expirationTime) {
        if (expirationTime != null) {
            redisTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    // key값에 대한 데이터를 삭제한다
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public <T> T get(String key, Class<T> tClass) {
        // key 값으로 value 를 가져온다
        Object object = redisTemplate.opsForValue().get(key);

        if (object != null) {
            if (object instanceof List<?>) {
                return modelMapper.map(object, tClass);
            } else {
                return tClass.cast(object);
            }
        }
        return null;
    }

    public boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }
    public void setExpireTime(String key, Long expirationTime) {
        redisTemplate.expire(key, expirationTime, TimeUnit.SECONDS);
    }
    public Long getExpireTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}
