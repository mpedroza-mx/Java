package org.mrk.learning.rest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import org.mrk.learning.rest.dto.ContinentDto;
import org.mrk.learning.rest.dto.CountryDto;
import org.mrk.learning.rest.dto.StateDto;
import org.mrk.learning.rest.util.DtoWrapper;
import org.mrk.learning.rest.util.Node;

@Provider
public class FieldHidingFilter implements ContainerResponseFilter {

    @Context
    private UriInfo uriInfo;

    @Context
    private Providers providers;

    private static Map<Class,String> jsonFiltersMap = new HashMap<>();
    static{

        jsonFiltersMap.put(ContinentDto.class,"continent");
        jsonFiltersMap.put(CountryDto.class,"country");
        jsonFiltersMap.put(StateDto.class,"state");

    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        ContextResolver<ObjectMapper> resolver =
                providers.getContextResolver(ObjectMapper.class, MediaType.WILDCARD_TYPE);
        ObjectMapper mapper = resolver.getContext(ObjectMapper.class);

        List<String> queryParameters= Optional.ofNullable( uriInfo.getQueryParameters())
                .map(it->it.get("fields"))
                .map(items->Arrays.asList(items.get(0).split(",")))
                .orElse(new ArrayList<>());
        FilterProvider filters = null;
        DtoWrapper wrapper = (DtoWrapper)responseContext.getEntity();
        if (queryParameters.isEmpty()){
            filters = new SimpleFilterProvider().setFailOnUnknownId(false);
        }else {
            Node root = new Node("root");
            root.setField("root");

            Map<String, Node> nodesMap = new LinkedHashMap<>();
            nodesMap.put(root.getId(), root);

            queryParameters.forEach(userField -> {
                buildTreeByQueryStringField(userField, root, nodesMap);
            });

            Map<String, List<String>> filtersAndFieldsMap = new LinkedHashMap<>();
            Class clazz = Optional.ofNullable(wrapper.getClazzForFiltering()).map(it->it).orElse(wrapper.getEntity().getClass());
            filtersAndFieldsMap.put(jsonFiltersMap.get(clazz), new ArrayList<>());
            try {
                createFiltersMap(root.getChildren(), jsonFiltersMap, filtersAndFieldsMap, clazz);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            filters = populateFilterObject(filtersAndFieldsMap).setFailOnUnknownId(false);
        }
        String entity = mapper.writer(filters).writeValueAsString(wrapper.getEntity());
        responseContext.setEntity(entity);

    }


    SimpleFilterProvider populateFilterObject(Map<String,List<String>> filtersAndFieldsMap ){
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        filtersAndFieldsMap.entrySet()
                .forEach((key)->{
                    String fieldsArray = Arrays.toString(key.getValue().stream().toArray(String[]::new));
                    simpleFilterProvider.addFilter(key.getKey(), SimpleBeanPropertyFilter.filterOutAllExcept(key.getValue().stream().toArray(String[]::new)));
                });

        return simpleFilterProvider;
    }

    void buildTreeByQueryStringField(String currentField, Node parent, Map<String,Node> nodesMap){
        String[] composedFields = currentField.split("\\.");
        if (composedFields.length == 1){
            Node node = new Node(parent.getId()+"."+currentField);
            node.setField(composedFields[0]);
            parent.addChild(node);
            nodesMap.put(node.getId(),node);
        }else{
            Node node = nodesMap.get(parent.getId()+"."+composedFields[0]);
            if (nodesMap.get(parent.getId()+"."+composedFields[0])== null) {
                node = new Node(parent.getId() + "." + composedFields[0]);
                node.setField(composedFields[0]);
                parent.addChild(node);
                nodesMap.put(node.getId(), node);
            }
            List<String> composedFieldAsArrayList = new ArrayList(Arrays.asList(composedFields));
            composedFieldAsArrayList.removeIf(it->it.equals(composedFields[0]));
            String newCurrentField = String.join(".",composedFieldAsArrayList);

            buildTreeByQueryStringField(newCurrentField,node,nodesMap);
        }
    }

    void createFiltersMap(List<Node>children, Map<Class,String> jsonFiltersMap, Map<String,List<String>> filtersAndFieldsMap, Class rootClazz) throws NoSuchMethodException {
        for (Node child:children){
            if (child.getChildren() == null || child.getChildren().isEmpty()){
                if (jsonFiltersMap.get(rootClazz) != null) {
                    String filterKey = jsonFiltersMap.get(rootClazz);
                    if (filtersAndFieldsMap.get(filterKey) != null) {
                        filtersAndFieldsMap.get(filterKey).add(child.getField());
                    }
                }
            }else{
                if (jsonFiltersMap.get(rootClazz) != null){
                    String filterKey = jsonFiltersMap.get(rootClazz);
                    Class newClazz = getParametrizedTypeForField(rootClazz,child.getField());
                    if (filtersAndFieldsMap.get(filterKey) != null )
                    {
                        filtersAndFieldsMap.get(filterKey).add(child.getField());
                        List<String> startFields = new ArrayList<>();
                        filtersAndFieldsMap.put(jsonFiltersMap.get(newClazz),startFields);
                    }

                    createFiltersMap(child.getChildren(),jsonFiltersMap,filtersAndFieldsMap, newClazz);
                }
            }
        }
    }

    Class getParametrizedTypeForField(Class clazz, String fieldStr) throws NoSuchMethodException{
        fieldStr = fieldStr.substring(0,1).toUpperCase() + fieldStr.substring(1).toLowerCase();
        Method method = clazz.getMethod("get"+fieldStr, null);

        Type returnType = method.getGenericReturnType();

        if(returnType instanceof ParameterizedType){
            ParameterizedType type = (ParameterizedType) returnType;
            Type[] typeArguments = type.getActualTypeArguments();
            for(Type typeArgument : typeArguments){
                Class typeArgClass = (Class) typeArgument;
                return typeArgClass;
            }
        }
        return method.getReturnType();
    }
}
