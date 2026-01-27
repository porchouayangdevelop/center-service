package com.cbs.center_service.services.impl;

import com.cbs.center_service.interfaces.CimcusmInterface;
import com.cbs.center_service.models.CardInfo;
import com.cbs.center_service.utils.ValidateError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CimcusmServiceImpl implements CimcusmInterface {
  /**
   * @param cif
   * @param cardType
   * @return
   */
  @Override
  public CardInfo getCimcusmByCif(String cif, String cardType) {
    ValidateError.validateLengthCusNo(cif);
    ValidateError.validateCardType(cardType);
    return null;
  }
}
