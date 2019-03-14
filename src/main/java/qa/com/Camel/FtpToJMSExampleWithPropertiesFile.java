package qa.com.Camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class FtpToJMSExampleWithPropertiesFile {

	public static void main(String args[]) throws Exception {
		while (true) {
			CamelContext context = new DefaultCamelContext();
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

			PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
			prop.setLocation("classpath:system-properties.properties");

			context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() {
					from("ftp://test.rebex.net?username=demo&password=password").to("activemq:queue:${{myDest}}");
				}
			});
			context.start();
			Thread.sleep(10000);
			context.stop();

		}
	}
}