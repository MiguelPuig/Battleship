package com.battleship.app.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    // mapped By = search player in GamePlayer java class.
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    private String userName;
    private String email;
    private String password;

    public Score getScorePlayer(Game game){
        return this.scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    public Player(){};

    public Player(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }


    public void addGamePlayer(GamePlayer gameplayer){
        //gameplayer.setPlayer(this);
        gamePlayers.add(gameplayer);
    }

    public void addScore(Score score){
        scores.add(score);
    }
    public Set<Score> setScores(){
        return scores;
    }
    public List<Double> getScores(){
        return scores.stream().map(score -> score.getScore()).collect(Collectors.toList());
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<Game> getGames() {
        return gamePlayers.stream().map(gamePlayer -> gamePlayer.getGame()).collect(Collectors.toList());
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
