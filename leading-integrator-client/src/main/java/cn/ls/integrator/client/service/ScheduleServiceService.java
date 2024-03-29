
package cn.ls.integrator.client.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ScheduleServiceService", targetNamespace = "http://service.server.integrator.ls.cn/", wsdlLocation = "http://192.168.1.128:11818/wsc/ScheduleService?wsdl")
public class ScheduleServiceService
    extends Service
{

    private final static URL SCHEDULESERVICESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(cn.ls.integrator.client.service.ScheduleServiceService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = cn.ls.integrator.client.service.ScheduleServiceService.class.getResource(".");
            url = new URL(baseUrl, "http://192.168.1.128:11818/wsc/ScheduleService?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://192.168.1.128:11818/wsc/ScheduleService?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        SCHEDULESERVICESERVICE_WSDL_LOCATION = url;
    }

    public ScheduleServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ScheduleServiceService() {
        super(SCHEDULESERVICESERVICE_WSDL_LOCATION, new QName("http://service.server.integrator.ls.cn/", "ScheduleServiceService"));
    }

    /**
     * 
     * @return
     *     returns ScheduleService
     */
    @WebEndpoint(name = "ScheduleServicePort")
    public ScheduleService getScheduleServicePort() {
        return super.getPort(new QName("http://service.server.integrator.ls.cn/", "ScheduleServicePort"), ScheduleService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ScheduleService
     */
    @WebEndpoint(name = "ScheduleServicePort")
    public ScheduleService getScheduleServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.server.integrator.ls.cn/", "ScheduleServicePort"), ScheduleService.class, features);
    }

}
