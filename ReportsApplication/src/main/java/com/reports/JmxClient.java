package com.reports;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxClient {
	public static void main(String[] args) throws MalformedURLException {
		
		// 1. Define the JMX service URL (adjust host and port)
		String url = "service:jmx:rmi:///jndi/rmi://localhost:9010/jmxrmi";
		JMXServiceURL serviceUrl = new JMXServiceURL(url);
	
		// 2. Establish a connection
		try (JMXConnector connector = JMXConnectorFactory.connect(serviceUrl)) {
			MBeanServerConnection mbsc = connector.getMBeanServerConnection();

			getTestMbean(mbsc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getTestMbean(MBeanServerConnection mbsc) throws Exception {
		// 3. Define the ObjectName of the MBean you want to access
		ObjectName mbeanName = new ObjectName("batch:name=TestMbean");

		// 4. Get an attribute value
		Object status = mbsc.getAttribute(mbeanName, "Status");
		System.out.println("Application Health: " + status);
				
		Object[] params = new Object[] { "test", 12345L};
		String[] signature = new String[] { String.class.getName() , Long.class.getName()}; // Resolves to "java.lang.String"

		Object result = mbsc.invoke(mbeanName, "testMethod", params, signature);
		System.out.println("Job started with execution id: " + result);
	}

	
}
