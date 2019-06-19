package codesquad.model;

import codesquad.exception.UnAuthorizedException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_parent_id"))
    private Post post;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_comment_writer"))
    private User writer;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private boolean deleted;

    private String url;

    public Comment(User writer, Post post, String contents) {
        this.writer = writer;
        this.post = post;
        this.contents = contents;
        this.url = "/api/questions/" + post.getId() + "/answers";
        this.deleted = false;
    }

    public void delete(User sessionedUser) {
        if (!isSameWriter(sessionedUser)) {
            throw new UnAuthorizedException();
        }
        this.deleted = true;
    }

    public boolean isSameWriter(User sessionedUser) {
        return this.writer.isSameUser(sessionedUser.getId());
    }
}
