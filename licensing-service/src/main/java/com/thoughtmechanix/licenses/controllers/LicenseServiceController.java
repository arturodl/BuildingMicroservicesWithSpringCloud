package com.thoughtmechanix.licenses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;

import com.thoughtmechanix.licenses.utils.UserContextHolder;

@RestController 
@RequestMapping(value="/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

	@Autowired
	private LicenseService licenseService;

	@Autowired
    private HttpServletRequest request;

	private static final Logger logger = LoggerFactory.getLogger(LicenseServiceController.class);

	@RequestMapping(value="/",method = RequestMethod.GET)
    public List<License> getLicenses( @PathVariable("organizationId") String organizationId) {
        logger.debug(">>>>>>>>>>>>>> LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicensesByOrg(organizationId);
    }

    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    public License getLicenses( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId) {
    	logger.debug("Entering the license-service-controller, method getLicenses 2 params");
        logger.debug("Found tmx-correlation-id in license-service-controller: {} ", request.getHeader("tmx-correlation-id"));
		//System.out.println(">>>>>>>>>>>> Executing getLicenses.");
		//System.out.println(">>>>>>>>>>>> The correlationid from getLicenses is: "+UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicense(organizationId, licenseId);
    }

	@RequestMapping(value="/{licenseId}/{clientType}", method = RequestMethod.GET)
	public License getLicensesWithClient(
			@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId,
			@PathVariable("clientType") String clientType) {
		return licenseService.getLicense(organizationId,
					licenseId, clientType);
	}
}