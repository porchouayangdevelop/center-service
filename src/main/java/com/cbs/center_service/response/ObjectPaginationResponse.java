package com.cbs.center_service.response;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ObjectPaginationResponse extends PaginationResponse implements Serializable {
  private Map<String, Object> data;

}
