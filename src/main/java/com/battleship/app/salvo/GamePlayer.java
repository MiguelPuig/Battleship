package com.battleship.app.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvos = new HashSet<>();

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



    public void addShip(Ship ship){
        ships.add(ship);
        ship.setGamePlayer(this);
    }

    public void addSalvo(Salvo salvo){
        salvos.add(salvo);
        salvo.setGamePlayer(this);
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

    public Long getId() {
        return id;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvo() {
        return salvos;
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
