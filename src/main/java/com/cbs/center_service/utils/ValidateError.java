package com.cbs.center_service.utils;

import org.springframework.stereotype.Component;

@Component
public class ValidateError {

  static String[] cardType ={"D","C"};
  static String[] lengthCusNo = {"12","16"};

  public static String validateCardType(String type){
    for (String s : cardType) {
      if (s.equals(type)) {
        return null;
      }

    }
    return "Invalid Card Type";
  }

  public static String validateLengthCusNo(String length){
    for (String s : lengthCusNo) {
      if (s.equals(length)) {
        return null;
      }
    }
    return "Invalid Length CIF";
  }


}
