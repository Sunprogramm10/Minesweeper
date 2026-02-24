package com.javarush.task.jdk13.task53.task5302;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private int countFlags;
    private int countClosedTiles = SIDE * SIDE;
    private int score;
    private boolean isGameStopped;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject thisGo = gameField[y][x];
                if (!thisGo.isMine) {
                    List<GameObject> thisNeighbors = getNeighbors(thisGo);
                    for (GameObject thisNMines : thisNeighbors) {
                        if (thisNMines.isMine) {
                            thisGo.countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.WHITE);
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void openTile(int x, int y) {
        GameObject mineOnIt = gameField[y][x];
        if (mineOnIt.isOpen) {
            return;
        }
        if (mineOnIt.isFlag) {
            return;
        }
        mineOnIt.isOpen = true;
        countClosedTiles--;
        setCellColor(x, y, Color.GREEN);
        if (isGameStopped == true) {
            return;
        }
        if (mineOnIt.isOpen && !mineOnIt.isMine) {
            score += 5;
            setScore(score);
        }
        if (mineOnIt.isMine) {
            setCellValue(x, y, MINE);
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else if (!mineOnIt.isMine && mineOnIt.countMineNeighbors == 0) {
            setCellValue(x, y, "");
            List<GameObject> closeNeighbors = getNeighbors(mineOnIt);
            for (var cleanNeighbors : closeNeighbors) {
                if (!cleanNeighbors.isOpen) {
                    openTile(cleanNeighbors.x, cleanNeighbors.y);

                }
            }
        } else if (!mineOnIt.isMine && mineOnIt.countMineNeighbors > 0) {
            setCellNumber(x, y, mineOnIt.countMineNeighbors);
        }
        if (countClosedTiles == countMinesOnField && !mineOnIt.isMine) {
            win();
        }
    }

    private void markTile(int x, int y) {
        GameObject flag = gameField[y][x];
        if (isGameStopped == true) {
            return;
        }
        if (!flag.isOpen) {
            if (countFlags == 0 && flag.isFlag == false) {

            } else if (countFlags > 0 && flag.isFlag == false) {
                flag.isFlag = true;
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.YELLOW);
            } else if (countFlags > 0 && flag.isFlag == true) {
                flag.isFlag = false;
                countFlags++;
                setCellValue(x, y, "");
                setCellColor(x, y, Color.WHITE);
            }
        }

    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "УРА!!! ВЫ ВЫИГРАЛИ!", Color.RED, 15);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Таaaa-daaa!", Color.RED, 20);
    }

    private void restart() {
        countClosedTiles = SIDE * SIDE;
        score = 0;
        countMinesOnField = 0;
        isGameStopped = false;
        setScore(score);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) {
            restart();
            return;
        }
        openTile(x, y);
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }
}