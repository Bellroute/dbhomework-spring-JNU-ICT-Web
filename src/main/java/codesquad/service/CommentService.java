package codesquad.service;

import codesquad.Result;
import codesquad.dto.CommentDTO;
import codesquad.exception.CommentNotFoundException;
import codesquad.exception.PostNotFoundException;
import codesquad.model.Comment;
import codesquad.model.Post;
import codesquad.repository.CommentRepository;
import codesquad.repository.PostRepository;
import codesquad.utils.HttpSessionUtils;
import codesquad.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public Comment saveAnswer(HttpSession session, Long questionId, CommentDTO commentDTO) {
        Comment comment = new Comment(HttpSessionUtils.getSessionedUser(session), findPost(questionId), commentDTO.getContents());
        comment.setTime(TimeUtils.getCurrentTime());

        Post post = findPost(questionId);
        post.addComment(comment);
        postRepository.save(post);

        return commentRepository.save(comment);
    }

    public ResponseEntity<Map<String, Long>> deleteAnswer(HttpSession session, Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.delete(HttpSessionUtils.getSessionedUser(session));

        if (comment.isDeleted()) {
            commentRepository.save(comment);
            return Result.ok(id);
        }

        return Result.fail(id);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }
}
