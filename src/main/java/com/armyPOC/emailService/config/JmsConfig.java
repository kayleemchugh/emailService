package com.armyPOC.emailService.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@EnableJms
public class JmsConfig
{

   private static final String JMS_BROKER_URL = "vm://embedded?broker.persistent=false,useShutdownHook=false";
   public static final String JMS_TOPIC_MAIL = "mailbox.topic";

   @Bean
   public ActiveMQConnectionFactory amqConnectionFactory() {

      return new ActiveMQConnectionFactory(JMS_BROKER_URL);

   }

   @Bean
   public CachingConnectionFactory connectionFactory() {

      return new CachingConnectionFactory(amqConnectionFactory());

   }


   @Bean
   public JmsTemplate jmsTemplate() {

      JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
      jmsTemplate.setDefaultDestinationName(JMS_TOPIC_MAIL);
      jmsTemplate.setConnectionFactory(connectionFactory());
      jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
      return jmsTemplate;
   }

   @Bean
   public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
         ConnectionFactory connectionFactory,
         DefaultJmsListenerContainerFactoryConfigurer configurer) {
      DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
      factory.setPubSubDomain(true);
      factory.setConnectionFactory(connectionFactory());
      // This provides all boot's default to this factory, including the
      // message converter
      configurer.configure(factory, connectionFactory());
      // You could still override some of Boot's default if necessary.
      return factory;
   }

   @Bean // Serialize message content to json using TextMessage
   public MessageConverter jacksonJmsMessageConverter() {
      MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
      converter.setTargetType(MessageType.TEXT);
      converter.setTypeIdPropertyName("_type");
      return converter;
   }
}
