package com.prescription.ui.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
@Component
public class CustomerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Customer filter - do filter");
    }

    @Override
    public void destroy() {
        log.info("Customer filter - destroy");
        Filter.super.destroy();
    }
}
