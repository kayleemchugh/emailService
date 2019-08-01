package com.armyPOC.emailService;

import javax.jms.ConnectionFactory;

import com.armyPOC.emailService.service.WebsocketListenerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class EmailServiceApplication {

	@Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }


	public static void main(String[] args) {

		//String token = createJWT("MikeA", null, null, null, 3600000);
		ConfigurableApplicationContext context = SpringApplication.run(EmailServiceApplication.class, args);

		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

		WebsocketListenerService websocketListenerService = new WebsocketListenerService(jmsTemplate);

		websocketListenerService.startClient();
	}

	

}
