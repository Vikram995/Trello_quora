package com.upgrad.quora.service.entity;


import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "answer")
@NamedQueries({@NamedQuery(name = "getAnswerById", query = "select a from Answer a where a.uuid = :uuid"),
@NamedQuery(name = "getAllQuestionsByQuestion", query = "select a from Answer a where a.question = :question")})
public class Answer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "uuid")
    private String uuid;


    @Column(name = "ans")
    private String ans;


    @Column(name = "date")
    private ZonedDateTime date;


    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "question_id")
    private Question question;

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

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
