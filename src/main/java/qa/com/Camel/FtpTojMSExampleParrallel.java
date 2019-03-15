package qa.com.Camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class FtpTojMSExampleParrallel {

	public static void main(String args[]) throws Exception {
		test1();
	}

	public static void test1() throws Exception {
		while (true) {
			CamelContext context = new DefaultCamelContext();
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
			context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
			context.addRoutes(new RouteBuilder() {

				@Override
				public void configure() {
					from("ftp://test.rebex.net?username=demo&password=password").wireTap("activemq:queue:TxtWiredTapped").multicast().parallelProcessing()
							.stopOnException().to("activemq:queue:Txt Files", "activemq:queue:Txt Orders");
				}
			});
			context.start();
			Thread.sleep(10000);
			context.stop();
		}
	}
}