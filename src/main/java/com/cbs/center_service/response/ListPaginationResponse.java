package com.cbs.center_service.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ListPaginationResponse extends PaginationResponse implements Serializable {
  private List<Map<String, Objects>> data;
}
