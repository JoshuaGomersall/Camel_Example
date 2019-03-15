package qa.com.Camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class FtpTojMSExampleSorting {

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
					from("ftp://test.rebex.net?username=demo&password=password").choice()
							.when(header("CamelFileName").endsWith(".xml")).to("activemq:queue:XML")
							.when(header("CamelFileName").endsWith(".csv")).to("activemq:queue:CSV")
							.when(header("CamelFileName").endsWith(".txt")).to("activemq:queue:TXT")
							.when(header("CamelFileName").endsWith(".png")).to("activemq:queue:PNG").otherwise()
							.to("activemq:queue:Other").end().to("activemq:queue:Total");

					from("ftp://test.rebex.net/pub/example?username=demo&password=password").choice()
							.when(header("CamelFileName").endsWith(".xml")).to("activemq:queue:XML")
							.when(header("CamelFileName").endsWith(".csv")).to("activemq:queue:CSV")
							.when(header("CamelFileName").endsWith(".txt")).to("activemq:queue:TXT")
							.when(header("CamelFileName").endsWith(".png")).to("activemq:queue:PNG").otherwise()
							.to("activemq:queue:Other").end().to("activemq:queue:Total");
				}
			});
			context.start();
			Thread.sleep(10000);
			context.stop();
		}
	}
}