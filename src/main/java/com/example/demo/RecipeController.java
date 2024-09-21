package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recipes/v1")
public class RecipeController {

    private RecipeClient client;


    public RecipeController(RecipeClient client){
        this.client = client;
    }

    @GetMapping("/random")
    public ResponseEntity<List<Recipe>> getRandomList(){
        List<Recipe> recipes = client.getRandomRecipes();
        if(recipes.size() != 5){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(client.getRandomRecipes());
    }

    @GetMapping("/query")
    public ResponseEntity<List<Recipe>> queryRecipes(@RequestParam String searchTerm,@RequestParam String diet, @RequestParam int size, @RequestParam Integer pageNumber){

        Params params = new Params(size,diet,searchTerm);
        List<Recipe> recipes = client.getRecipeByParams(params,pageNumber);
        System.out.println(params.number());
        System.out.println(recipes);
        return ResponseEntity.ok(recipes);
    }


}
