package com.battleship.app.salvo;





import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SalvoRepository salvoRepository;


    @RequestMapping(value = "/api/games/players/{gpId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeSalvos(@PathVariable("gpId") Long id, Authentication authentication, @RequestBody List<String> salvoLocations) {
    Player player = loginPlayer(authentication);
        if (authentication == null && player == null){
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayer = gamePlayerRepository.getOne(id);
        if (gamePlayer == null){
            return  new ResponseEntity<>(makeMap("error", "Different id"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.ships.size() < 5){
            return new ResponseEntity<>(makeMap("error", "Can't shoot before place the ships"), HttpStatus.UNAUTHORIZED);
        }
        if (getOpponent(gamePlayer).ships.size() < 5){
            return new ResponseEntity<>(makeMap("error", "Can't shoot before Opponent place the ships"), HttpStatus.UNAUTHORIZED);
        }
       if (salvoLocations.size() != 5){
           return new ResponseEntity<>(makeMap("error", "You have to place 5 salvos"), HttpStatus.CONFLICT);
       }

        if ((getOpponent(gamePlayer).salvos.size() < gamePlayer.salvos.size())){
                return new ResponseEntity<>(makeMap("error", "Waiting for opponent shots"), HttpStatus.FORBIDDEN);
        }

        if ((makeSunkDTO(gamePlayer.salvos, gamePlayer).get("SunkShips").size() == 5) && gamePlayer.getSalvo().size() == getOpponent(gamePlayer).getSalvo().size() || (makeSunkDTO(getOpponent(gamePlayer).salvos, getOpponent(gamePlayer)).get("SunkShips").size() == 5) && (gamePlayer.getSalvo().size() == getOpponent(gamePlayer).getSalvo().size()) ||
                ((makeSunkDTO(gamePlayer.salvos, gamePlayer).get("SunkShips").size() == 5) && (makeSunkDTO(getOpponent(gamePlayer).salvos, getOpponent(gamePlayer)).get("SunkShips").size() == 5)) &&
                        (gamePlayer.getSalvo().size() == getOpponent(gamePlayer).getSalvo().size())){
            return new ResponseEntity<>(makeMap("error", "GAME OVER"), HttpStatus.FORBIDDEN);
        }

        Salvo salvo = new Salvo (gamePlayer.salvos.size()+1, salvoLocations, gamePlayer);
        salvoRepository.save(salvo);


        return new ResponseEntity<>(makeMap("success", "Placed salvos"),HttpStatus.CREATED);


}

    @RequestMapping(value = "/api/games/players/{gpId}/ships", method = RequestMethod.POST)
        public ResponseEntity<Map<String, Object>> placeShips(@PathVariable ("gpId") Long id, Authentication authentication,@RequestBody Set<Ship> ships){
        Player player = loginPlayer(authentication);
        if (authentication == null && player == null){
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayer = gamePlayerRepository.getOne(id);
        if ( gamePlayer == null ){
            return new ResponseEntity<>(makeMap("error","Different id"), HttpStatus.UNAUTHORIZED);
        }
        for (Ship ship: ships) {
            if (ship.getLocations().size() == 0) {
                return new ResponseEntity<>(makeMap("error", "Place all ships before sending"), HttpStatus.FORBIDDEN);
            }
        }

        if (gamePlayer.ships.size() == 5){
            return new ResponseEntity<>(makeMap("error", "All ships placed"), HttpStatus.FORBIDDEN);
        }

            ships.stream().forEach(ship -> {
                gamePlayer.addShip(ship);
                shipRepository.save(ship);

            });

        return new ResponseEntity<>(makeMap("success", "Placed ships"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/game/{id}/players", method = RequestMethod.POST)
        public ResponseEntity<Map<String, Object>> joinGame(@PathVariable ("id") Long id, Authentication authentication){
        Player player = loginPlayer(authentication);
        if (authentication == null && player == null ) {
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.UNAUTHORIZED);
        }
        Game game = gameRepository.getOne(id);
        if (game.equals(null)){
            return new ResponseEntity<>(makeMap("error",  "No such game"), HttpStatus.FORBIDDEN);
        } if(game.getGamePlayers().size() == 2){
            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }
        GamePlayer newGamePlayer = new GamePlayer(game, player, new Date());
        gamePlayerRepository.save(newGamePlayer);

        return new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
        public ResponseEntity<Map<String,Object>> register(
                @RequestBody Player player) {

            if (player.getUserName().isEmpty() || player.getEmail().isEmpty() || player.getPassword().isEmpty()) {
                return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.FORBIDDEN);
            }

            if (playerRepository.findByUserName(player.getUserName()) != null) {
                return new ResponseEntity<>(makeMap("error","Name already in use"), HttpStatus.FORBIDDEN);
            }

            player.setPassword(passwordEncoder.encode(player.getPassword()));
            Player newPlayer = playerRepository.save(player);
            return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(value = "/api/games" , method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postGames(Authentication authentication) {
        Player player = loginPlayer(authentication);
        if (authentication == null && player == null ) {
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.UNAUTHORIZED);
        }
       Game newGame = new Game();
        gameRepository.save(newGame);

        GamePlayer newGamePlayer = new GamePlayer(newGame, player, new Date());
        gamePlayerRepository.save(newGamePlayer);

        return new  ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);

    }


    @RequestMapping(value = "/api/games" , method= RequestMethod.GET)
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("games", gameRepository.findAll().stream().map(game -> makeGameDTO(game)).collect(Collectors.toList()));
        if (authentication == null) {
            dto.put("player", null);
        } else {
            dto.put("player", makePlayerDTO(loginPlayer(authentication)));
        }
        return dto;
    }
        private Player loginPlayer(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> makeGamePlayerDTO(gamePlayer)));
        return dto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId() );
        dto.put("userName", player.getUserName());
        dto.put("email", player.getEmail());
        return dto;
    }


    @RequestMapping(value = "/api/game_view/{id}" , method= RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getOneGame(@PathVariable("id") Long id, Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        Player player = loginPlayer(authentication);
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "You have to login"), HttpStatus.UNAUTHORIZED);
        }
        if (player == null){
            return new ResponseEntity<>(makeMap("error", "Player doesn't exist"), HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayer = gamePlayerRepository.getOne(id);
        if (gamePlayer.equals(null)){
            return new ResponseEntity<>(makeMap("error", "GamePlayer does't exist"), HttpStatus.UNAUTHORIZED);
        }
        if(!player.equals(gamePlayer.getPlayer())){
            return new ResponseEntity<>(makeMap("error", "Don't try to cheat"), HttpStatus.CONFLICT);
        }

        dto.put("games", makeGameDTO(gamePlayer.getGame()));
        dto.put("ships", gamePlayer.getShips().stream().map(ship -> makeShipDTO(ship)));
        dto.put("mySalvoes", gamePlayer.getSalvo().stream().map(salvo -> makeSalvoDTO(salvo)));
        dto.put("state", getLogic(gamePlayer));
        if (getOpponent(gamePlayer) != null){
            dto.put("opponentSalvoes", getOpponent(gamePlayer).getSalvo().stream().map(salvo -> makeSalvoDTO(salvo)));
            dto.put("hits", gamePlayer.getSalvo().stream().sorted(Comparator.comparing(Salvo::getTurn)).map(salvo -> makeTurnDTO(salvo)));

            dto.put("sunk", makeSunkDTO(gamePlayer.salvos, gamePlayer));

        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // METHOD LOGIC FOR THE GAME //

    private Map<String, Object> getLogic (GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        if (getOpponent(gamePlayer) == null) {
            dto.put("Logic", "Waiting for opponent");

        }else {
                dto.put("Logic", "Place ships");

            if (gamePlayer.ships.size() > getOpponent(gamePlayer).ships.size()){
                dto.put("Logic", "Waiting for opponent ships");
            }else {

                if (gamePlayer.getShips().size() == 5 && getOpponent(gamePlayer).getShips().size() == 5) {
                    dto.put("Logic", "Shots");
                }
                if (getOpponent(gamePlayer).salvos.size() < gamePlayer.salvos.size()) {
                    dto.put("Logic", "Waiting for opponent shots");
                }
                if ((makeSunkDTO(gamePlayer.salvos, gamePlayer).get("SunkShips").size() == 5) && gamePlayer.getSalvo().size() == getOpponent(gamePlayer).getSalvo().size()
                ) {
                    Score score = new Score(1.00, gamePlayer.getPlayer(), gamePlayer.getGame());
                    scoreRepository.save(score);
                    dto.put("Logic", "VICTORY");
                }
                if((makeSunkDTO(getOpponent(gamePlayer).salvos, getOpponent(gamePlayer)).get("SunkShips").size() == 5) && (gamePlayer.getSalvo().size() == getOpponent(gamePlayer).getSalvo().size())){
                    Score score = new Score(0.00,gamePlayer.getPlayer(), gamePlayer.getGame());
                    scoreRepository.save(score);
                    dto.put("Logic", "DEFEAT");
                }
                if (((makeSunkDTO(gamePlayer.salvos, gamePlayer).get("SunkShips").size() == 5) && (makeSunkDTO(getOpponent(gamePlayer).salvos, getOpponent(gamePlayer)).get("SunkShips").size() == 5)) &&
                        (gamePlayer.getSalvo().size() == getOpponent(gamePlayer).getSalvo().size())){


                    if (gamePlayer.getPlayer().getScorePlayer(gamePlayer.getGame()) == null){

                    Score score = new Score(0.50,gamePlayer.getPlayer(),gamePlayer.getGame());
                    scoreRepository.save(score);
                    Score score1 = new Score(0.50, getOpponent(gamePlayer).getPlayer(), getOpponent(gamePlayer).getGame());
                    scoreRepository.save(score1);
                }
                    dto.put("Logic", "DRAW");
                }
            }
        }
       return dto;
    }


    private Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("location", ship.getLocations());
        dto.put("type", ship.getType());
        return dto;
    }



    private Map<String, Object> makeSalvoDTO(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("location", salvo.getLocations());
        dto.put("turn", salvo.getTurn());

        return dto;
    }

    private Map<Integer, List<String>> makeTurnDTO(Salvo salvo){
        Map<Integer, List<String>> dto = new LinkedHashMap<>();
        List<String> Hits = new ArrayList<>();
        for ( Ship ship : getOpponent(salvo.getGamePlayer()).getShips()) {
            for (String loc : ship.getLocations()) {
                if ( salvo.getLocations().contains(loc)){
                    Hits.add(loc);

                }
            }
        }
        dto.put(salvo.getTurn(), Hits);
        return dto;
    }

    private  Map<String, List<String>> makeSunkDTO(Set<Salvo> salvos, GamePlayer gamePlayer){
        Map<String, List<String>> dto = new LinkedHashMap<>();
        List<String> Sunk = new ArrayList<>();

            for (Ship ship : getOpponent(gamePlayer).getShips()) {
                int shipSize = ship.getLocations().size();
                for (Salvo salvo : salvos.stream().sorted((salvo1,salvo2) -> salvo1.getTurn().compareTo(salvo2.getTurn())).collect(Collectors.toList())) {
                    for (String loc : salvo.getLocations()) {
                        if (ship.getLocations().contains(loc)) {
                            shipSize = shipSize - 1;

                            if (shipSize == 0) {
                                Sunk.add(ship.getType());
                            }
                        }
                    }
            }
        }
       dto.put("SunkShips", Sunk);
    return dto;
    }

    // METHOD TO RETURN THE OPPONENT //

    private GamePlayer getOpponent (GamePlayer gamePlayer){
        return gamePlayer.getGame().getGamePlayers()
                .stream()
                .filter(gamePlayer1 -> !gamePlayer1.getId()
                        .equals(gamePlayer.getId()))
                .findFirst()
                        .orElse(null);
    }




    @RequestMapping(value = "/api/ladderBoard" , method= RequestMethod.GET)
    public List<Map<String, Object>> getLadderBoard(){
        return playerRepository.findAll().stream().map(player -> getPlayerDetail(player)).collect(Collectors.toList());
    }


    private Map<String, Object> getPlayerDetail (Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", makePlayerDTO(player));
        dto.put("score", player.getScores());
        return dto;
    }
}
