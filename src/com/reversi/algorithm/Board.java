package com.reversi.algorithm;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class Board {
	public int[][] boardMatrix = new int[8][8];
	private List<Point> toFlip = new ArrayList<Point>();
	private int[] score = new int[2];
	private final static Point dirArray[] = { new Point(0, -1),
			new Point(0, 1), new Point(-1, 0), new Point(1, 0),
			new Point(-1, -1), new Point(1, -1), new Point(-1, 1),
			new Point(1, 1) };

	public List<Point> getToFlip() {
		return toFlip;
	}

	public int[] getScore() {
		return score;
	}

	public int[][] getBoardMatrix() {
		return boardMatrix;
	}

	public Board() {
		score[0] = 2;
		score[1] = 2;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				boardMatrix[i][j] = 0;

		boardMatrix[3][3] = 1;
		boardMatrix[4][4] = 1;
		boardMatrix[3][4] = -1;
		boardMatrix[4][3] = -1;
		getToFlip().clear();

	}

	public void Score() {
		int first = 0, second = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getBoardMatrix()[i][j] == -1) {
					second++;
				} else if (getBoardMatrix()[i][j] == 1) {
					first++;
				}
			}
		}
		score[0] = first;
		score[1] = second;
	}

	public void move(int player, Point point) {
		{
			int notPlayer = (player == 1) ? -1 : 1;
			int x = point.x;
			int y = point.y;
			for (int i = 0; i < 8; i++) {
				int step = 1;
				boolean check = false;
				Point currentDir = dirArray[i];
				int currX = currentDir.x;
				int currY = currentDir.y;
				if (x + currX >= 0 && y + currY >= 0 && x + currX < 8
						&& y + currY < 8) {
					if (getBoardMatrix()[x + currX][y + currY] == notPlayer) {
						check = true;
					}
					if (check) {
						step = 2;
						while (x + step * currX >= 0 && x + step * currX < 8
								&& y + step * currY >= 0
								&& y + step * currY < 8) {
							if (getBoardMatrix()[x + step * currX][y + step
									* currY] == 0) {
								break;
							}
							if (getBoardMatrix()[x + step * currX][y + step
									* currY] == player) {
								for (int k = 0; k < step; k++) {
									getBoardMatrix()[x + k * currX][y + k
											* currY] = player;
									Point p = new Point(x + k * currX, y + k
											* currY);
									getToFlip().add(p);
								}
								break;
							}
							step++;
						}

					}
				}
			}

		}
		for (int i = 0; i < getToFlip().size(); i++) {
			if (getToFlip().get(i).equals(point)) {
				getToFlip().remove(i);
			}

		}

	}

	public boolean isValidMove(int player, Point point) {
		{

			int notPlayer = (player == 1) ? -1 : 1;
			int x = point.x;
			int y = point.y;

			if (getBoardMatrix()[point.x][point.y] != 0) {
				return false;
			}
			for (int i = 0; i < 8; i++) {
				int step = 1;
				boolean check = false;
				Point currentDir = dirArray[i];
				int currX = currentDir.x;
				int currY = currentDir.y;
				if (x + currX >= 0 && y + currY >= 0 && x + currX < 8
						&& y + currY < 8) {
					if (getBoardMatrix()[x + currX][y + currY] == notPlayer) {
						check = true;
					}
					if (check) {
						step = 2;
						while (x + step * currX >= 0 && x + step * currX < 8
								&& y + step * currY >= 0
								&& y + step * currY < 8) {
							if (getBoardMatrix()[x + step * currX][y + step
									* currY] == 0) {
								break;
							}
							if (getBoardMatrix()[x + step * currX][y + step
									* currY] == player) {
								return true;
							}
							step++;
						}
					}
				}
			}

			return false;
		}
	}

	public List<Point> playerValidMoves(int player) {

		List<Point> validMoves = new ArrayList<Point>();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				Point p = new Point(i, j);
				if (isValidMove(player, p)) {
					validMoves.add(p);
				}
			}

		return validMoves;
	}

	public boolean hasValidMove(int player) {
		List<Point> validMoves = playerValidMoves(player);
		if (validMoves.isEmpty()) {
			return false;
		} else
			return true;
	}

	public boolean isGameOver() {
		if (!hasValidMove(1) && !hasValidMove(-1)) {
			return true;
		} else {
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					if (boardMatrix[i][j] == 0) {
						return false;
					}
				}
		}
		return true;
	}
}
