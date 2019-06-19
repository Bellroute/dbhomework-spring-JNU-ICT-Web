package codesquad.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        super("해당 Answer을 찾을 수 없습니다");
    }
}
