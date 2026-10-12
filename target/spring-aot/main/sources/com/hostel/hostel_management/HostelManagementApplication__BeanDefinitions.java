package com.hostel.hostel_management;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link HostelManagementApplication}.
 */
@Generated
public class HostelManagementApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'hostelManagementApplication'.
   */
  public static BeanDefinition getHostelManagementApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(HostelManagementApplication.class);
    beanDefinition.setInstanceSupplier(HostelManagementApplication::new);
    return beanDefinition;
  }
}
