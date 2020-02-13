package com.battleship.app.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ElementCollection
    @JoinColumn (name="location")
    private List<String> locations = new ArrayList<>();
    private Integer turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Salvo(){}

    public Salvo(Integer turn, List<String> locations, GamePlayer gamePlayer){
        gamePlayer.addSalvo(this);
        this.turn = turn;
        this.locations = locations;
        this.gamePlayer = gamePlayer;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }


    @Override
    public String toString() {
        return "Salvo{" +
                "id=" + id +
                ", locations=" + locations +
                ", turn=" + turn +
                ", gamePlayer=" + gamePlayer +
                '}';
    }
}
