package com.example.demo;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/v1")
public class UserController {

    UserService service;

    RecipeClient recipeClient;

    public UserController(UserService service,RecipeClient recipeClient){
        this.recipeClient = recipeClient;
        this.service = service;
    }

    @PostMapping("/Register")
    public ResponseEntity<Long> register(@RequestBody LoginForm incomingForm) {
        System.out.println(incomingForm.getUsername());
        User user = service.findUserByUsername(incomingForm.getUsername());
        if (user != null) {
            return ResponseEntity.status(409).build();
        }
        user = service.createUser(incomingForm.getUsername(), incomingForm.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user.getId());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginForm incomingForm) {
        User user = service.findUserByUsername(incomingForm.getUsername());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/addRecipe/{userId}/{recipeId}")
    public ResponseEntity<User> addRecipeToLiked(@PathVariable Long userId,@PathVariable Long recipeId){
        Optional<User> user = service.getUser(userId);
        if(user.isEmpty()){
            System.out.println("error with user");
            return ResponseEntity.badRequest().build();
        }
        System.out.println(user.get().password);
        Recipe fav = recipeClient.getRecipeById(recipeId);
        if(fav == null){
            System.out.println("error with recipe client");
            return ResponseEntity.badRequest().build();
        }
        user.get().getFavorite().add(fav);
        service.saveUser(user.get());

        return ResponseEntity.ok(user.get());


    }

    @PostMapping("/removeRecipe/{userId}/{recipeId}")
    public ResponseEntity<User> removeRecipeFromLiked(@PathVariable Long userId,@PathVariable Long recipeId){
        Optional<User> user = service.getUser(userId);
        if(user.isEmpty()){
            System.out.println("error with user");
            return ResponseEntity.badRequest().build();
        }

        ArrayList<Recipe> list = user.get().getFavorite();
        for(int i = 0;i<list.size();i++){
            if(list.get(i).id().equals(recipeId)){
                list.remove(i);
                user.get().setFavorite(list);
                service.saveUser(user.get());
                return ResponseEntity.ok(user.get());

            }
        }

        return ResponseEntity.badRequest().build();


    }


}
