# boardgame-project
A chess game project for oop course 2023.

## TODO

- Add Reversi (Othello) ruleset.
- Add replay feature.
- Add account feature.
- Add AI feature.
- Cancel handler classes, define action listeners within their corresponding components.

## 2.8.1

- Add boardLegit domain to BoardScanResult.
- Add Direction enum type. Add nextPosition() method to Position class.
- Add init() method to Ruleset interface.
  - Game will call Ruleset.init() in its constructor.
- Unify naming: Board.getChessType -> getStoneColor.
- Will judge if BoardHistory before poping for withdraw.

## 2.8.0
- Add both side withdraw & surrender feature.
- Fix bugs about gomoku boardScan function.

## 2.7.1
- Add string() method to ChessType, more convenient for display.

## 2.7.0
- Add load/save game feature. (without checking legitimacy)

## 2.6.1
- Remove Step class as it is never used.

## 2.6.0
- Add pop-up window components.

## 2.5.0
- Add random game simulation feature.

## 2.4.0
- Delete stepHistory attribute.
- Add withdraw feature.

## 2.3.3
- Add step count rendering logic. User can configure whether to show step count or not.

## 2.3.2
- Fix issues regarding liberty judgement in the Go game. Go game can now function properly.

## 2.3.1
- Add new global config argument: DEFAULT_BOARD_SIZE
- Apply orange color to BoardPanel.
- Minor adjustment on BoardPanel size so that it looks more harmonious.

## 2.3.0
- Add step logic for Gomoku game.
- Server refactor: change the input parameter of gameEnd from identity to stoneColor.
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