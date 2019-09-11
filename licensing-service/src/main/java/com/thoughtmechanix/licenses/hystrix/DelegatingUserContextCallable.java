package com.thoughtmechanix.licenses.hystrix;

import com.thoughtmechanix.licenses.utils.UserContext;
import com.thoughtmechanix.licenses.utils.UserContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import java.util.concurrent.Callable;


public final class DelegatingUserContextCallable<V> implements Callable<V> {
    private final Callable<V> delegate;
    private UserContext originalUserContext;
    private OAuth2ClientContext originalOAuth2ClientContext;

    public DelegatingUserContextCallable(Callable<V> delegate,
                                             UserContext userContext) {
        this.delegate = delegate;
        this.originalUserContext = userContext;        
    }

    public V call() throws Exception {
        UserContextHolder.setContext( originalUserContext );

        try {
            return delegate.call();
        }
        finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate,
                                         UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}