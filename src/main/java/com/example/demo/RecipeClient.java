package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class RecipeClient{

    private final ObjectMapper mapper;
    private final RestClient restClient;

    private final HttpHeaders headers;


    public RecipeClient(RestClient.Builder restClient,ObjectMapper mapper){
        headers = new HttpHeaders();
        headers.set("X-api-key","b1ad3c72c9cf4c1ba07b8338cba32434");
        this.mapper = mapper;
        var jdkFactory = new JdkClientHttpRequestFactory();
        this.restClient = restClient.baseUrl("https://api.spoonacular.com/")
                .requestFactory(jdkFactory)
                .build();
    }

    public List<Recipe> getRecipeByParams(Params params,int page){
        String apiEndpoint;
        if(params.diet().isEmpty()){
            apiEndpoint = "/recipes/complexSearch?number"+params.number()+"&query="+params.title()+"&offset="+(-10+(10*page));
        }else{
            apiEndpoint = "/recipes/complexSearch?number"+params.number()+"&diet="+params.diet()+"&query="+params.title()+"&offset="+(-10+(10*page));
        }
        String result;
        try{
            result = restClient.get()
                    .uri(apiEndpoint)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(new ParameterizedTypeReference<String>(){});
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        JsonNode node;
        try{
            node = mapper.readTree(result);
        }catch(JsonProcessingException e){
            e.printStackTrace();
            return null;
        }

        if(node.path("results").isArray()){
            return mapper.convertValue(node.path("results"), new TypeReference<List<Recipe>>() {});
        }

        return null;

    }

    public Recipe getRecipeById(Long id){
        System.out.println(id);
        String apiEndpoint = "/recipes/"+id+"/information";
        String result;
        try{
            result = restClient.get()
                    .uri(apiEndpoint)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(new ParameterizedTypeReference<String>() {});
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        JsonNode node;
        try{
            node = mapper.readTree(result);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }

        Long idNumber = node.path("id").asLong();
        String title = node.path("title").asText();
        String imgURL = node.path("image").asText();
        if(title.isEmpty() || imgURL.isEmpty() || idNumber.toString().isEmpty()){
            return null;
        }
        return new Recipe(idNumber,title,imgURL);


    }

    public List<Recipe> getRandomRecipes(){

        String apiEndpoint = "/recipes/random?number=5";
        String result;
        try{
            result = restClient.get()
                    .uri(apiEndpoint)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(new ParameterizedTypeReference<String>() {});
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        JsonNode node;
        try{
            node = mapper.readTree(result);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }


        if(node.path("recipes").isArray()){
            List<Recipe> recipes = mapper.convertValue(node.path("recipes"), new TypeReference<List<Recipe>>() {});
            return recipes;
        }


        return null;


    }



    /*
    public void getVegetarianPasta(){
        String test = "";
        Params params = new Params(10,"vegetarian","pasta");
        String apiEndpoint = "/recipes/complexSearch?diet="+params.diet()+"&query="+params.title();
        try{
            test = restClient.get()
                    .uri(apiEndpoint)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(new ParameterizedTypeReference<String>(){});

        }catch (Exception e){
            e.printStackTrace();
        }

            System.out.println(test);

    }

    public String test() {
        String apiEndpoint = "/recipes/random?number=1"; // Example endpoint to get a random recipe
        String test;
        try {
             test = restClient.get()
                    .uri(apiEndpoint)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

        System.out.println(test);
        JsonNode node;
        try {
            node = mapper.readTree(test);
        }catch(JsonProcessingException error){
            error.printStackTrace();
            return "";
        }

        JsonNode recipe = node.path("recipes").get(0);
        System.out.println(recipe.path("title").asText());
        return test;
    }
    */



}
