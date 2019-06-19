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

    @PostMapping("/questions")
    public String questions(Post question, HttpSession session) {
        questionService.savePost(question, session);

        return "redirect:/";
    }

    @GetMapping("/questions")
    public String list(Model model) {
        model.addAttribute("questions", questionService.findPosts());

        return "/posts/list";
    }

    @GetMapping("questions/form")
    public String getQuestionForm(HttpSession session, Model model) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }
        model.addAttribute("user", HttpSessionUtils.getSessionedUser(session));

        return "/posts/form";
    }

    @GetMapping("/questions/{questionId}")
    public String accessQuestion(@PathVariable Long questionId, Model model) {
        model.addAttribute("question", questionService.findPostById(questionId));

        return "/posts/show";
    }

    @GetMapping("/questions/{questionId}/form")
    public String modifyQuestion(@PathVariable Long questionId, Model model, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }

        if (!questionService.isSameWriter(questionId, session)) {
            return "redirect:/questions/{questionId}";
        }
        model.addAttribute("question", questionService.findPostById(questionId));

        return "/posts/updateForm";
    }

    @PutMapping("/questions/{questionId}")
    public String updateQuestion(@PathVariable Long questionId, Post updatedQuestion) {
        questionService.updatePost(questionService.findPostById(questionId), updatedQuestion);

        return "redirect:/questions/{questionId}";
    }

    @DeleteMapping("/questions/{questionId}")
    public String deleteQuestion(@PathVariable Long questionId, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }

        if (!questionService.isSameWriter(questionId, session)) {
            return "redirect:/questions/{questionId}";
        }
        questionService.deletePost(questionId);

        return "redirect:/";
    }
}
