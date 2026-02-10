package com.soa.lab4.service1.proxy.client;

import jakarta.xml.ws.Service;
import java.net.URL;
import javax.xml.namespace.QName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Service1SoapClient {
    private final Service1SoapPort port;

    public Service1SoapClient(
            @Value("${soap.wsdl-url}") String wsdlUrl,
            @Value("${soap.namespace}") String namespace,
            @Value("${soap.service-name}") String serviceName
    ) {
        try {
            URL url = new URL(wsdlUrl);
            QName serviceQName = new QName(namespace, serviceName);
            QName portQName = new QName(namespace, "Service1SoapServicePort");
            Service service = Service.create(url, serviceQName);
            this.port = service.getPort(portQName, Service1SoapPort.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create SOAP client: " + ex.getMessage(), ex);
        }
    }

    public Service1SoapPort getPort() {
        return port;
    }
}
