package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name="posts")
@NamedQueries({
    @NamedQuery(
        name="getAllPosts",
        query="SELECT p FROM Post AS p ORDER BY p.id DESC"
            ),

    @NamedQuery(
        name="getPostsCount",
        query="SELECT COUNT(p) FROM Post AS p"
            ),
})
@Entity
public class Post {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @Column(name="title",length=255,nullable=false)
    private String title;

    @Lob
    @Column(name="content",nullable=false)
    private String content;

    @Column(name="createde_at",nullable=false)
    private Timestamp createde_at;

    @Column(name = "image", length = 255, nullable = false)
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatede_at() {
        return createde_at;
    }

    public void setCreatede_at(Timestamp createde_at) {
        this.createde_at = createde_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
