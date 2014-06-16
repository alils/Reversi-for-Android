package com.reversi.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.reversi.algorithm.Board;
import com.reversi.algorithm.Player;
import com.reversi.android.R;
import com.reversi.artificialIntelligence.AI;

public class GameActivity extends Activity implements OnClickListener {
	private AI ai = new AI();
	private boolean flipCheck = true;
	private Player currentPlayer = new Player();
	private Player player1 = new Player();
	private Board board;
	private boolean check = false;
	private boolean gameType = true;
	private boolean showValidMoves = true;// true = Human VS. Computer false =
											// Human VS. Human

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				findViewById(getResId("a" + i + j)).setOnClickListener(this);

		// game type
		Bundle b = getIntent().getExtras();
		int value = b.getInt("key");
		if (value == 2) {
			gameType = false;
		} else {
			gameType = true;
		}
		if (gameType) {
			popUpChooseLevel();
			popUpChooseColor();
		} else {
			popUpChooseColor();
		}
	}

	private void popUpChooseLevel() {
		LayoutInflater layout = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View popupView = layout.inflate(R.layout.ly_choose_level, null);
		final PopupWindow popupWindow = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		RadioGroup level = (RadioGroup) popupView
				.findViewById(R.id.radiobtn_choose_level);
		level.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_easy: {
					ai.setMaxDepth(1);
					break;
				}
				case R.id.rbtn_medium: {
					ai.setMaxDepth(3);
					break;
				}
				case R.id.rbtn_hard: {
					ai.setMaxDepth(5);
					break;
				}
				}
			}

		});

		Button next = (Button) popupView.findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindow.dismiss();

			}
		});
		popupView.post(new Runnable() {
			public void run() {
				popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
			}
		});
	}

	private void popUpChooseColor() {

		LayoutInflater layout = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View popupViewColor = layout.inflate(R.layout.ly_choose_color,
				null);
		final PopupWindow popupWindowColor = new PopupWindow(popupViewColor,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		RadioGroup color = (RadioGroup) popupViewColor
				.findViewById(R.id.radiobtn_choose_color);
		color.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_white) {
					player1 = new Player(1, true);
				} else {
					player1 = new Player(1, false);
				}
			}

		});
		CheckBox check_validMoves = (CheckBox) popupViewColor
				.findViewById(R.id.check_validMoves);
		check_validMoves
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked)
							showValidMoves = true;
						else
							showValidMoves = false;

					}
				});
		Button next = (Button) popupViewColor.findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindowColor.dismiss();
				newGame();

			}
		});
		popupViewColor.post(new Runnable() {
			public void run() {
				popupWindowColor.showAtLocation(popupViewColor, Gravity.CENTER,
						0, 0);
			}
		});
	}

	private void newGame() {

		board = new Board();
		try {
			reCreate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		currentPlayer.setPlayer(player1);
		try {
			PrepareToPlay(currentPlayer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		check = true;
		TextView textScore1 = (TextView) findViewById(R.id.turn_1stPlayer);
		textScore1.setText("2");
		TextView textScore2 = (TextView) findViewById(R.id.turn_2ndPlayer);
		textScore2.setText("2");
		ImageView player1Color = (ImageView) findViewById(R.id.color_1stPlayer);
		ImageView player2Color = (ImageView) findViewById(R.id.color_2ndPlayer);
		if (gameType) {
			textScore1.setBackgroundResource(R.drawable.turn_human_true);
			textScore2.setBackgroundResource(R.drawable.turn_android);
		} else {
			textScore1.setBackgroundResource(R.drawable.turn_player1_true);
			textScore2.setBackgroundResource(R.drawable.turn_player2);
		}
		if (player1.getPlayerColor()) {
			player1Color.setBackgroundResource(R.drawable.turn_white);
			player2Color.setBackgroundResource(R.drawable.turn_black);
		} else {
			player1Color.setBackgroundResource(R.drawable.turn_black);
			player2Color.setBackgroundResource(R.drawable.turn_white);
		}

	}

	@Override
	public void onClick(View v) {
		if (check && flipCheck) {

			board.getToFlip().clear();
			String tag = (String) v.getTag();
			Integer i = Integer.parseInt(tag.substring(0, 1));
			Integer j = Integer.parseInt(tag.substring(1));
			Point p = new Point(i, j);

			if (board.isValidMove(currentPlayer.getPlayer(), p)) {
				check = false;
				Play(p);

			}
		}

	}

	private void Play(Point p) {
		setPieces(p.x, p.y, currentPlayer.getPlayerColor() ? R.drawable.white
				: R.drawable.black);

		board.move(currentPlayer.getPlayer(), p);
		try {
			reCreate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void PrepareToPlay(Player player) throws InterruptedException {
		if (!board.isGameOver()) {

			if (board.hasValidMove(player.getPlayer())) {
				currentPlayer.setPlayer(player);
				TextView Turn1stPlayer = (TextView) findViewById(R.id.turn_1stPlayer);
				TextView Turn2ndPlayer = (TextView) findViewById(R.id.turn_2ndPlayer);
				if (currentPlayer.getPlayer() == 1) {

					Turn1stPlayer
							.setBackgroundResource(gameType ? R.drawable.turn_human_true
									: R.drawable.turn_player1_true);
					Turn2ndPlayer
							.setBackgroundResource(gameType ? R.drawable.turn_android
									: R.drawable.turn_player2);
				} else {

					Turn1stPlayer
							.setBackgroundResource(gameType ? R.drawable.turn_human
									: R.drawable.turn_player1);
					Turn2ndPlayer
							.setBackgroundResource(gameType ? R.drawable.turn_android_true
									: R.drawable.turn_player2_true);
				}
			} else {
				Toast.makeText(
						this,
						(player.getPlayerColor() ? "black" : "white")
								+ " player have turn again, because "
								+ (player.getPlayerColor() ? "white" : "black")
								+ " player doesn't have valid moves",
						Toast.LENGTH_LONG).show();
			}

			if (showValidMoves) {
				List<Point> validMoves = new ArrayList<Point>();
				validMoves = board.playerValidMoves(currentPlayer.getPlayer());
				for (Point p : validMoves) {
					findViewById(getResId("a" + p.x + p.y))
							.setBackgroundResource(R.drawable.valid_move);
				}
			}
			NextMove();

		} else {
			GameOver();
		}

	}

	private void NextMove() throws InterruptedException {
		if (gameType && currentPlayer.getPlayer() == -1) {
			board.getToFlip().clear();
			Point p = ai.find(board, currentPlayer);
			Play(p);

		} else
			check = true;

	}

	private void reCreate() throws Exception {
		if (board.getToFlip().isEmpty()) {
			int color1 = player1.getPlayerColor() ? R.drawable.white
					: R.drawable.black;
			int color2 = player1.getPlayerColor() ? R.drawable.black
					: R.drawable.white;

			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board.getBoardMatrix()[i][j] == 1) {
						setPieces(i, j, color1);
					} else if (board.getBoardMatrix()[i][j] == -1) {
						setPieces(i, j, color2);
					} else
						setPieces(i, j, R.drawable.img_bckg);
				}
			}

		} else {

			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board.getBoardMatrix()[i][j] == 0) {
						setPieces(i, j, R.drawable.img_bckg);
					}
				}
			}
			StartAnimation();

		}

		board.Score();
		TextView textScore1 = (TextView) findViewById(R.id.turn_1stPlayer);
		textScore1.setText("" + board.getScore()[0]);
		TextView textScore2 = (TextView) findViewById(R.id.turn_2ndPlayer);
		textScore2.setText("" + board.getScore()[1]);
	}

	private void setPieces(int i, int j, int id) {

		findViewById(getResId("a" + i + j)).setBackgroundResource(id);
	}

	public int getResId(String variableName) {
		int id = getResources().getIdentifier(variableName, "id",
				getPackageName());
		return id;
	}

	Timer animationTimer;
	int img;

	public void StartAnimation() {
		check = false;
		animationTimer = new Timer();
		animationTimer.schedule(new secondTask(), 0, 20);
		img = 1;
	}

	class secondTask extends TimerTask {

		@Override
		public void run() {
			GameActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (img <= 10) {
						int backgImg;
						if (board.boardMatrix[board.getToFlip().get(0).x][board
								.getToFlip().get(0).y] == 1) {
							backgImg = GameActivity.this
									.getResources()
									.getIdentifier(
											(player1.getPlayerColor() ? "img_to_white"
													: "img_to_black")
													+ img, "drawable",
											GameActivity.this.getPackageName());
						} else {
							backgImg = GameActivity.this
									.getResources()
									.getIdentifier(
											(player1.getPlayerColor() ? "img_to_black"
													: "img_to_white")
													+ img, "drawable",
											GameActivity.this.getPackageName());

						}
						for (Point p : board.getToFlip()) {
							findViewById(getResId("a" + p.x + p.y))
									.setBackgroundResource(backgImg);
						}
						img++;
					} else {
						timer_Cancel();
					}
				}
			});
		}
	};

	void timer_Cancel() {
		animationTimer.cancel();
		animationTimer.purge();
		flipCheck = true;
		try {
			PrepareToPlay(currentPlayer.NotPlayer());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void Btn_Restart(View v) {
		AlertDialog.Builder restart = new AlertDialog.Builder(this);
		restart.setMessage("Are you sure you want to restart game?");
		restart.setCancelable(true);
		restart.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				newGame();
			}
		});
		restart.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = restart.create();
		alert.show();

	}

	public void Btn_Menu(View v) {
		AlertDialog.Builder menu = new AlertDialog.Builder(this);
		menu.setMessage("Are you sure you want go to menu ? All game progress will be losed.");
		menu.setCancelable(true);
		menu.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				showValidMoves = true;
				Intent intent = new Intent(GameActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		menu.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert = menu.create();
		alert.show();

	}

	private void GameOver() {
		LayoutInflater layout = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupView = layout.inflate(R.layout.ly_game_over, null);
		final PopupWindow popupWindow = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		Button newGame = (Button) popupView.findViewById(R.id.btn_NewGame);
		newGame.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				newGame();
			}
		});

		Button menu = (Button) popupView.findViewById(R.id.btn_menu);
		menu.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				Intent intent = new Intent(GameActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();

			}
		});
		TextView txtWiner = (TextView) popupView.findViewById(R.id.txt_Winners);

		if (board.getScore()[0] > board.getScore()[1]) {
			txtWiner.setText(player1.getPlayerColor() ? "White Wins!"
					: "Black Wins!");
		} else {
			if (board.getScore()[0] < board.getScore()[1]) {
				txtWiner.setText(player1.getPlayerColor() ? "Black Wins!"
						: "White Wins!");
			} else {
				txtWiner.setText("Draw!!!!");
			}
		}
		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

	}
}
