package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import com.thoughtmechanix.licenses.config.ServiceConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtmechanix.licenses.repository.OrganizationRedisRepository;

@Component
public class OrganizationRestTemplateClient {
    
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    @Autowired
    OrganizationRedisRepository orgRedisRepo;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    private Organization checkRedisCache(String organizationId) {
        try {
            return orgRedisRepo.findOrganization(organizationId);
        }
        catch (Exception ex){
            logger.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRedisRepo.saveOrganization(org);
        }catch (Exception ex){
            logger.error("Unable to cache organization {} in Redis. Exception {}", org.getId(), ex);
        }
    }

    public Organization getOrganization(String organizationId){
        System.out.println(">>>>>>>>>>>>>>>> Executing OrganizationRestTemplateClient.");
       
        Organization org = checkRedisCache(organizationId);

        if (org!=null){
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, org);
            return org;
        }

        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);
        logger.debug("Now we are getting the info for this org: {}.", organizationId);
        logger.debug("Url to be invoked: {}", "http://zuulservice/api/organization/v1/organizations/"+ organizationId);
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://zuulservice/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        logger.debug("After retriving info for the org: {}.", organizationId);
         /*Save the record from cache*/
        org = restExchange.getBody();

        if (org!=null) {
            cacheOrganizationObject(org);
        }

        logger.debug("Returnin org: {}.", organizationId);
        return org;
    }
}