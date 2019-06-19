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


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public void saveComment(HttpSession session, Long questionId, String contents) {
        Comment answer = new Comment(HttpSessionUtils.getSessionedUser(session), findQuestion(questionId), contents);
        answer.setTime(TimeUtils.getCurrentTime());

        commentRepository.save(answer);
    }

    public boolean isSameWriter(Long id, HttpSession session) {
        return findQuestion(id).isSameWriter(HttpSessionUtils.getSessionedUser(session));
    }

    public void deleteAnswer(Long id, HttpSession session) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.delete(HttpSessionUtils.getSessionedUser(session));
        commentRepository.save(comment);
    }

    private Post findQuestion(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }
}
