package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "favorites")
@NamedQueries({
        // 投稿にいいねした人を表示する
        @NamedQuery(name = "getFavoritesOfAttenssion", query = "SELECT f FROM Favorite AS f WHERE f.user=:user AND f.post=:post"),
        // 日報にいいねした数を数える
        @NamedQuery(name = "getFavoritesCount", query = "SELECT COUNT(f) FROM Favorite AS f WHERE f.post=:post") })
// エンティティ
@Entity
public class Favorite {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ユーザーモデルと多対一で結びつく
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ポストモデルと多対一で結びつく
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // ゲッターとセッター
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
