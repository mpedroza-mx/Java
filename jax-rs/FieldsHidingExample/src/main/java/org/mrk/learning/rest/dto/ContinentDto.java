package org.mrk.learning.rest.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import java.util.List;

@JsonFilter(value="continent")
public class ContinentDto {

  private Long id;
  private String name;
  private Integer population;
  private Integer area;
  private List<String> languages;
  private List<CountryDto> countries;

  public Long getId() {
    return id;
  }

  public ContinentDto setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public ContinentDto setName(String name) {
    this.name = name;
    return this;
  }

  public Integer getPopulation() {
    return population;
  }

  public ContinentDto setPopulation(Integer population) {
    this.population = population;
    return this;
  }

  public Integer getArea() {
    return area;
  }

  public ContinentDto setArea(Integer area) {
    this.area = area;
    return this;
  }

  public List<String> getLanguages() {
    return languages;
  }

  public ContinentDto setLanguages(List<String> languages) {
    this.languages = languages;
    return this;
  }

  public List<CountryDto> getCountries() {
    return countries;
  }

  public ContinentDto setCountries(List<CountryDto> countries) {
    this.countries = countries;
    return this;
  }
}
