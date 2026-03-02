package com.javarush.task.jdk13.task53.task5303;

import com.javarush.engine.cell.*;


import java.util.ArrayList;
import java.util.List;

public class Snake {
    public int x;
    public int y;
    public List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
        GameObject snakePart1 = new GameObject(x, y);
        GameObject snakePart2 = new GameObject(x + 1, y);
        GameObject snakePart3 = new GameObject(x + 2, y);
        snakeParts.add(snakePart1);
        snakeParts.add(snakePart2);
        snakeParts.add(snakePart3);
    }

    public void draw(Game game) {
        for (int i = 0; i < snakeParts.size(); i++) {
            if (i == 0 && isAlive == false) {
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.RED, 75);
            } else if (i == 0 && isAlive == true) {
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.GREEN, 75);
            } else if (i > 0 && isAlive == false) {
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.RED, 75);
            } else if (i > 0 && isAlive == true) {
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.GREEN, 75);
            }
        }
    }


    public void setDirection(Direction direction) {
        if ((this.direction == Direction.LEFT || this.direction == Direction.RIGHT) && snakeParts.get(0).x == snakeParts.get(1).x) {
            return;
        }
        if ((this.direction == Direction.UP || this.direction == Direction.DOWN) && snakeParts.get(0).y == snakeParts.get(1).y) {
            return;
        }

        if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return;
        } else if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {
            return;
        } else if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return;
        } else if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return;
        }

        this.direction = direction;
    }

    public void move(Apple apple) {
        int size = snakeParts.size();
        GameObject newHead = createNewHead();

        if (newHead.x >= SnakeGame.WIDTH || newHead.y >= SnakeGame.HEIGHT || newHead.x < 0 || newHead.y < 0) {
            isAlive = false;
            return;
        }
        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }
        snakeParts.add(0, newHead);
        if (newHead.x == apple.x && newHead.y == apple.y) {
            apple.isAlive = false;
        } else {
            removeTail();
        }
        while (snakeParts.size() != size) {
            if (size > snakeParts.size()) {
                GameObject snailBody = new GameObject(snakeParts.get(snakeParts.size() - 1).x + 1, snakeParts.get(snakeParts.size() - 1).y);
                snakeParts.add(snailBody);
            } else if (size < snakeParts.size()) {
                removeTail();
            }
        }
    }

    public GameObject createNewHead() {
        GameObject headMove = new GameObject(0, 0);
        if (direction == Direction.DOWN) {
            headMove = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1);
            return headMove;
        }
        if (direction == Direction.RIGHT) {
            headMove = new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y);
            return headMove;
        }
        if (direction == Direction.UP) {
            headMove = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1);
            return headMove;
        }
        if (direction == Direction.LEFT) {
            headMove = new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y);
            return headMove;
        }
        return headMove;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject isBusy) {
        boolean isBusyTile = false;
        for (int i = 0; i < snakeParts.size(); i++) {
            if (isBusy.x == snakeParts.get(i).x && isBusy.y == snakeParts.get(i).y) {
                return true;

            } else {
                isBusyTile = false;
            }
        }
        return isBusyTile;
    }
    public int getLength() {
        return snakeParts.size();
    }
}

