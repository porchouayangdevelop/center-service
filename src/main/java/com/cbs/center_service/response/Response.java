package com.cbs.center_service.response;


import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Response {
  private String code;
  private String message;
  private String status;
  private Date timestamp;
}
