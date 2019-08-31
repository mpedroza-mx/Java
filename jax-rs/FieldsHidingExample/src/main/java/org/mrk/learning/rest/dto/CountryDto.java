package org.mrk.learning.rest.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import java.util.List;

@JsonFilter(value="country")
public class CountryDto {

  private Long id;
  private String name;
  private String capital;
  private String currency;
  private List<String> languages;
  private List<StateDto> states;


  public Long getId() {
    return id;
  }

  public CountryDto setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public CountryDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getCapital() {
    return capital;
  }

  public CountryDto setCapital(String capital) {
    this.capital = capital;
    return this;
  }

  public String getCurrency() {
    return currency;
  }

  public CountryDto setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public List<String> getLanguages() {
    return languages;
  }

  public CountryDto setLanguages(List<String> languages) {
    this.languages = languages;
    return this;
  }

  public List<StateDto> getStates() {
    return states;
  }

  public CountryDto setStates(List<StateDto> states) {
    this.states = states;
    return this;
  }
}
