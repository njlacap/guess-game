package njslcp.project.guess_game.service;

import lombok.extern.slf4j.Slf4j;
import njslcp.project.guess_game.constant.GuessGameStatusEnum;
import njslcp.project.guess_game.dto.GameRequestDto;
import njslcp.project.guess_game.dto.GameResponseDto;
import njslcp.project.guess_game.exception.*;
import njslcp.project.guess_game.helper.GameMapper;
import njslcp.project.guess_game.helper.WordSupplier;
import njslcp.project.guess_game.model.Game;
import njslcp.project.guess_game.repository.GamePlayerRepository;
import njslcp.project.guess_game.repository.GameRepository;
import njslcp.project.guess_game.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class
 */
@Slf4j
@Service
public class GuessGameService {

    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final GameMapper gameMapper;
    private final WordSupplier wordSupplier;

    @Autowired
    public GuessGameService (GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, GameMapper gameMapper, WordSupplier wordSupplier) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.gameMapper = gameMapper;
        this.wordSupplier = wordSupplier;
    }

    public GameResponseDto startNewGame( GameRequestDto gameRequestDto ) {

        Game game = wordSupplier.apply(gameRequestDto.getDifficulty().toLowerCase());
        log.info("===== game details: {}", game.toString());

        String maskedWord = StringUtil.maskWord(game.getGameWord());
        log.info("===== masked word: {}", maskedWord);

        log.info("===== building initial game status");
        // Build initial state of game
        buildInitialGame(game, maskedWord);

        log.info("===== adding players to the current game");
        // Add players
        addPlayers(game, gameRequestDto);

        log.info("===== setting the player to first round");
        // Set player to 1st round
        resetPlayerRound(game);

        log.info("===== setting the starting time of game");
        // Set start time
        game.setStartTime(LocalDateTime.now());

        log.info("===== persisting the game status");
        game = gameRepository.updateGame(game);

        log.info("{}", game);

        return gameMapper.gameToGameResponseDto(game);
    }

    public GameResponseDto guessAttempt( GameRequestDto gameRequestDto
                                                , String gameId ) {

        // Get game by game id
        Game game = gameRepository.getGameById(gameId);
        log.info("===== game details: {}", game.toString());

        log.info("===== checking the current game status");
        // Game is over if status is won or lost already
        checkStatus(game);

        log.info("===== checking the attempts");
        // Check attempts if still can proceed
        checkAttempts(game, gameRequestDto);

        log.info("===== checking if the player's turn");
        // Check if player's turn or not
        checkPlayerTurn(game, gameRequestDto);

        log.info("===== checking the guess if it matches");
        // Proceed with character | string matching
        processGuess(game, gameRequestDto);

        log.info("===== setting the player for next round");
        // Set next player's turn
        int playerRound = game.getCurrentPlayer();

        if(playerRound == game.getPlayer().size()) {
            resetPlayerRound(game);
        } else {
            game.setCurrentPlayer(++playerRound);
        }

        log.info("===== persisting the game status");
        // Overwrite the state of the game with new changes
        game = gameRepository.updateGame(game);

        log.info("{}",game.toString());

        return gameMapper.gameToGameResponseDto(game);
    }

    public GameResponseDto forfeitGame( String id ) {

        // Get game by game id
        Game game = gameRepository.getGameById(id);
        log.info("===== game details: {}", game.toString());

        log.info("===== checking the current game status");
        // Game is over if status is won or lost already
        checkStatus(game);

        log.info("===== setting the game to forfeited");
        // Set game to forfeited
        game.setStatus(GuessGameStatusEnum.FORFEITED);

        log.info("===== setting the end time");
        // Set end time
        game.setEndTime(LocalDateTime.now());

        log.info("===== setting the game's duration");
        // Set game duration
        setGameDuration(game);

        log.info("===== persisting the game status");
        // Overwrite the state of the game with new changes
        game = gameRepository.updateGame(game);

        log.info("{}",game.toString());

        return gameMapper.gameToGameResponseDto(game);
    }

    public GameResponseDto gameDetails( String gameId ) {
        return gameMapper.gameToGameResponseDto(gameRepository.getGameById(gameId));
    }

    public List<GameResponseDto> leaderboards() {

        Comparator<Game> leaderBoardByDurationAsc = (o1, o2) -> Math.toIntExact(o1.getDuration() - o2.getDuration());
        Comparator<Game> leaderBoardByAttemptsDesc = (o1, o2) -> Math.toIntExact(o2.getRemainingAttempts() - o1.getRemainingAttempts());

        List<Game> gameList = gameRepository.getAllGames().values().stream().filter(g -> g.getStatus().equals(GuessGameStatusEnum.WON)).sorted(leaderBoardByDurationAsc.thenComparing(leaderBoardByAttemptsDesc)).toList();

        if(gameList == null || gameList.isEmpty()) {
            throw new NoGamesFoundException("No games recorded yet.");
        }

        return gameMapper.map(gameList);
    }

    private void processGuess(Game game, GameRequestDto gameRequestDto) {

        String guess = gameRequestDto.getGuess().toLowerCase();
        String gameWord = game.getGameWord();
        String maskedWord = game.getMaskedWord().replace(" ","");
        int gameWordLength = gameWord.length();
        int guessLength = guess.length();
        char[] mwArray = maskedWord.toCharArray();

        int pos = 0;
        // Single letter guess attempt
        if (guessLength == 1) {
            for (int i = 0; i < gameWordLength; i++) {
                // Check only from the index position+1 of the last correct guessed letter
                // No need to iterate through whole word
                pos = gameWord.indexOf(guess, pos, gameWordLength);

                // Word does not contain the guess letter, stop iterating
                if(pos < 0) break;

                // Check if guessed letter is already revealed, if so, throw error
                if(!"_".equalsIgnoreCase(String.valueOf(mwArray[pos]))) {
                    throw new AlreadyGuessedException("You already guessed this correct letter");
                }

                // Update the masked word to replace the mask into correct letter
                // based on the game word
                mwArray[pos] = gameWord.charAt(pos);

                // Skip the position of the correct guessed letter
                pos += 1;

            }
            game.setMaskedWord(StringUtil.splitBySpace(String.valueOf(mwArray)));
        }
        // Whole word guess attempt
        else if (guessLength == gameWordLength && guess.equalsIgnoreCase(gameWord)) {
            game.setMaskedWord(StringUtil.splitBySpace(gameWord));
            game.setStatus(GuessGameStatusEnum.WON);
            game.setEndTime(LocalDateTime.now());
            setGameDuration(game);
        }
        // Invalid guess attempt
        else {
            throw new WrongGuessFormatException("You can only guess one (1) letter OR the full word at a time");
        }

    }

    private void checkAttempts(Game game, GameRequestDto gameRequestDto) {

        String guess = gameRequestDto.getGuess().toLowerCase();
        int attempts = game.getRemainingAttempts();

        if(!game.getGameWord().contains(guess)) {
            game.setRemainingAttempts(--attempts);
        }

        if(game.getRemainingAttempts() < 1) {
            game.setStatus(GuessGameStatusEnum.LOST);
            game.setEndTime(LocalDateTime.now());
            setGameDuration(game);
            gameRepository.updateGame(game);
            throw new NoMoreAttemptsException("No More Attempts!");
        }

    }

    private void checkStatus(Game game) {

        if(game.getStatus().equals(GuessGameStatusEnum.WON)
                || game.getStatus().equals(GuessGameStatusEnum.LOST)
                || game.getStatus().equals(GuessGameStatusEnum.FORFEITED) ) {
            throw new GameOverException("This game is already over");
        }

    }

    private void addPlayers(Game game, GameRequestDto gameRequestDto) {
        List<String> players = new ArrayList<>(gameRequestDto.getPlayer());
        gamePlayerRepository.saveGamePlayer(game.getGameId(), gameRequestDto.getPlayer());
        game.setPlayer(players);
    }

    private void buildInitialGame(Game game, String maskedWord) {
        buildInitialGame(game, maskedWord, 6, GuessGameStatusEnum.IN_PROGRESS);
    }

    private void buildInitialGame(Game game, String maskedWord, int attempt, GuessGameStatusEnum statusEnum) {
        game.setMaskedWord(maskedWord);
        game.setRemainingAttempts(attempt);
        game.setStatus(statusEnum);
    }

    private void resetPlayerRound(Game game) {
        game.setCurrentPlayer(1);
    }

    private void checkPlayerTurn(Game game, GameRequestDto gameRequestDto) {
        String curPlayer = getCurrentPlayerByRound(game, game.getCurrentPlayer());
        if(!curPlayer.equalsIgnoreCase(gameRequestDto.getName())) {
            throw new NotPlayerTurnException("It's not this player's turn. Current player should be: " + curPlayer);
        }
    }

    private String getCurrentPlayerByRound(Game game, int round) {
        return game.getPlayer().get(round-1);
    }

    private void setGameDuration(Game game) {
        LocalDateTime start = game.getStartTime();
        LocalDateTime end = game.getEndTime();
        long seconds = Duration.between(start,end).getSeconds();
        game.setDuration(seconds);
    }

}
