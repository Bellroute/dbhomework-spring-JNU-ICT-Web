package codesquad.controller;

import codesquad.dto.CommentDTO;
import codesquad.exception.UnAuthorizedException;
import codesquad.model.Comment;
import codesquad.service.CommentService;
import codesquad.utils.HttpSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class ApiCommentController {
    private static final Logger log = LoggerFactory.getLogger(ApiCommentController.class);

    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public Comment uploadComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            throw new UnAuthorizedException();
        }
        return commentService.saveComment(session, postId, commentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Long>> delete(@PathVariable Long id, HttpSession session) {
        if (!HttpSessionUtils.isSessionedUser(session)) {
            throw new UnAuthorizedException();
        }

        log.info("id -> ", id);

        return commentService.deleteAnswer(session, id);
    }
}
