package com.thoughtmechanix.licenses.services;

import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thoughtmechanix.licenses.clients.OrganizationDiscoveryClient;
import com.thoughtmechanix.licenses.clients.OrganizationFeignClient;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.util.List;
import java.util.UUID;
import java.util.Random;
import java.util.ArrayList;

@Service
public class LicenseService {

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;

	@Autowired
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
	OrganizationDiscoveryClient organizationDiscoveryClient;
    
    @HystrixCommand(
        commandProperties=
        {@HystrixProperty(
        name="execution.isolation.thread.timeoutInMilliseconds",
        value="10000")})
	public License getLicense(String organizationId,String licenseId) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(
		organizationId, licenseId);

		Organization org = getOrganization(organizationId);

        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
	}
    
    private Organization getOrganization(String organizationId) {
        randomlyRunLong();
        return organizationRestClient.getOrganization(organizationId);
    }

	private Organization retrieveOrgInfo(String organizationId, String clientType){
		System.out.println("Getting in retrieveOrgInfo");

        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
            	System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }

    public License getLicense(String organizationId,String licenseId, String clientType) {
    	System.out.println("Before getting findByOrganizationId");
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        System.out.println("Before retriving org info");
        Organization org = retrieveOrgInfo(organizationId, clientType);

        System.out.println("Before returning info, org is: "+org);
        System.out.println("Before returning info, license is: "+license);
        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
	}

    private void randomlyRunLong(){
        Random rand = new Random();

        int randomNum = rand.nextInt((3 - 1) + 1) + 1;

        if (randomNum==3) 
            sleep();
    }

    private void sleep(){
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList")
	public List<License> getLicensesByOrg(String organizationId){
        randomlyRunLong();

		return licenseRepository.findByOrganizationId( organizationId );
	}

    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();

        License license = new License()
                                .withId("0000000-00-00000")
                                .withOrganizationId( organizationId )
                                .withProductName(
                                "Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

	public void saveLicense(License license){
		license.withId( UUID.randomUUID().toString());
		licenseRepository.save(license);
	}	

    public void updateLicense(License license){
      licenseRepository.save(license);
    }

    public void deleteLicense(License license){
        licenseRepository.delete( license.getLicenseId());
	}
}