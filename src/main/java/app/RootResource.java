package app;

import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class RootResource {
    private final Protocol protocol = Protocol.CAS3;
    private final AppConf configuration;
    private final TicketValidator ticketValidator;

    public RootResource(AppConf configuration) {
        this.configuration = configuration;
        this.ticketValidator = new Cas30ServiceTicketValidator(configuration.getCasServerUrlPrefix());
    }

    @GET
    public String mainPage(@Context HttpServletRequest request) throws Exception {
        StringBuilder responseData = new StringBuilder();
        String ticket = CommonUtils.safeGetParameter(request, protocol.getArtifactParameterName());
        if (CommonUtils.isNotEmpty(ticket)) {
            responseData.append("Service ticket present<br/>");
            try {
                Assertion assertion = ticketValidator.validate(ticket, configuration.getServiceUrl());
                if (assertion.isValid()) {
                    responseData.append("Service ticket valid<br/>");
                    responseData.append("<h1>Attributes</h1>");
                    responseData.append("<ul>");
                    responseData.append(assertion.getPrincipal().getAttributes().entrySet()
                            .stream().map(e ->
                                    "<li>" + e.getKey() + "=" + e.getValue() + "</li>"
                            ).collect(Collectors.joining()));
                    responseData.append("</ul>");
                    responseData.append("<p>This is when you would create a session based on the attributes.</p>");
                } else {
                    responseData.append("Service ticket invalid<br/>");
                }
            } catch (TicketValidationException ex) {
                responseData.append("Service ticket unknown<br/>");
            }
        } else {
            responseData.append("No service ticket present<br/>");
        }
        String loginUrl = CommonUtils.constructRedirectUrl(configuration.getCasServerLoginUrl(), protocol.getServiceParameterName(), configuration.getServiceUrl(), false, false);
        String logoutUrl = CommonUtils.constructRedirectUrl(configuration.getCasServerLogoutUrl(), protocol.getServiceParameterName(), configuration.getServiceUrl(), false, false);
        responseData.append("[ <a href=\"").append(loginUrl).append("\">login</a> ] ");
        responseData.append("[ <a href=\"").append(logoutUrl).append("\">logout</a> ] ");
        return responseData.toString();
    }
}
