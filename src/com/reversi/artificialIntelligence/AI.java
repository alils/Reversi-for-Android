package com.reversi.artificialIntelligence;

import java.util.List;

import android.graphics.Point;

import com.reversi.algorithm.Board;
import com.reversi.algorithm.Player;

public class AI {

	private Point bestPossibleMove;
	private int maxDepth;

	public AI() {
		bestPossibleMove = null;
		maxDepth = 5;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public Point getBestPossibleMove() {
		return bestPossibleMove;
	}

	private int computerTurn(Board board, Player player, int depth, int alpha,
			int beta) {

		int value = 0;
		if (board.isGameOver() || depth > maxDepth) {
			return boardValue(board, player);
		}

		List<Point> validMoves = board.playerValidMoves(player.getPlayer());
		// for every valid move
		for (Point p : validMoves) {
			Board boardClone = new Board();
			for (int y = 0; y < board.getBoardMatrix().length; y++) {
				for (int x = 0; x < board.getBoardMatrix()[y].length; x++) {
					boardClone.boardMatrix[x][y] = board.getBoardMatrix()[x][y];
				}
			}
			boardClone.move(player.getPlayer(), p);
			value = humanTurn(boardClone, new Player(
					player.getPlayer() == 1 ? -1 : 1,
					player.getPlayerColor() ? false : true), depth + 1, beta,
					alpha);
			if (value > alpha) {
				alpha = value;
				if (depth == 0) {
					bestPossibleMove = p;
				}
			}
			if (alpha >= beta) {
				return alpha;
			}
		}

		return alpha;

	}

	private int humanTurn(Board board, Player player, int depth, int alpha,
			int beta) {
		int value = 0;

		if (board.isGameOver() || depth > maxDepth) {
			return boardValue(board, player);
		}

		List<Point> validMoves = board.playerValidMoves(player.getPlayer());
		for (Point p : validMoves) {
			Board boardClone = new Board();
			for (int y = 0; y < board.getBoardMatrix().length; y++) {
				for (int x = 0; x < board.getBoardMatrix()[y].length; x++) {
					boardClone.boardMatrix[x][y] = board.getBoardMatrix()[x][y];
				}
			}
			boardClone.move(player.getPlayer(), p);
			value = computerTurn(boardClone, new Player(
					player.getPlayer() == 1 ? -1 : 1,
					player.getPlayerColor() ? false : true), depth + 1, beta,
					alpha);

			if (value < alpha) {
				alpha = value;
			}
			if (alpha <= beta) {
				return alpha;
			}
		}
		return alpha;
	}

	private int boardValue(Board board, Player player) {
		int result = 0;
		int[] score = board.getScore();

		if (board.isGameOver()) {
			if (score[1] > score[0]) {
				if (player.getPlayerColor() == false) {
					return 100000;
				} else {
					return -100000;
				}
			}
			if (score[1] > score[0]) {
				if (player.getPlayerColor() == true) {
					return -100000;
				} else {
					return 100000;
				}
			}
		}
		if (player.getPlayerColor() == false) {
			result = result + score[1];
		} else {
			result = result + score[0];
		}

		List<Point> validMoves = board.playerValidMoves(player.getPlayer());
		int mobility = validMoves.size();
		result = result + (mobility * 3);

		Point arrBadTopLeft[] = { new Point(1, 0), new Point(1, 1),
				new Point(0, 1) };

		Point arrBadTopRight[] = { new Point(6, 0), new Point(6, 1),
				new Point(7, 1) };

		Point arrBadDownLeft[] = { new Point(0, 6), new Point(1, 6),
				new Point(1, 7) };
		Point arrBadDownRight[] = { new Point(6, 6), new Point(6, 7),
				new Point(7, 6) };

		for (int i = 0; i < arrBadTopLeft.length; i++) {
			if (board.boardMatrix[0][0] == 0
					&& board.boardMatrix[arrBadTopLeft[i].x][arrBadTopLeft[i].y] == player
							.getPlayer()) {
				result = result - 10;
			}
		}

		for (int i = 0; i < arrBadTopRight.length; i++) {
			if (board.boardMatrix[7][0] == 0
					&& board.boardMatrix[arrBadTopRight[i].x][arrBadTopRight[i].y] == player
							.getPlayer()) {
				result = result - 10;
			}
		}
		for (int i = 0; i < arrBadDownLeft.length; i++) {
			if (board.boardMatrix[0][7] == 0
					&& board.boardMatrix[arrBadDownLeft[i].x][arrBadDownLeft[i].y] == player
							.getPlayer()) {
				result = result - 10;
			}
		}
		for (int i = 0; i < arrBadDownRight.length; i++) {
			if (board.boardMatrix[7][7] == 0
					&& board.boardMatrix[arrBadDownRight[i].x][arrBadDownRight[i].y] == player
							.getPlayer()) {
				result = result - 10;
			}
		}

		Point arrCorners[] = { new Point(0, 0), new Point(7, 7),
				new Point(7, 0), new Point(0, 7) };
		for (int i = 0; i < arrCorners.length; i++) {
			if (board.boardMatrix[arrCorners[i].x][arrCorners[i].y] == player
					.getPlayer()) {
				result = result + 50;
			}
		}

		Point arrSides[] = { new Point(0, 2), new Point(0, 3), new Point(0, 4),
				new Point(0, 5), new Point(7, 2), new Point(7, 3),
				new Point(7, 4), new Point(7, 5), new Point(2, 0),
				new Point(3, 0), new Point(4, 0), new Point(5, 0),
				new Point(2, 7), new Point(3, 7), new Point(4, 7),
				new Point(5, 7) };

		for (int i = 0; i < arrSides.length; i++) {
			if (board.boardMatrix[arrSides[i].x][arrSides[i].y] == player
					.getPlayer()) {
				result = result + 5;
			}
		}

		return result;
	}

	public Point find(Board board, Player player) {
		bestPossibleMove = null;
		computerTurn(board, player, 0, -1000000, +1000000);
		return bestPossibleMove;
	}

}
