package com.cbs.center_service.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ListResponse extends Response implements Serializable {
  List<Map<String, Object>> data;
}
