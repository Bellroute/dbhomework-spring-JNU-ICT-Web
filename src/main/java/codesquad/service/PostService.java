package codesquad.service;

import codesquad.exception.PostNotFoundException;
import codesquad.model.Post;
import codesquad.repository.PostRepository;
import codesquad.utils.HttpSessionUtils;
import codesquad.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void savePost(Post post, HttpSession session) {
        post.setWriter(HttpSessionUtils.getSessionedUser(session));
        post.setTime(TimeUtils.getCurrentTime());
        postRepository.save(post);
    }

    public Iterable<Post> findPosts() {
        return postRepository.findAll();
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    public void updatePost(Post post, Post updatedPost) {
        post.update(updatedPost);
        post.setTime(TimeUtils.getCurrentTime());
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (post.isAbleDelete()) {
            post.delete();
            postRepository.save(post);
        }
    }

    public boolean isSameWriter(Long id, HttpSession session) {
        return findPostById(id).isSameWriter(HttpSessionUtils.getSessionedUser(session));
    }
}
