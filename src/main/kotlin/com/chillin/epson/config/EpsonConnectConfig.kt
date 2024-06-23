package com.chillin.epson.config

import com.chillin.http.HttpClient
import okhttp3.FormBody
import okhttp3.OkHttpClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EpsonConnectProperties::class)
class EpsonConnectConfig {

    @Bean
    fun epsonConnectClient(): HttpClient {
        return HttpClient(OkHttpClient())
    }

    @Bean
    fun basicHeader(epsonConnectProperties: EpsonConnectProperties): String {
        return epsonConnectProperties.basicHeader()
    }

    @Bean
    fun authenticationHeader(epsonConnectProperties: EpsonConnectProperties): FormBody {
        return epsonConnectProperties.authenticationForm()
    }
}