package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.repository.UserRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link HostellerController}.
 */
@Generated
public class HostellerController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'hostellerController'.
   */
  private static BeanInstanceSupplier<HostellerController> getHostellerControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<HostellerController>forConstructor(UserRepository.class)
            .withGenerator((registeredBean, args) -> new HostellerController(args.get(0)));
  }

  /**
   * Get the bean definition for 'hostellerController'.
   */
  public static BeanDefinition getHostellerControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(HostellerController.class);
    beanDefinition.setInstanceSupplier(getHostellerControllerInstanceSupplier());
    return beanDefinition;
  }
}
