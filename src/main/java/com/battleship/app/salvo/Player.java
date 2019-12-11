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

    private String userName;

    public Player(){};

    public Player(String userName){
        this.userName = userName;
    }


    public void addGamePlayer(GamePlayer gameplayer){
        //gameplayer.setPlayer(this);
        gamePlayers.add(gameplayer);
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
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
