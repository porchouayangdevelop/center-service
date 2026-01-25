package com.cbs.center_service.models;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class CardInfo {
  private String ciNo;
  private String ciBranch;
  private String customerTitle;
  private String customerEngName;
  private String customerGender;
  private String customerIdType;
  private String dateBirth;
  private String customerNationalityCode;
  private String address1;
  private String address2;
  private String address3;
  private String address4;
  private String villageCode;
  private String villageName;
  private String cityCode;
  private String cityName;
  private String provinceCode;
  private String provinceName;
  private String telNo1;
  private String telNo2;
  private String email;
  private String faxNo;

  List<AccountList> accountList;
}
