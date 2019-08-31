package org.mrk.learning.rest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import org.jboss.resteasy.core.interception.ContainerResponseContextImpl;
import org.jboss.resteasy.specimpl.BuiltResponse;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
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
import org.mrk.learning.rest.util.DtoWrapper;

@RunWith(MockitoJUnitRunner.class)
public class FieldHidingFilterTest {


    @Mock
    private UriInfo uriInfo;

    @Mock
    private Providers providers;

    @InjectMocks
    private FieldHidingFilter filter;

    private ObjectMapper objectMapper;

    private DtoWrapper<Collection<ContinentDto>> continents;

    private DtoWrapper<ContinentDto> continent;


    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        Collection<ContinentDto> continentsDto = buildContinentsEntity();
        continents = new DtoWrapper.Builder<Collection<ContinentDto>>(continentsDto)
            .setClazzForFiltering(ContinentDto.class).build();

        continent = new DtoWrapper.Builder<ContinentDto>(continentsDto.stream().findFirst().get()).build();

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testSimpleContinentFields() throws IOException, URISyntaxException {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("fields", "name,id,area");
        executeTest(queryParams, continent,"TestContinentSimpleField.json");
    }

    @Test
    public void testContinentsSimpleField() throws IOException,URISyntaxException {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("fields", "name");
        executeTest(queryParams, continents, "TestContinentsSimpleField.json");
    }

    @Test
    public void testContinentsCountries() throws IOException, URISyntaxException {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("fields", "id,name,countries");
        executeTest(queryParams, continents, "TestContinentsCountries.json");
    }

    @Test
    public void testContinentsCountriesSimpleField() throws IOException, URISyntaxException {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("fields", "id,name,countries.name");
        executeTest(queryParams, continents,
            "TestContinentsCountriesSimpleField.json");
    }

    @Test
    public void testContinentsCountriesListField() throws IOException, URISyntaxException {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("fields", "id,name,countries.name,countries.languages");
        executeTest(queryParams, continents, "TestContinentsCountriesListField.json");
    }

    @Test
    public void testContinentsCountriesStates() throws IOException, URISyntaxException {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("fields", "id,name,countries.name,countries.states");
        executeTest(queryParams, continents, "TestContinentsCountriesStates.json");
    }


    @Test
    public void testContinentsNoFiltersProvided() throws IOException, URISyntaxException {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        executeTest(queryParams, continents, "TestContinentsNoFiltersProvided.json");
    }


    private void executeTest(MultivaluedMap<String, String> queryParams, Object complexEntity, String jsonFile) throws IOException, URISyntaxException{
        ContextResolver<ObjectMapper> resolver = Mockito.mock(ContextResolver.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpResponse response = Mockito.mock(HttpResponse.class);
        BuiltResponse serverResponse = new BuiltResponse();
        ContainerResponseContext responseContext = new ContainerResponseContextImpl(request,response,serverResponse);
        responseContext.setEntity(complexEntity);
        Mockito.when(providers.getContextResolver(ObjectMapper.class, MediaType.WILDCARD_TYPE)).thenReturn(resolver);
        Mockito.when(resolver.getContext(ObjectMapper.class)).thenReturn(objectMapper);
        Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        filter.filter(Mockito.mock(ContainerRequestContext.class),responseContext);

        Path path = Paths.get(FieldHidingFilterTest.class.getClassLoader().getResource(
            jsonFile).toURI());
        String content = new String(Files.readAllBytes(path));
        assert content.equals(responseContext.getEntity().toString());
    }


    private Collection<ContinentDto> buildContinentsEntity(){
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
}
