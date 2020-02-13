package com.battleship.app.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.Location;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean

	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository){
		return (args) -> {
			Player player1 = new Player("Jack Bauer", "j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player player2 = new Player("Chloe O'Brian", "c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player player3 = new Player("Kim Bauer", "kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			Player player4 = new Player("Tony Almeida", "t.almeida@ctu.gov", passwordEncoder().encode("mole"));

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);


			System.out.println(player1 + "..." + player2);

			Game game = new Game(new Date());
			Game game2 = new Game( Date.from(game.getDate().toInstant().plusSeconds(3600)));
			Game game3 = new Game( Date.from(game.getDate().toInstant().plusSeconds(7200)));
			Game game4 = new Game( Date.from(game.getDate().toInstant().plusSeconds(10800)));
			Game game5 = new Game( Date.from(game.getDate().toInstant().plusSeconds(14400)));
			Game game6 = new Game( Date.from(game.getDate().toInstant().plusSeconds(18000)));
			Game game7 = new Game( Date.from(game.getDate().toInstant().plusSeconds(21600)));
			Game game8 = new Game( Date.from(game.getDate().toInstant().plusSeconds(25200)));


			gameRepository.save(game);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);

			System.out.println(game + "..." + game2);

			GamePlayer gameplayer = new GamePlayer(game,player1,new Date());
			GamePlayer gameplayer2 = new GamePlayer(game,player2,new Date());
			gamePlayerRepository.save(gameplayer);
			gamePlayerRepository.save(gameplayer2);
			GamePlayer gameplayer3 = new GamePlayer(game2,player1,new Date());
			GamePlayer gameplayer4 = new GamePlayer(game2,player2,new Date());
			gamePlayerRepository.save(gameplayer3);
			gamePlayerRepository.save(gameplayer4);
			GamePlayer gameplayer5 = new GamePlayer(game3,player2,new Date());
			GamePlayer gameplayer6 = new GamePlayer(game3,player4,new Date());
			gamePlayerRepository.save(gameplayer5);
			gamePlayerRepository.save(gameplayer6);
			GamePlayer gameplayer7 = new GamePlayer(game4,player2,new Date());
			GamePlayer gameplayer8 = new GamePlayer(game4,player1,new Date());
			gamePlayerRepository.save(gameplayer7);
			gamePlayerRepository.save(gameplayer8);
			GamePlayer gameplayer9 = new GamePlayer(game5,player4,new Date());
			GamePlayer gameplayer10 = new GamePlayer(game5,player1,new Date());
			gamePlayerRepository.save(gameplayer9);
			gamePlayerRepository.save(gameplayer10);
			GamePlayer gameplayer11 = new GamePlayer(game6,player3,new Date());
			//GamePlayer gameplayer12 = new GamePlayer(game6,player4,new Date());
			gamePlayerRepository.save(gameplayer11);
			//gamePlayerRepository.save(gameplayer12);
			GamePlayer gameplayer13 = new GamePlayer(game7,player4,new Date());
			//GamePlayer gameplayer14 = new GamePlayer(game7, player1,new Date());
			gamePlayerRepository.save(gameplayer13);
			//gamePlayerRepository.save(gameplayer14);
			GamePlayer gameplayer15 = new GamePlayer(game8,player3,new Date());
			GamePlayer gameplayer16 = new GamePlayer(game8, player4,new Date());
			gamePlayerRepository.save(gameplayer15);
			gamePlayerRepository.save(gameplayer16);


			Ship ship = new Ship("Destroyer", Arrays.asList("H2","H3","H4"),  gameplayer);

			Ship ship1 = new Ship("Submarine", Arrays.asList("E1","F1","G1"), gameplayer);
			Ship ship2 = new Ship("PatrolBoat", Arrays.asList("B4", "B5"), gameplayer);

			Ship ship3 = new Ship("Destroyer", Arrays.asList("B5","C5","D5"), gameplayer2);
			Ship ship4 = new Ship("PatrolBoat", Arrays.asList("F1","F2"), gameplayer2);

			Ship ship5 = new Ship("Destroyer", Arrays.asList("B5","C5","D5"), gameplayer3);
			Ship ship6 = new Ship("PatrolBoat", Arrays.asList("C6","C7"), gameplayer3);

			Ship ship7 = new Ship("Submarine", Arrays.asList("A2","A3","A4"), gameplayer4);
			Ship ship8 = new Ship("PatrolBoat", Arrays.asList("G6","H6"), gameplayer4);

			Ship ship9 = new Ship("Destroyer", Arrays.asList("B5","C5","D5"), gameplayer5);
			Ship ship10 = new Ship("PatrolBoat", Arrays.asList("C6","C7"), gameplayer5);

			Ship ship11= new Ship("Submarine", Arrays.asList("A2","A3","A4"), gameplayer6);
			Ship ship12 = new Ship("PatrolBoat", Arrays.asList("G6","H6"), gameplayer6);

			Ship ship13= new Ship("Destroyer", Arrays.asList("B5","C5", "D5"), gameplayer7);
			Ship ship14 = new Ship("PatrolBoat", Arrays.asList("C6", "C7"), gameplayer7);

			Ship ship15= new Ship("Submarine", Arrays.asList("A2", "A3","A4"), gameplayer8);
			Ship ship16 = new Ship("PatrolBoat", Arrays.asList("G6", "H6"), gameplayer8);

			Ship ship17= new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), gameplayer9);
			Ship ship18= new Ship("PatrolBoat", Arrays.asList("C6", "C7"), gameplayer9);

			Ship ship19= new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), gameplayer10);
			Ship ship20= new Ship("PatrolBoat", Arrays.asList("G6", "H6"), gameplayer10);

			Ship ship21= new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), gameplayer11);
			Ship ship22= new Ship("PatrolBoat", Arrays.asList("C6", "C7"), gameplayer11);

			Ship ship23= new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), gameplayer15);
			Ship ship24= new Ship("PatrolBoat", Arrays.asList("C6", "C7"), gameplayer15);

			Ship ship25= new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), gameplayer16);
			Ship ship26= new Ship("PatrolBoat", Arrays.asList("G6","H6"), gameplayer16);

			shipRepository.save(ship);
			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);
			shipRepository.save(ship11);
			shipRepository.save(ship12);
			shipRepository.save(ship13);
			shipRepository.save(ship14);
			shipRepository.save(ship15);
			shipRepository.save(ship16);
			shipRepository.save(ship17);
			shipRepository.save(ship18);
			shipRepository.save(ship19);
			shipRepository.save(ship20);
			shipRepository.save(ship21);
			shipRepository.save(ship22);
			shipRepository.save(ship23);
			shipRepository.save(ship24);
			shipRepository.save(ship25);
			shipRepository.save(ship26);

			Salvo salvo = new Salvo(1 , Arrays.asList("B5","C5","F1"), gameplayer);
			Salvo salvo2 = new Salvo(1 , Arrays.asList("B4","B5","B6"), gameplayer2);

			Salvo salvo3 = new Salvo(2 , Arrays.asList("F2","D5"), gameplayer);
			Salvo salvo4 = new Salvo(2 , Arrays.asList("E1","H3","A2"), gameplayer2);

			Salvo salvo5 = new Salvo(1 , Arrays.asList("A2","A4","G6"), gameplayer3);
			Salvo salvo6 = new Salvo(1 , Arrays.asList("B5","D5","C7"), gameplayer4);

			Salvo salvo7 = new Salvo(2 , Arrays.asList("A3","H6"), gameplayer3);
			Salvo salvo8 = new Salvo(2 , Arrays.asList("C5","C6"), gameplayer4);

			Salvo salvo9 = new Salvo(1 , Arrays.asList("G6","H6","A4"), gameplayer5);
			Salvo salvo10 = new Salvo(1 , Arrays.asList("H1","H2","H3"), gameplayer6);

			Salvo salvo11 = new Salvo(2 , Arrays.asList("A2","A3","D8"), gameplayer5);
			Salvo salvo12 = new Salvo(2 , Arrays.asList("E1","F2","G3"), gameplayer6);

			Salvo salvo13 = new Salvo(1 , Arrays.asList("A3","A4","F7"), gameplayer7);
			Salvo salvo14 = new Salvo(1 , Arrays.asList("B5","C6","H1"), gameplayer8);

			Salvo salvo15 = new Salvo(2 , Arrays.asList("A2","G6","H6"), gameplayer7);
			Salvo salvo16 = new Salvo(2 , Arrays.asList("C5","C7","D5"), gameplayer8);

			Salvo salvo17 = new Salvo(1 , Arrays.asList("A1","A2","A3"), gameplayer9);
			Salvo salvo18 = new Salvo(1 , Arrays.asList("B5","B6","C7"), gameplayer10);

			Salvo salvo19 = new Salvo(2 , Arrays.asList("G6","G7","G8"), gameplayer9);
			Salvo salvo20 = new Salvo(2 , Arrays.asList("C6","D6","E6"), gameplayer10);

			Salvo salvo22 = new Salvo(3 , Arrays.asList("H1","H8"), gameplayer10);

			salvoRepository.save(salvo);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);
			salvoRepository.save(salvo5);
			salvoRepository.save(salvo6);
			salvoRepository.save(salvo7);
			salvoRepository.save(salvo8);
			salvoRepository.save(salvo9);
			salvoRepository.save(salvo10);
			salvoRepository.save(salvo11);
			salvoRepository.save(salvo12);
			salvoRepository.save(salvo13);
			salvoRepository.save(salvo14);
			salvoRepository.save(salvo15);
			salvoRepository.save(salvo16);
			salvoRepository.save(salvo17);
			salvoRepository.save(salvo18);
			salvoRepository.save(salvo19);
			salvoRepository.save(salvo20);
			salvoRepository.save(salvo22);

			Score score	= new Score(1.0, player1, game);
			Score score2 = new Score(0.0, player2, game);
			Score score3 = new Score(0.5, player1, game2);
			Score score4 = new Score(0.5, player2, game2);
			Score score5 = new Score(1.0, player2, game3);
			Score score6 = new Score(0.0, player4, game3);
			Score score7 = new Score(0.5, player2, game4);
			Score score8 = new Score(0.5, player1, game4);


			scoreRepository.save(score);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
			scoreRepository.save(score7);
			scoreRepository.save(score8);

		};
	}
}


@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}


@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/ladderBoard").permitAll()
				.antMatchers("/api/register").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/**").hasAuthority("USER")
				.antMatchers("/rest/**").hasAuthority("ADMIN")
				.anyRequest().fullyAuthenticated();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

		http.headers().frameOptions().disable();
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

}




