package com.cbs.center_service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LoadEnvironment {

  @Autowired
  private Environment environment;

  public String getStrEnvironment(String key) {
    return environment.getProperty(key);
  }

  public Integer getIntEnvironment(String key) {
    return Integer.parseInt(environment.getProperty(key));
  }

  public boolean getBoolEnvironment(String key) {
    return Boolean.parseBoolean(environment.getProperty(key));
  }
}
