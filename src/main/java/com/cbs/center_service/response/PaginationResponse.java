package com.cbs.center_service.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PaginationResponse extends Response implements Serializable {
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
}
