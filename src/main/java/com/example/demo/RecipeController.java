package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/query/{pageNumber}")
    public ResponseEntity<List<Recipe>> queryRecipes(@RequestBody Params params, @PathVariable Integer pageNumber){
        List<Recipe> recipes = client.getRecipeByParams(params,pageNumber);
        return ResponseEntity.ok(recipes);
    }
}
