package com.hostel.hostel_management.service;

import com.hostel.hostel_management.repository.RoomRepository;
import com.hostel.hostel_management.repository.UserRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RoomService}.
 */
@Generated
public class RoomService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'roomService'.
   */
  private static BeanInstanceSupplier<RoomService> getRoomServiceInstanceSupplier() {
    return BeanInstanceSupplier.<RoomService>forConstructor(RoomRepository.class, UserRepository.class)
            .withGenerator((registeredBean, args) -> new RoomService(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'roomService'.
   */
  public static BeanDefinition getRoomServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RoomService.class);
    beanDefinition.setInstanceSupplier(getRoomServiceInstanceSupplier());
    return beanDefinition;
  }
}
