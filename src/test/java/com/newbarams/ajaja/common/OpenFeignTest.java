package com.newbarams.ajaja.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

import com.newbarams.ajaja.global.config.OpenFeignConfig;

@ImportAutoConfiguration({
	OpenFeignConfig.class,
	FeignAutoConfiguration.class,
	HttpMessageConvertersAutoConfiguration.class
})
@SpringBootTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenFeignTest {
}
