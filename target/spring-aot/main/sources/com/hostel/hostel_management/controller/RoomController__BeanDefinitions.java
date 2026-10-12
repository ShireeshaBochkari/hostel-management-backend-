package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.service.RoomService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RoomController}.
 */
@Generated
public class RoomController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'roomController'.
   */
  private static BeanInstanceSupplier<RoomController> getRoomControllerInstanceSupplier() {
    return BeanInstanceSupplier.<RoomController>forConstructor(RoomService.class)
            .withGenerator((registeredBean, args) -> new RoomController(args.get(0)));
  }

  /**
   * Get the bean definition for 'roomController'.
   */
  public static BeanDefinition getRoomControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RoomController.class);
    beanDefinition.setInstanceSupplier(getRoomControllerInstanceSupplier());
    return beanDefinition;
  }
}
