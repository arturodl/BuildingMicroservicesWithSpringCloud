package com.thoughtmechanix.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.thoughtmechanix.zuulsvr.config.ServiceConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class TrackingFilter extends ZuulFilter{
    private static final int      FILTER_ORDER =  1;
    private static final boolean  SHOULD_FILTER=true;
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    private ServiceConfig serviceConfig;

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private boolean isCorrelationIdPresent(){
      if (filterUtils.getCorrelationId() !=null){
          return true;
      }

      return false;
    }

    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }

    private String getOrganizationId(){

        String result="";
        if (filterUtils.getAuthToken()!=null){

            String authToken = filterUtils.getAuthToken().replace("Bearer ","");
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(serviceConfig.getJwtSigningKey().getBytes("UTF-8"))
                        .parseClaimsJws(authToken).getBody();
                result = (String) claims.get("organizationId");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object run() {
        System.out.println(">>>>>>>>>>>>>>>> Running TrakingFilter as Pre-Filter.");
        if (isCorrelationIdPresent()) {
           logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
           System.out.println(">>>>>>>>>>>>>>>>> There's already a correlation-id [Pre-Filter Message]");
        }
        else{
            filterUtils.setCorrelationId(generateCorrelationId());
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
            System.out.println(">>>>>>>>>>>>>>>> There's not any correlation-id, we'll generate one for you. [Pre-Filter Message]");
        }

        System.out.println(">>>>>>The organization id from the JWTToken is : " + getOrganizationId());
        filterUtils.setOrgId(getOrganizationId());

        RequestContext ctx = RequestContext.getCurrentContext();
        logger.debug("Processing incoming request for {}.",  ctx.getRequest().getRequestURI());

        System.out.println(">>>>>>>>>>>>>>>> Ending TrackingFilter as Pre-Filter for: "+ filterUtils.getCorrelationId());
        return null;
    }
}