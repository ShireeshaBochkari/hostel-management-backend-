package com.hostel.hostel_management.service;

import com.hostel.hostel_management.repository.MessageRepository;
import com.hostel.hostel_management.repository.UserRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MessageService}.
 */
@Generated
public class MessageService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'messageService'.
   */
  private static BeanInstanceSupplier<MessageService> getMessageServiceInstanceSupplier() {
    return BeanInstanceSupplier.<MessageService>forConstructor(MessageRepository.class, UserRepository.class)
            .withGenerator((registeredBean, args) -> new MessageService(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'messageService'.
   */
  public static BeanDefinition getMessageServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MessageService.class);
    beanDefinition.setInstanceSupplier(getMessageServiceInstanceSupplier());
    return beanDefinition;
  }
}
