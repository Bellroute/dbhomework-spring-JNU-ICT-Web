package codesquad.controller;

import codesquad.service.CommentService;
import codesquad.utils.HttpSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public String uploadComment(@PathVariable Long postId, String contents, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }
        commentService.saveComment(session, postId, contents);

        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            return "redirect:/users/loginForm";
        }

        if (!commentService.isSameWriter(id, session)) {
            return "redirect:/posts/" + id;
        }

        commentService.deleteAnswer(id, session);

        return "redirect:/posts/" + id;
    }
}
