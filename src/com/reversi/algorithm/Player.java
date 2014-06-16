package com.reversi.algorithm;

public class Player {

	
	private int player;

	/**
	 * true is a white color, false is a black color
	 */
	private boolean playerColor;
	private String playerName;

	public Player() {
		player =1;
		playerColor = true;
	}
	public Player(int p, boolean c) {
		player = p;
		playerColor = c;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}
	public void setPlayer(Player player) {
		this.player = player.getPlayer();
		this.playerColor=player.getPlayerColor();
	}

	public boolean getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(boolean playerColor) {
		this.playerColor = playerColor;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public Player NotPlayer(){
		return new Player (player==1?-1:1, playerColor?false:true);
	}

}
