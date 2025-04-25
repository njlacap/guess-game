package njslcp.project.guess_game.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import java.io.IOException;

//@Log4j2
public class RequestIDFilter implements Filter {

    private static final String REQ_ID = "requestId";
    private static final String HEADER_KEY = "X-Request-ID";

    public RequestIDFilter() { }

    @Override
    public void doFilter(ServletRequest servletRequest
                        ,ServletResponse servletResponse
                        ,FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestId = httpServletRequest.getHeader(HEADER_KEY);

//        log.info("***** Request ID {}",requestId);
        try {
            MDC.put(REQ_ID,requestId);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }

    }

}
