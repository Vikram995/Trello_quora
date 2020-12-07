package com.upgrad.quora.service.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "question")
@NamedQueries({@NamedQuery(name = "getAllQuestions", query = "select q from Question q"),
@NamedQuery(name = "getQuestionById", query = "select q from Question q where q.uuid = :uuid"),
@NamedQuery(name = "deleteQuestion", query = "delete from Question q where q.uuid = :uuid"),
@NamedQuery(name = "getQuestionsByUser", query = "select q from Question q where q.user = :user")})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "uuid")
    private String uuid;

    @Column(name = "content")
    private String content;


    @Column(name = "date")
    private ZonedDateTime date;


    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
