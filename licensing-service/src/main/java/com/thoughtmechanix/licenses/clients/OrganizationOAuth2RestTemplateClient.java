package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import com.thoughtmechanix.licenses.utils.UserContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Component
public class OrganizationOAuth2RestTemplateClient {
    
    @Autowired
    @Qualifier("oauth2RestTemplate")
    OAuth2RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationOAuth2RestTemplateClient.class);

    public Organization getOrganization(String organizationId){
        //System.out.println(">>>>>>>>>>>>>>>> Executing OrganizationOAuth2RestTemplateClient.");
        logger.debug("Logger >>>>>>> In OrganizationOAuth2RestTemplateClient.getOrganization: {}", UserContext.getCorrelationId());        
        
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://localhost:5555/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}