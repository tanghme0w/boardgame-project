# boardgame-project
A chess game project for oop course 2023.

## 2.3.2
- Fix issues regarding liberty judgement in the Go game. Go game can now function properly.

## 2.3.1
- Add new global config argument: DEFAULT_BOARD_SIZE
- Apply orange color to BoardPanel.
- Minor adjustment on BoardPanel size so that it looks more harmonious.

## 2.3.0
- Add step logic for Gomoku game.
- Server refactor: change the input parameter of gameEnd from identity to chessType.
- Server refactor: make code cleaner for Server.surrender(), Server.stepAt() and Server.abstain().
- Fix bug: error handling null pieces when comparing boards.
- Fix bug: server does not set board.nextChessType when game starts.

## 2.2.0
- Add step logic for Go game. (untested)
- Populate Position class.
- Change history data structure from ArrayList to Stack.
- Add boardScan method to ruleset.
- Remove metadata for pieces, as I noticed it doesn't improve calculation complexity.

## 2.1.3
- Add metadata for pieces. Currently, we introduce two types of metadata: 
  - LIBERTY (for go game)
  - CONNECTION_COUNT (for gomoku game) 

## 2.1.2
- Optimize imports

## 2.1.1
- Fix minor text issues.
- Rename move -> step.
- Remove deprecated files.

## 2.1.0
- Server: add withdraw interface.
- Client: add graphic user interface.