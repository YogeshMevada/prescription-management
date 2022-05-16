package com.prescription.ui.filter;

import com.google.common.base.Joiner;
import com.prescription.ui.util.UrlHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class CustomerFilter implements Filter {

    public static final String BASE64_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChOv6xMUV/KK10E/oOQsieGNnm/ImYt3PBL7Pmp8z4QI/iOLLWAVqUPq46Xg0W5xsK/xkedWWzbhBcfIIeMOe3NJ239YkYF7DXE31F3+vS8u5tX2BD8/ofj4nr9TleJE3X9kX2mHrxHc3kxPX3GKGDRi4glOR1hHR+pyh3SNiGPwIDAQAB";
    private final UrlHelper urlHelper;

    @Autowired
    public CustomerFilter(final UrlHelper urlHelper) {
        this.urlHelper = urlHelper;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        log.info("Customer filter - do filter");
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        updateMandatoryData(httpServletRequest.getSession());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void updateMandatoryData(final HttpSession session) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roles = "";
        if (Objects.nonNull(authentication)) {
            roles = Joiner.on(",").join(authentication.getAuthorities());
        }
        session.setAttribute("roles", roles);
        session.setAttribute("authUrl", urlHelper.AUTH_URL);
        session.setAttribute("publicEncKey", BASE64_PUBLIC_KEY);
    }

    @Override
    public void destroy() {
        log.info("Customer filter - destroy");
        Filter.super.destroy();
    }
}
