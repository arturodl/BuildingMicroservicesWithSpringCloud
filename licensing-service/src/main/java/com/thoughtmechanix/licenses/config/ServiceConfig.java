package com.thoughtmechanix.licenses.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig{

  @Value("${signing.key}")
  private String jwtSigningKey="";

  // Example code for getting a property from configuration
  @Value("${example.property}")
  private String exampleProperty;

  @Value("${bms.zuul.server.uri}")
  private String zuulServerUri="";

  @Value("${redis.server}")
  private String redisServer="";

  @Value("${redis.port}")
  private String redisPort="";

  public String getJwtSigningKey() {
    return jwtSigningKey;
  }

  public String getExampleProperty(){
    return exampleProperty;
  } 

  public String getZuulServerUri(){
    return zuulServerUri;
  }

  public String getRedisServer(){
    return redisServer;
  }

  public Integer getRedisPort(){
    return new Integer( redisPort ).intValue();
  }
  
}