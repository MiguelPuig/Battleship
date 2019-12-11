package com.battleship.app.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean

	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository){
		return (args) -> {
			Player player1 = new Player("gorka");
			Player player2 = new Player("oriol");
			Player player3 = new Player("enrique");
			Player player4 = new Player("jerome");
			Player player5 = new Player("max");
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);

			System.out.println(player1 + "..." + player2);

			Game game = new Game(new Date());
			Game game2 = new Game( Date.from(game.getDate().toInstant().plusSeconds(3600)));
			Game game3 = new Game( Date.from(game.getDate().toInstant().plusSeconds(7200)));
			gameRepository.save(game);
			gameRepository.save(game2);
			gameRepository.save(game3);

			System.out.println(game + "..." + game2);

			GamePlayer gameplayer = new GamePlayer(game,player1,new Date());
			GamePlayer gameplayer2 = new GamePlayer(game,player2,new Date());
			gamePlayerRepository.save(gameplayer);
			gamePlayerRepository.save(gameplayer2);

		};
	}
}
