package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.repository.UserRepository;
import com.hostel.hostel_management.service.MessageService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MessageController}.
 */
@Generated
public class MessageController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'messageController'.
   */
  private static BeanInstanceSupplier<MessageController> getMessageControllerInstanceSupplier() {
    return BeanInstanceSupplier.<MessageController>forConstructor(MessageService.class, UserRepository.class)
            .withGenerator((registeredBean, args) -> new MessageController(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'messageController'.
   */
  public static BeanDefinition getMessageControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MessageController.class);
    beanDefinition.setInstanceSupplier(getMessageControllerInstanceSupplier());
    return beanDefinition;
  }
}
