package com.battleship.app.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //@OneToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name="date_id")
    private Date date;


    public GamePlayer(){};

    public GamePlayer(Game game, Player player,Date date){
        player.addGamePlayer(this);
        game.addGamePlayer(this);
        this.game = game;
        this.player = player;
        this.date = date;
    }


    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Date getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id=" + id +
                ", player=" + player +
                ", game=" + game +
                ", date=" + date +
                '}';
    }
}
