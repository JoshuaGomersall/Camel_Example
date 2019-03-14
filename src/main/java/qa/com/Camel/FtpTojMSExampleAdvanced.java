package qa.com.Camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class FtpTojMSExampleAdvanced {

	public static void main(String args[]) throws Exception {
		test();
	}

	public static void test() throws Exception {
		while (true) {
			CamelContext context = new DefaultCamelContext();
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
			context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() {
					from("ftp://test.rebex.net?username=demo&password=password").process(new Processor() {

						public void process(Exchange exchange) throws Exception {
							System.out.println("WE JUST GOT " + exchange.getIn().getHeader("CamelFileName"));
						}

					}).to("activemq:queue:OtherPlayers");

					from("ftp://test.rebex.net/pub/example?username=demo&password=password").process(new Processor() {

						public void process(Exchange exchange) throws Exception {
							System.out.println("WE JUST GOT " + exchange.getIn().getHeader("CamelFileName"));
						}
					}).to("activemq:queue:HumanFighters");
				}
			});
			context.start();
			Thread.sleep(10000);
			context.stop();
		}
	}
}