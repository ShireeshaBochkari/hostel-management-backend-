package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.repository.UserRepository;
import com.hostel.hostel_management.security.JwtService;
import com.hostel.hostel_management.service.UserService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Bean definitions for {@link AuthController}.
 */
@Generated
public class AuthController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'authController'.
   */
  private static BeanInstanceSupplier<AuthController> getAuthControllerInstanceSupplier() {
    return BeanInstanceSupplier.<AuthController>forConstructor(UserService.class, UserRepository.class, AuthenticationManager.class, JwtService.class)
            .withGenerator((registeredBean, args) -> new AuthController(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'authController'.
   */
  public static BeanDefinition getAuthControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AuthController.class);
    beanDefinition.setInstanceSupplier(getAuthControllerInstanceSupplier());
    return beanDefinition;
  }
}
