package models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "posts")
@NamedQueries({ @NamedQuery(name = "getAllPosts", query = "SELECT p FROM Post AS p ORDER BY p.id DESC"),

        @NamedQuery(name = "getPostsCount", query = "SELECT COUNT(p) FROM Post AS p"),
        @NamedQuery(name = "getMyAllPosts", query = "SELECT p FROM Post AS p WHERE p.user = :user ORDER BY p.id DESC"),
        @NamedQuery(name="getMyPostsCount",query="SELECT COUNT(p) FROM Post AS p WHERE p.user = :user")

})
@Entity
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "image", length = 255, nullable = false)
    private String image;

    @ManyToMany(mappedBy = "my_favorite_post_list", fetch = FetchType.EAGER)
    private List<User> favorited_user_list;

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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<User> getFavorited_user_list() {
        return favorited_user_list;
    }

    public void setFavorited_user_list(List<User> favorited_user_list) {
        this.favorited_user_list = favorited_user_list;
    }

}
