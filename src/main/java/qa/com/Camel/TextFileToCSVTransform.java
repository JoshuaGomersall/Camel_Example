package qa.com.Camel;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class TextFileToCSVTransform {

	public static void main(String args[]) throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			public void configure() {
//				from("file:inbox?noop=true").transform(body().regexReplaceAll(" ", "<br/>"))
//						.to("file:outbox?fileName=example.csv");

				from("file:inbox?noop=true").transform(new Expression() {

					public <T> T evaluate(Exchange exchange, Class<T> type) {
						String body = exchange.getIn().getBody(String.class);
						body = body.replaceAll(" ", "<br/>");
						body = "<body>" + body + "</body>";
						return (T) body;
					}
				}).to("file:outbox?fileName=example.csv");

			}
		});
		context.start();
		Thread.sleep(10000); // need sleep to keep JVM running until the job is done
		context.stop();
	}

}
