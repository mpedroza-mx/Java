package org.mrk.learning.rest.resources;


import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.mrk.learning.rest.dto.ContinentDto;
import org.mrk.learning.rest.service.ContinentService;
import org.mrk.learning.rest.util.DtoWrapper;

@Path("/continents")
@Consumes({"application/json"})
@Produces({"application/json"})
@Stateless
public class ContinentResource {

  @Context
  UriInfo uriInfo;

  @EJB
  private ContinentService continentService;


  @GET
  @Path("/")
  public Response getContinents() {
    Collection<ContinentDto> continentsDto = continentService.findAllContinents();
    DtoWrapper<Collection<ContinentDto>> complexEntity = new DtoWrapper.Builder<Collection<ContinentDto>>(
        continentsDto)
        .setClazzForFiltering(ContinentDto.class)
        .build();

    return Response.ok().entity(complexEntity).build();
  }


  @GET
  @Path("/{continentId}")
  public Response getContinent(@PathParam("reservationId") Long reservationId) {
    ContinentDto continentDto = continentService.findContinentById(reservationId);

    DtoWrapper<ContinentDto> complexEntity = new DtoWrapper.Builder<ContinentDto>(
        continentDto)
        .build();
    return Response.ok().entity(complexEntity).build();
  }

}
