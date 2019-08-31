package org.mrk.learning.rest.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter(value="state")
public class StateDto {
  private Long id;
  private String name;
  private String density;
  private String demonym;

  public Long getId() {
    return id;
  }

  public StateDto setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public StateDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getDensity() {
    return density;
  }

  public StateDto setDensity(String density) {
    this.density = density;
    return this;
  }

  public String getDemonym() {
    return demonym;
  }

  public StateDto setDemonym(String demonym) {
    this.demonym = demonym;
    return this;
  }
}
