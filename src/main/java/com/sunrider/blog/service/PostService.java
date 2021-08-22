package com.sunrider.blog.service;

import com.sunrider.blog.dto.PostDto;
import com.sunrider.blog.exception.PostNotFoundException;
import com.sunrider.blog.model.Post;
import com.sunrider.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final AuthService authService;
    private final PostRepository postRepository;

    public void createPost(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
       User user = authService.getCurrentUser().orElseThrow(()
                -> new IllegalArgumentException("No user is logged in"));
        post.setUsername(user.getUsername());
        post.setCreatedAt(Instant.now());
        postRepository.save(post);
    }

    public List<PostDto> showAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(Collectors.toList());
    }

    private PostDto mapFromPostToDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setTitle(post.getTitle());
        postDto.setUsername(post.getUsername());
        return postDto;
    }

    private Post mapFromDtoToPost(PostDto postDto){
        Post post = new Post();
        post.setId(postDto.getId());
        post.setUsername(postDto.getUsername());
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        return post;
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        return mapFromPostToDto(post);
    }
}
