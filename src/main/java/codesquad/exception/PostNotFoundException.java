package codesquad.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("해당 Question을 찾을 수 없습니다");
    }
}
