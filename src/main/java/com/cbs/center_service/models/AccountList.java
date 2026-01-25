package com.cbs.center_service.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class AccountList {
  private String acct;
  private String acctBranchCode;
  private String prodCode;
  private String acctType;
  private String localName;
  private String engName;
  private String ccy;
  private String acStatus;

}
