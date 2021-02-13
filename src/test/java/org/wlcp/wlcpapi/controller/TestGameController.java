package org.wlcp.wlcpapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.dto.GameDto;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.service.GameService;
import org.wlcp.wlcpapi.service.UsernameService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameController.class)
public class TestGameController {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
	private GameRepository gameRepository;
	
    @MockBean
	private GameService gameService;
    
    @MockBean
    private UsernameService usernameService;
    
    @Test
    public void testGetGamesSuccess() throws UnsupportedEncodingException, Exception {
    	Game game = new Game("gameid", 0, 0, null, true, false);
    	List<Game> games = new ArrayList<Game>();
    	games.add(game);
    	when(gameRepository.findAll()).thenReturn(games);
    	mvc.perform(get("/gameController/getGames")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testGetGameByGameIdSuccess() throws UnsupportedEncodingException, Exception {
    	Game game = new Game("gameid", 0, 0, null, true, false);
    	when(gameRepository.findById(any(String.class))).thenReturn(Optional.of(game));
    	mvc.perform(get("/gameController/getGame/" + game.getGameId())
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testGetPrivateGamesSuccess() throws UnsupportedEncodingException, Exception {
    	GameDto gameDto = new GameDto("gameId");
    	List<GameDto> games = new ArrayList<GameDto>();
    	games.add(gameDto);
    	when(gameService.getPrivateGames(any(String.class))).thenReturn(games);
    	mvc.perform(get("/gameController/getPrivateGames/")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.param("usernameId", "usernameId")
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testGetPublicGamesSuccess() throws UnsupportedEncodingException, Exception {
    	GameDto gameDto = new GameDto("gameId");
    	List<GameDto> games = new ArrayList<GameDto>();
    	games.add(gameDto);
    	when(gameService.getPrivateGames(any(String.class))).thenReturn(games);
    	mvc.perform(get("/gameController/getPublicGames/")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.param("usernameId", "usernameId")
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testLoadGameSuccess() throws UnsupportedEncodingException, Exception {
    	Game game = new Game();
    	when(gameService.loadGame(any(String.class))).thenReturn(game);
    	mvc.perform(get("/gameController/loadGame/")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.param("gameId", "gameId")
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testSaveGameSuccess() throws UnsupportedEncodingException, Exception {
    	Game game = new Game();
    	when(gameService.saveGame(any(Game.class))).thenReturn(game);
    	mvc.perform(post("/gameController/saveGame/")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.content(new ObjectMapper().writeValueAsString(game))
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testCopyGameSuccess() throws UnsupportedEncodingException, JsonProcessingException, Exception {
    	Game game = new Game();
    	CopyRenameDeleteGameDto copyDto = new CopyRenameDeleteGameDto();
    	when(gameService.copyGame(any(CopyRenameDeleteGameDto.class))).thenReturn(game);
    	mvc.perform(post("/gameController/copyGame/")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.content(new ObjectMapper().writeValueAsString(copyDto))
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testRenameGameSuccess() throws UnsupportedEncodingException, JsonProcessingException, Exception {
    	Game game = new Game();
    	CopyRenameDeleteGameDto renameDto = new CopyRenameDeleteGameDto();
    	when(gameService.renameGame(any(CopyRenameDeleteGameDto.class))).thenReturn(game);
    	mvc.perform(post("/gameController/renameGame/")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.content(new ObjectMapper().writeValueAsString(renameDto))
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testDeleteGameSuccess() throws UnsupportedEncodingException, JsonProcessingException, Exception {
    	CopyRenameDeleteGameDto deleteDto = new CopyRenameDeleteGameDto();
    	mvc.perform(post("/gameController/deleteGame/")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    			.content(new ObjectMapper().writeValueAsString(deleteDto))
    			.contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
}
