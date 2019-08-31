package org.mrk.learning.rest.resources;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mrk.learning.rest.dto.ContinentDto;
import org.mrk.learning.rest.dto.CountryDto;
import org.mrk.learning.rest.dto.StateDto;
import org.mrk.learning.rest.service.ContinentService;
import org.mrk.learning.rest.util.DtoWrapper;

@RunWith(MockitoJUnitRunner.class)
public class ContinentResourceTest {

    @Mock
    UriInfo uriInfo;

    @InjectMocks
    ContinentResource resource;

    @Mock
    ContinentService continentService;

    private Collection<ContinentDto> continents;

    private ContinentDto continent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        buildContinentsEntity();
    }

    @Test
    public void getContinents(){
        UriBuilder builder = UriBuilder.fromUri("http://localhost:8080");

        Mockito.when(continentService.findAllContinents()).thenReturn(continents);
        Mockito.when(uriInfo.getBaseUriBuilder()).thenReturn(builder);


        Response response = resource.getContinents();
        DtoWrapper<Collection<ContinentDto>> wrapper = (DtoWrapper<Collection<ContinentDto>>)response.getEntity();
        assert wrapper.getEntity().size() ==2;
    }

    @Test
    public void getContinent(){
        UriBuilder builder = UriBuilder.fromUri("http://localhost:8080");

        Mockito.when(continentService.findContinentById(1L)).thenReturn(continent);
        Mockito.when(uriInfo.getBaseUriBuilder()).thenReturn(builder);

        Response response = resource.getContinent(1L);
        DtoWrapper<ContinentDto> wrapper = (DtoWrapper<ContinentDto>)response.getEntity();
        assert  null != wrapper.getEntity();
    }

    private void buildContinentsEntity(){
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

        continents = continentsDto;
        continent = america;

    }

}
