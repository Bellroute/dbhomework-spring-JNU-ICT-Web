package codesquad.controller;

import codesquad.model.Post;
import codesquad.service.PostService;
import codesquad.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {

    @Autowired
    private PostService questionService;

    @PostMapping("/posts")
    public String posts(Post question, HttpSession session) {
        questionService.savePost(question, session);

        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String list(Model model) {
        model.addAttribute("posts", questionService.findPosts());

        return "/posts/list";
    }

    @GetMapping("posts/form")
    public String getPostForm(HttpSession session, Model model) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }
        model.addAttribute("user", HttpSessionUtils.getSessionedUser(session));

        return "/posts/form";
    }

    @GetMapping("/posts/{postId}")
    public String accessPost(@PathVariable Long postId, Model model) {
        model.addAttribute("post", questionService.findPostById(postId));

        return "/posts";
    }

    @GetMapping("/posts/{postId}/form")
    public String modifyPost(@PathVariable Long postId, Model model, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }

        if (!questionService.isSameWriter(postId, session)) {
            return "redirect:/posts/{postId}";
        }
        model.addAttribute("post", questionService.findPostById(postId));

        return "/posts/updateForm";
    }

    @PutMapping("/questions/{postId}")
    public String updatePost(@PathVariable Long postId, Post updatedQuestion) {
        questionService.updatePost(questionService.findPostById(postId), updatedQuestion);

        return "redirect:/posts/{postId}";
    }

    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable Long postId, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }

        if (!questionService.isSameWriter(postId, session)) {
            return "redirect:/posts/{postId}";
        }
        questionService.deletePost(postId);

        return "redirect:/";
    }
}
