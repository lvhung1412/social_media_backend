package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.repository.model.ModelAI;
import com.example.SocialMedia.repository.model.ModelCommonFriend;
import com.example.SocialMedia.repository.model.ModelStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByPhone(String phone);
    Optional<User> findUserByVerificationCodeAndEmail(String verificationCode, String email);
    Optional<User> findUserByUsername(String username);
    @Query(nativeQuery = true, value ="SELECT username, avatar, bio, birthday, create_date, email, enable, gender, name, nickname, password, phone, verification_code, address, role_id, security FROM \n" +
            "(leaf_db.user left join (Select user_from, user_to, status as friend from leaf_db.relationship where user_from = :user or user_to = :user) AS T\n" +
            "ON user.username = T.user_from or user.username = T.user_to )\n" +
            "WHERE name like '%' :name '%' and role_id = 'CUSTOMER' and username != :user and (friend != 'BLOCK' OR friend IS NUll ) LIMIT 8")
    List<User> searchByName(@Param("name") String text, @Param("user") String user);

    @Query(nativeQuery = true, value ="SELECT username, avatar, bio, birthday, create_date, email, enable, gender, name, nickname, password, phone, verification_code, address, role_id, security FROM \n" +
            "(leaf_db.user left join (Select user_from, user_to, status as friend from leaf_db.relationship where user_from = :user or user_to = :user) AS T\n" +
            "ON user.username = T.user_from or user.username = T.user_to )\n" +
            "WHERE name like '%' :name '%' and role_id = 'CUSTOMER' and username != :user and friend = 'FRIEND' LIMIT 10")
    List<User> searchFriendByName(@Param("name") String text, @Param("user") String user);

    @Query(nativeQuery = true, value = "SELECT T.user as user1, P.user as user2, COUNT(*) as common FROM\n" +
            "(\n" +
            "\tSELECT user_from as user, user_to as friend FROM leaf_db.relationship WHERE user_from = :user1 \n" +
            "\tUNION\n" +
            "\tSELECT user_to as user, user_from as friend FROM leaf_db.relationship WHERE user_to = :user1 \n" +
            ") AS T\n" +
            "JOIN\n" +
            "(\n" +
            "\tSELECT user_from as user, user_to as friend FROM leaf_db.relationship WHERE user_from = :user2 \n" +
            "\tUNION\n" +
            "\tSELECT user_to as user, user_from as friend FROM leaf_db.relationship WHERE user_to = :user2 \n" +
            ") AS P\n" +
            "ON T.friend = P.friend\n" +
            "GROUP BY T.user, P.user")
    List<ModelCommonFriend> getCommonFriend(@Param("user1") String user1, @Param("user2") String user2);
    @Query(nativeQuery = true, value = "SELECT * FROM \n" +
            "(\n" +
            "\tSELECT M.user_id, M.item_id, (SUM(rating)/700) AS rating FROM\n" +
            "\t(\n" +
            "\t\t(\n" +
            "\t\t\tSELECT post.user AS user_id, reaction_post.user item_id, (COUNT(reaction_post.user)*5) AS rating\n" +
            "\t\t\tFROM ( leaf_db.reaction_post LEFT JOIN leaf_db.post ON reaction_post.post = post.id ) \n" +
            "\t\t\tWHERE reaction_post.user != post.user AND post.status = 'ENABLE' AND reaction_post.status='ENABLE'\n" +
            "\t\t\tGROUP BY reaction_post.user, post.user\n" +
            "\t\t) \n" +
            "\t\tUNION\n" +
            "\t\t(\n" +
            "\t\t\tSELECT post.user AS user_id, comment.user AS item_id, (COUNT(comment.user)*10) AS rating\n" +
            "\t\t\tFROM (leaf_db.comment LEFT JOIN leaf_db.post ON comment.post = post.id ) \n" +
            "\t\t\tWHERE comment.user != post.user AND post.status = 'ENABLE' AND comment.status='ENABLE'\n" +
            "\t\t\tGROUP BY comment.user, post.user\n" +
            "\t\t) \n" +
            "\t\tUNION\n" +
            "\t\t(\n" +
            "\t\t\tSELECT M1.user as user_id, M2.user as item_id, (COUNT(*)*20) as rating FROM \n" +
            "\t\t\t(\n" +
            "\t\t\t\t(SELECT T1.user , P1.friend FROM\n" +
            "\t\t\t\t\t(SELECT DISTINCT user_to as user FROM leaf_db.relationship) AS T1\n" +
            "\t\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t\t(SELECT user_to, user_from as friend FROM leaf_db.relationship WHERE status = 'FRIEND') AS P1\n" +
            "\t\t\t\t\tON T1.user = P1.user_to\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tUNION\n" +
            "\t\t\t\t(SELECT T2.user , P2.friend FROM\n" +
            "\t\t\t\t\t(SELECT DISTINCT user_from as user FROM leaf_db.relationship) AS T2\n" +
            "\t\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t\t(SELECT user_from, user_to as friend FROM leaf_db.relationship WHERE status = 'FRIEND') AS P2\n" +
            "\t\t\t\t\tON T2.user = P2.user_from\n" +
            "\t\t\t\t)\n" +
            "\t\t\t) AS M1\n" +
            "\t\t\tLEFT JOIN\n" +
            "\t\t\t(\n" +
            "\t\t\t\t(SELECT T1.user , P1.friend FROM\n" +
            "\t\t\t\t\t(SELECT DISTINCT user_to as user FROM leaf_db.relationship) AS T1\n" +
            "\t\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t\t(SELECT user_to, user_from as friend FROM leaf_db.relationship WHERE status = 'FRIEND') AS P1\n" +
            "\t\t\t\t\tON T1.user = P1.user_to\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tUNION\n" +
            "\t\t\t\t(SELECT T2.user , P2.friend FROM\n" +
            "\t\t\t\t\t(SELECT DISTINCT user_from as user FROM leaf_db.relationship) AS T2\n" +
            "\t\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t\t(SELECT user_from, user_to as friend FROM leaf_db.relationship WHERE status = 'FRIEND') AS P2\n" +
            "\t\t\t\t\tON T2.user = P2.user_from\n" +
            "\t\t\t\t)\n" +
            "\t\t\t) AS M2\n" +
            "\t\t\tON M1.user != M2.user AND M1.friend = M2.friend\n" +
            "\t\t\tGROUP BY M1.user , M2.user\n" +
            "\t\t)\n" +
            "\t) AS M\n" +
            "\tGROUP BY user_id, item_id\n" +
            ") AS MM\n" +
            "WHERE rating > (:rating/700)\n" +
            "ORDER BY user_id ASC, item_id ASC")
    List<ModelAI> getDataSourceForAI(@Param("rating") Integer rating);

    @Query(nativeQuery = true, value = "SELECT MONTH(create_date) as month, COUNT(*) as count FROM leaf_db.user WHERE YEAR(create_date) = '2023' AND role_id = 'CUSTOMER' GROUP BY MONTH(create_date) ORDER BY month asc\n")
    List<ModelStatistic> countUserEachMonth();

    @Query(nativeQuery = true, value = "SELECT MONTH(create_date) as month, COUNT(*) as count FROM leaf_db.post WHERE YEAR(create_date) = '2023' GROUP BY MONTH(create_date) ORDER BY month asc\n")
    List<ModelStatistic> countPostEachMonth();

    @Query(nativeQuery = true, value = "SELECT MONTH(create_date) as month, COUNT(*) as count FROM leaf_db.comment WHERE YEAR(create_date) = '2023' GROUP BY MONTH(create_date) ORDER BY month asc\n")
    List<ModelStatistic> countCommentEachMonth();
}
