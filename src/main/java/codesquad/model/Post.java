package codesquad.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Comment> comments;

    public Post() {
        this.deleted = false;
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        comment.setPost(this);
        this.comments.add(comment);
    }

    public void update(Post updatedPost) {
        this.title = updatedPost.getTitle();
        this.contents = updatedPost.getContents();
    }

    public boolean isSameWriter(User sessionedUser) {
        return this.writer.isSameUser(sessionedUser.getId());
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isAbleDelete() {
        return comments.stream().allMatch(comment -> comment.isSameWriter(this.writer));
    }
}
