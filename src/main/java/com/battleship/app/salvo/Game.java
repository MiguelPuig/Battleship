package com.battleship.app.salvo;



import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date date;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    public Game(){};

    public Game (Date date){
        this.date = date;
    }


    public void addGamePlayer(GamePlayer gameplayer) {
        //gamePlayer.setGame(this);
        gamePlayers.add(gameplayer);
    }

    public void addScore (Score score){
        scores.add(score);
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public List<Player> getPlayers() {
        return gamePlayers.stream().map(gamePlayer -> gamePlayer.getPlayer()).collect(Collectors.toList());
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Game{" +
                ", date=" + date +
                '}';
    }
}
