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
    private PostService postService;

    @PostMapping("/posts")
    public String posts(Post post, HttpSession session) {
        postService.savePost(post, session);

        if (post.isNotice()) {
            return "redirect:/notice";
        }
        if (post.isActivitesInfoList()) {
            return "redirect:/activitesInfo";
        }
        if (post.isjobInfoList()) {
            return "redirect:/jobInfo";
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String list(Model model) {
        model.addAttribute("posts", postService.findPosts());

        return "/posts/list";
    }

    @GetMapping("/notice")
    public String noticeList(Model model) {
        model.addAttribute("posts", postService.findNotice());

        return "/posts/notice";
    }

    @GetMapping("/activitesInfo")
    public String activitesInfoList(Model model) {
        model.addAttribute("posts", postService.findActivitesInfo());

        return "/posts/activitesInfo";
    }

    @GetMapping("/jobInfo")
    public String jobInfoList(Model model) {
        model.addAttribute("posts", postService.findJobInfo());

        return "/posts/jobInfo";
    }

    @GetMapping("/posts/form")
    public String getPostForm(HttpSession session, Model model) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }
        model.addAttribute("user", HttpSessionUtils.getSessionedUser(session));

        return "/posts/form";
    }

    @GetMapping("/posts/{postId}")
    public String accessPost(@PathVariable Long postId, Model model) {
        model.addAttribute("post", postService.findPostById(postId));

        return "/posts/show";
    }

    @GetMapping("/posts/{postId}/form")
    public String modifyPost(@PathVariable Long postId, Model model, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }

        if (!postService.isSameWriter(postId, session)) {
            return "redirect:/posts/{postId}";
        }
        model.addAttribute("post", postService.findPostById(postId));

        return "/posts/updateForm";
    }

    @PutMapping("/posts/{postId}")
    public String updatePost(@PathVariable Long postId, Post updatedPost) {
        postService.updatePost(postService.findPostById(postId), updatedPost);

        return "redirect:/posts/{postId}";
    }

    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable Long postId, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }

        if (!postService.isSameWriter(postId, session)) {
            return "redirect:/posts/{postId}";
        }
        postService.deletePost(postId);

        return "redirect:/";
    }
}
