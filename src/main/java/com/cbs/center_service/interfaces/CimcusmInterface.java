package com.cbs.center_service.interfaces;

import com.cbs.center_service.models.CardInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface CimcusmInterface {
  CardInfo getCimcusmByCif(String cif, String cardType);
}
