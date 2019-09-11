package com.thoughtmechanix.licenses;

import com.thoughtmechanix.licenses.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Collections;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableResourceServer
public class Application {

    @Bean
    @Qualifier("oauth2RestTemplate")
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                            OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

	@LoadBalanced
    @Bean
    @Qualifier("restTemplate")
    public RestTemplate getRestTemplate(){  
        System.out.println(">>>>> executing the getRestTemplate method");
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();
        if (interceptors==null){
             System.out.println(">>>>> within the getRestTemplate method we got interceptor null");
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }
        else{
            System.out.println(">>>>> within the getRestTemplate method we got interceptor with elements");
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }

        return template;
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}