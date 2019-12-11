package com.battleship.app.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Date> {
   // List<Game> findByDate(Date date);
}

