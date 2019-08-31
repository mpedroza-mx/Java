package org.mrk.learning.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import org.mrk.learning.rest.dto.ContinentDto;
import org.mrk.learning.rest.dto.CountryDto;
import org.mrk.learning.rest.dto.StateDto;

@Stateless
public class ContinentService {

  public Collection<ContinentDto> findAllContinents(){

    List<String> languages = new ArrayList<>();
    languages.add("Spanish");
    languages.add("English");
    languages.add("Portuguese");
    languages.add("French");
    languages.add("Italian");


    List<StateDto> statesDto = new ArrayList<>();
    StateDto jalisco = new StateDto()
        .setId(1L)
        .setName("Jalisco")
        .setDemonym("Jalisciense")
        .setDensity("110/km");

    StateDto yucatan = new StateDto()
        .setId(2L)
        .setName("Yucatan")
        .setDemonym("Yucateco")
        .setDensity("53/km");

    statesDto.add(jalisco);
    statesDto.add(yucatan);

    List<CountryDto> countriesDto = new ArrayList<>();

    CountryDto mexico = new CountryDto()
        .setId(1L)
        .setName("Mexico")
        .setCapital("Mexico City")
        .setCurrency("Mexican Peso")
        .setLanguages(languages.stream()
            .filter(it->it.equalsIgnoreCase("Spanish"))
            .collect(Collectors.toList())
        )
        .setStates(statesDto);

    CountryDto usa = new CountryDto()
        .setId(2L)
        .setName("United States of America")
        .setCapital("Washington DC")
        .setCurrency("American Dollar")
        .setLanguages((languages.stream()
            .filter(it->it.equalsIgnoreCase("English"))
            .collect(Collectors.toList())
        ))
        .setStates(null);

    CountryDto canada = new CountryDto()
        .setId(3L)
        .setName("Canada")
        .setCapital("Ottawa")
        .setCurrency("Canadian Dollar")
        .setLanguages((languages.stream()
            .filter(it->it.equalsIgnoreCase("French"))
            .collect(Collectors.toList())
        ))
        .setStates(null);

    countriesDto.add(mexico);
    countriesDto.add(usa);
    countriesDto.add(canada);


    List<CountryDto> countriesEuropeDto = new ArrayList<>();

    CountryDto spain = new CountryDto()
        .setId(4L)
        .setName("Spain")
        .setCapital("Madrid")
        .setCurrency("Euro")
        .setLanguages(languages.stream()
            .filter(it->it.equalsIgnoreCase("Spanish"))
            .collect(Collectors.toList())
        )
        .setStates(null);

    CountryDto italy = new CountryDto()
        .setId(4L)
        .setName("Italy")
        .setCapital("Rome")
        .setCurrency("Euro")
        .setLanguages(languages.stream()
            .filter(it->it.equalsIgnoreCase("Italian"))
            .collect(Collectors.toList())
        )
        .setStates(null);


    countriesEuropeDto.add(spain);
    countriesEuropeDto.add(italy);

    Collection<ContinentDto> continentsDto = new ArrayList<>();

    ContinentDto america = new ContinentDto()
        .setId(1L)
        .setArea(42000000)
        .setName("America")
        .setLanguages(languages)
        .setCountries(countriesDto);

    ContinentDto europe = new ContinentDto()
        .setId(2L)
        .setArea(12000000)
        .setName("Europe")
        .setLanguages(languages)
        .setCountries(countriesEuropeDto);

    continentsDto.add(america);
    continentsDto.add(europe);

    return continentsDto;
  }

  public ContinentDto findContinentById(Long id){
    List<String> languages = new ArrayList<>();
    languages.add("Spanish");
    languages.add("English");
    languages.add("Portuguese");
    languages.add("French");


    List<StateDto> statesDto = new ArrayList<>();
    StateDto jalisco = new StateDto()
        .setId(1L)
        .setName("Jalisco")
        .setDemonym("Jalisciense")
        .setDensity("110/km");

    StateDto yucatan = new StateDto()
        .setId(2L)
        .setName("Yucatan")
        .setDemonym("Yucateco")
        .setDensity("53/km");

    statesDto.add(jalisco);
    statesDto.add(yucatan);

    List<CountryDto> countriesDto = new ArrayList<>();

    CountryDto mexico = new CountryDto()
        .setId(1L)
        .setName("Mexico")
        .setCapital("Mexico City")
        .setCurrency("Mexican Peso")
        .setLanguages(languages.stream()
            .filter(it->it.equalsIgnoreCase("Spanish"))
            .collect(Collectors.toList())
        )
        .setStates(statesDto);

    CountryDto usa = new CountryDto()
        .setId(2L)
        .setName("United States of America")
        .setCapital("Washington DC")
        .setCurrency("American Dollar")
        .setLanguages((languages.stream()
            .filter(it->it.equalsIgnoreCase("English"))
            .collect(Collectors.toList())
        ))
        .setStates(null);

    CountryDto canada = new CountryDto()
        .setId(3L)
        .setName("Canada")
        .setCapital("Ottawa")
        .setCurrency("Canadian Dollar")
        .setLanguages((languages.stream()
            .filter(it->it.equalsIgnoreCase("French"))
            .collect(Collectors.toList())
        ))
        .setStates(null);

    countriesDto.add(mexico);
    countriesDto.add(usa);
    countriesDto.add(canada);

    Collection<ContinentDto> continentsDto = new ArrayList<>();

    ContinentDto america = new ContinentDto()
        .setId(1L)
        .setArea(42000000)
        .setName("America")
        .setLanguages(languages)
        .setCountries(countriesDto);


    return america;
  }
}
