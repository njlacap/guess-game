# CodeWords - Word Guessing Game API

A fun JSON API game built with Spring Boot, where players guess hidden words. This challenge is designed for Java developers to demonstrate their skills in API design, state management, and clean coding.

---

## 🎮 What is CodeWords?

**CodeWords** is a simple, turn-based word-guessing game played entirely through a RESTful JSON API. Think of it as a mashup between **Hangman** and **Wordle**, but without the graphics—just clean, logical backend interaction.

### 🧩 How the Game Works

1. **Game Start**
    - When a new game is started, the backend randomly selects a word (e.g., "apple").
    - The player sees a **masked version** of the word: `_ _ _ _ _` (one underscore per letter).
    - The player has a limited number of attempts up to 6 to guess the word.

2. **Making Guesses**
    - Players can guess a **single letter** (e.g., "a") or try to guess the **entire word** (e.g., "apple").
    - Each incorrect guess reduces the number of remaining attempts by 1.
    - Correct letter guesses reveal that letter in its correct position(s) within the masked word.
    - If the player correctly guesses the entire word, the game ends immediately with a **WIN**.
    - If the player loses all attempts, the game ends immediately with a **LOST**.
    - If a player forfeits, the game ends immediately with a **FORFEITED**.

3. **Game End Conditions**
    - **Win:** The player uncovers the entire word (via letter guesses or full word guess).
    - **Lose:** The player runs out of attempts before guessing the full word.

4. **Game Status**
    - The current status of the game can be:
        - `IN_PROGRESS`
        - `WON`
        - `LOST`
        - `NOT_STARTED`
        - `FORFEITED`
5. **Game Difficulty**
    - The difficulty levels are:
        - `easy` - 5-letter word
        - `medium` - 8-letter word
        - `hard` - 12-letter word
6. **Game Mode**
    - Mode can be selected depending on the API URI


### 🧠 Example Game Flow

- Start a new game:
    - Server picks "apple" → masked: `_ _ _ _ _`, attempts: 6
- Guess "a" → masked: `a _ _ _ _`, attempts: 6
- Guess "e" → masked: `a _ _ _ e`, attempts: 6
- Guess "i" → incorrect, attempts: 5
- Guess "apple" → correct! Game ends: `WON`

---

## 🚀 How to Run

```bash
mvn spring-boot:run -Pdev
```

The application will start on `http://localhost:8001`.

All request should have the following headers:
- Header ` "X-Request-ID: [0-9]{1,5}}"`

      "X-Request-ID: 12452"

- Header `Content-Type: application/json`

---

## 🧪 API Endpoints

### 🎮 Start a New Game - Single Player
**POST** `/game/single`

**Body:**
```json
{
  "difficulty": "easy",
  "player": ["Player_1"]
}
```

**Response:**
```json
{
  "gameId": "e4988ee6-0da6-4904-95d8-c274239dae32",
  "maskedWord": "_ _ _ _ _",
  "remainingAttempts": 6,
  "player": ["Player_1"],
  "currentPlayer": 1,
  "startTime": "2025-04-26T04:29:55.0518018"
}
```

### 🎮 Start a New Game - Multi Player
**POST** `/game/multi`

**Allowed Values:**
```
   (String) difficulty: easy | medium | hard]
   Example: "difficulty": "medium"
            
   (String[]) player: ^[A-Za-z]+_[1-9]{1,3}+$
   Multiplayer - minimum of 2
   Example: "player": ["Player_1","Player_2","Player_3","Player_4"]
   
   Singleplayer - minimum of 1, maximum of 1
   Example: "player": ["Player_1"]
```

**Body:**
```json
{
  "difficulty": "hard",
  "player": ["Player_1","Player_2","Player_3","Player_4"]
}
```

**Response:**
```json
{
  "gameId": "e4988ee6-0da6-4904-95d8-c274239dae32",
  "maskedWord": "_ _ _ _ _ _ _ _ _ _",
  "remainingAttempts": 6,
  "player": ["Player_1","Player_2","Player_3","Player_4"],
  "currentPlayer": 1,
  "startTime": "2025-04-26T04:29:55.0518018"
}
```

---

### 🔡 Make a Guess
**PUT** `/game/{gameId}/guess`

**Allowed Values:**
```
   (String) name: [Name of player set during the new game]
   Example: "name": "Player_1"
            
   (String) guess: [Single letter OR Whole word]
   Single letter
   Example: "guess": "a"
   
   Whole word
   Example: "guess": "apple"
```

**Body:**
```json
{
  "name": "Player_1",
  "guess": "t"
}
```

**Response:**
```json
{
  "gameId": "53372baf-5092-4c9d-ad8f-efff42ab0212",
  "maskedWord": "t _ _ _ _",
  "remainingAttempts": 6,
  "status": "IN_PROGRESS", 
  "player":["Player_1"],
  "currentPlayer": 1,
  "startTime": "2025-04-26T05:21:47.0151642",
  "endTime":null,
  "duration":null
}
```

**Body:**
```json
{
  "name": "Player_1",
  "guess": "there"
}
```

