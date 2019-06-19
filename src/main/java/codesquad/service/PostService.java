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
    private final static int CATEGORY_NOTICE = 1;
    private final static int CATEGORY_ACTIVITES_INFO = 2;
    private final static int CATEGORY_JOB_INFO = 3;
    private final static int CATEGORY_POSTS = 4;

    @Autowired
    private PostRepository postRepository;

    public void savePost(Post post, HttpSession session) {
        post.setWriter(HttpSessionUtils.getSessionedUser(session));
        post.setTime(TimeUtils.getCurrentTime());
        postRepository.save(post);
    }

    public Iterable<Post> findPosts() {
        return postRepository.findByCategory(CATEGORY_POSTS);
    }

    public Iterable<Post> findNotice() {
        return postRepository.findByCategory(CATEGORY_NOTICE);
    }

    public Iterable<Post> findActivitesInfo() {
        return postRepository.findByCategory(CATEGORY_ACTIVITES_INFO);
    }

    public Iterable<Post> findJobInfo() {
        return postRepository.findByCategory(CATEGORY_JOB_INFO);
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
