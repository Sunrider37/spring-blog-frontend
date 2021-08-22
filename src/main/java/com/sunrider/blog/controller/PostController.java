package com.sunrider.blog.controller;

import com.sunrider.blog.dto.PostDto;
import com.sunrider.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostDto postDto){
        postService.createPost(postDto);
        return ResponseEntity.ok("Post has been created");
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> showAllPosts(){
        return ResponseEntity.ok(postService.showAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }
}
