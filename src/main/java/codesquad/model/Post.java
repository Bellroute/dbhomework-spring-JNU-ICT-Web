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
    private final static int CATEGORY_NOTICE = 1;
    private final static int CATEGORY_ACTIVITES_INFO = 2;
    private final static int CATEGORY_JOB_INFO = 3;
    private final static int CATEGORY_POSTS = 4;

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

    @Column(nullable = false)
    private int category;

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

    public boolean isNotice() {
        return this.category == CATEGORY_NOTICE;
    }

    public boolean isActivitesInfoList() {
        return this.category == CATEGORY_ACTIVITES_INFO;
    }


    public boolean isjobInfoList() {
        return this.category == CATEGORY_JOB_INFO;
    }
}