**Response:**
```json
{
   "gameId": "53372baf-5092-4c9d-ad8f-efff42ab0212",
   "maskedWord": "t h e r e",
   "remainingAttempts": 6,
   "status": "WON",
   "player": ["Player_1"],
   "currentPlayer": 1,
   "startTime": "2025-04-26T05:21:47.0151642",
   "endTime": "2025-04-26T05:24:34.098288",
   "duration": 167
}
```

---

### 📄 Get Game State
**GET** `/game/{gameId}`

**Response:**
```json
{
   "maskedWord": "t h e r e",
   "remainingAttempts": 6,
   "status": "WON",
   "player": ["Player_1"],
   "currentPlayer": 1,
   "startTime": "2025-04-26T05:21:47.0151642",
   "endTime": "2025-04-26T05:24:34.098288",
   "duration": 167
}
```

---

### 📄 Get Leaderboard
**GET** `/game/leaderboards`

**Response:** Sorted by duration ASC, remainingAttempts DESC
```json
[
   {
      "gameWord":"organization",
      "maskedWord":"o r g a n i z a t i o n",
      "remainingAttempts":6,
      "status":"WON",
      "player":["Player_1"],
      "currentPlayer":1,
      "startTime":"2025-04-26T05:31:43.3298148",
      "endTime":"2025-04-26T05:32:06.9755103",
      "duration":23
   },
   {
      "gameWord":"material",
      "maskedWord":"m a t e r i a l",
      "remainingAttempts":6,
      "status":"WON",
      "player":["Player_1","Player_2","Player_3","Player_4"],
      "currentPlayer":2,"startTime":"2025-04-26T05:30:58.8364088",
      "endTime":"2025-04-26T05:31:28.0221748",
      "duration":29
   },
   {
      "gameWord":"there",
      "maskedWord":"t h e r e",
      "remainingAttempts":6,
      "status":"WON",
      "player":["Player_1"],
      "currentPlayer":1,
      "startTime":"2025-04-26T05:21:47.0151642",
      "endTime":"2025-04-26T05:24:34.098288",
      "duration":167
   }
]
```

---

### 🎮 Forfeit a game
**PUT** `/game/{gameId}/forfeit`

**Body:**
```json
{
  "forfeit":"true"
}
```

**Response:**
```json
{
   "maskedWord":"_ _ _ _ _ _ _ _ _ _ _ _",
   "remainingAttempts":6,
   "status":"FORFEITED",
   "player":["Player_1"],
   "currentPlayer":1,
   "startTime":"2025-04-26T05:37:54.8561219",
   "endTime":"2025-04-26T05:38:07.7373972",
   "duration":12
}
```

---

## 🎯 Game Rules
- Each game starts with a hidden word (e.g., "apple").
- The user can guess a **letter** or the **full word**.
- A wrong guess reduces the remaining attempts.
- Game ends when the word is guessed, attempts run out, or forfeited.

---

## 🔧 Tech Stack
- Java 21
- Spring Boot
- Maven
- In-memory storage (Memcached)

---

## ✨ Has Bonus Features
- Random word generation
- Forfeit a game
- Leaderboard support
- Multi-player support
- Adjustable difficulty

---

## 📦 GitHub
- GitHub link: https://github.com/njlacap/guess-game

---

## 🧊 Example curl Commands
```bash

curl -X GET -H "Content-Type: application/json" -H "X-Request-ID: 12345" http://localhost:8001/game/18f19d44-b061-4752-85ff-f2829e038f10

curl -X GET -H "Content-Type: application/json" -H "X-Request-ID: 12345" http://localhost:8001/game/leaderboards 

curl -X POST -H "Content-Type: application/json" -H "X-Request-ID: 12345" -d "{\"difficulty\":\"easy\", \"player\": [\"Player_1\",\"Player_2\",\"Player_3\",\"Player_4\"] }" http://localhost:8001/game/multi

curl -X POST -H "Content-Type: application/json" -H "X-Request-ID: 12345" -d "{\"difficulty\":\"medium\", \"player\": [\"Player_1\"]}" http://localhost:8001/game/single

curl -X POST -H "Content-Type: application/json" -H "X-Request-ID: 12345" -d "{\"difficulty\":\"hard\", \"player\": [\"Player_1\",\"Player_2\",\"Player_3\",\"Player_4\"] }" http://localhost:8001/game/multi

curl -i -X PUT -H "Content-Type: application/json" -H "X-Request-ID: 12345" -d "{\"name\":\"Player_1\",\"guess\":\"conservative\"}" http://localhost:8001/game/60a3cd00-225f-49f5-8572-6fe56484fe8e/guess

curl -i -X PUT -H "Content-Type: application/json" -H "X-Request-ID: 12345" -d "{\"name\":\"Player_1\",\"guess\":\"a\"}" http://localhost:8001/game/63ec8a07-4ea4-45dd-9dee-f40bc0b1acbd/guess

curl -i -X PUT -H "Content-Type: application/json" -H "X-Request-ID: 12345" -d "{\"forfeit\":\"false\"}" http://localhost:8001/game/18f19d44-b061-4752-85ff-f2829e038f10/forfeit
```
