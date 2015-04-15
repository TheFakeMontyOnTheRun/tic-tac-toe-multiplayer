package br.odb.multiplayer.model.spacedames;

import java.util.ArrayList;

import br.odb.utils.math.Vec2;

//model
public class GameBoard {

	public enum Team {
		BLACK("Indigo Widows"), WHITE("Blood Geishas");

		Team(String nickname) {
			this.nickname = nickname;
		}

		@Override
		public String toString() {
			return nickname;
		}

		public final String nickname;
	};

	public enum RuleSet {
		PTBR, INTL
	}

	public static final int TABLESLOTS = 8;

	public final Piece[][] square = new Piece[TABLESLOTS][];
	int[] remainingPieces;
	int[] score;
	int[] moves;
	final public Vec2 lastTouch = new Vec2();
	public final ArrayList<Vec2> toDie = new ArrayList<Vec2>();
	public final ArrayList<Vec2> promotions = new ArrayList<Vec2>();
	public Piece selectedPiece;
	public Team currentTeamPlaying;
	int killsInThisTurn;
	public PieceMovement lastMove;

	private Team winner;

	@Override
	public String toString() {
		String buffer = "";
		Piece piece;
		for (int c = 0; c < TABLESLOTS; ++c) {
			for (int d = 0; d < TABLESLOTS; ++d) {
				piece = square[d][c];
				if (piece != null) {

					buffer += ( piece.team.ordinal() + 1 );
				} else {
					buffer += "0";
				}
			}
		}

		return buffer;
	}

	public int getKillsInTurn() {
		return killsInThisTurn;
	}

	public GameBoard() {
		
		this.currentTeamPlaying = Team.BLACK;

		remainingPieces = new int[2];
		score = new int[2];
		moves = new int[2];

		for (int c = 0; c < TABLESLOTS; ++c) {
			square[c] = new Piece[TABLESLOTS];
		}

		int offset = 0;

		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < TABLESLOTS; column += 2) {

				offset = ((column + row) % 2 == 0) ? 0 : 1;
				addNewPiece(column + offset, 0 + row, Team.WHITE);
				offset = (offset == 1) ? 0 : 1;
				addNewPiece(column + offset, 5 + row, Team.BLACK);
			}
		}
	}

	private Piece addNewPiece(int x, int y, Team team) {

		Piece piece = new Piece();
		piece.team = team;
		square[x][y] = piece;
		++remainingPieces[team.ordinal()];

		return piece;
	}

	public void trySelectingPieceIgnoringTeams(Vec2 newTouch) {
		lastTouch.set(newTouch);
		int x = (int) (lastTouch.x);
		int y = (int) (lastTouch.y);

		if (!isValidPosition(x, y)) {
			selectedPiece = null;
			return;
		}

		selectedPiece = square[x][y];
		
	}
	
	public void trySelectingPiece(Vec2 newTouch) {

		synchronized (this) {

			trySelectingPieceIgnoringTeams(newTouch);

			if (selectedPiece != null && selectedPiece.team != currentTeamPlaying) {
				selectedPiece = null;
			}
		}
	}

	public boolean isPossibleMovement(Vec2 from, Vec2 to) {

		int x0 = (int) (from.x);
		int y0 = (int) (from.y);
		int x1 = (int) (to.x);
		int y1 = (int) (to.y);
		boolean possible = false;

		if (!isValidPosition(x1, y1)) {
			return false;
		}

		if (square[x1][y1] != null) {
			return false;
		}

		if (selectedPiece != null) {

			if (selectedPiece.team == GameBoard.Team.WHITE) {
				possible = possible || (((x0 - 1) == x1) && ((y0 + 1) == y1))
						|| (((x0 + 1) == x1) && ((y0 + 1) == y1));
			}

			if (selectedPiece.team == GameBoard.Team.BLACK) {
				possible = possible || (((x0 - 1) == x1) && ((y0 - 1) == y1))
						|| (((x0 + 1) == x1) && ((y0 - 1) == y1));
			}

			if (selectedPiece.piled) {
				possible = possible || ((x1 - x0) == (y1 - y0))
						|| (-(x1 - x0) == (y1 - y0));
			}
		}

		possible = possible || isKillPossible(from, to);

		return possible;
	}

	public PieceMovement move(Vec2 from, Vec2 to) {

		int x0 = (int) (from.x);
		int y0 = (int) (from.y);
		int x1 = (int) (to.x);
		int y1 = (int) (to.y);

		if (selectedPiece != null) {

			if (isKillPossible(from, to)) {
				square[x0][y0] = null;
				square[x1][y1] = selectedPiece;
				kill(x0 + ((x1 - x0) / 2), y0 + ((y1 - y0) / 2));
			} else {
				killsInThisTurn = 0;
				square[x0][y0] = null;
				square[x1][y1] = selectedPiece;
			}
			if ((!selectedPiece.piled)
					&& ((y1 == (TABLESLOTS - 1) && selectedPiece.team == Team.WHITE) || (y1 == 0 && selectedPiece.team == Team.BLACK))) {
				selectedPiece.piled = true;
				promotions.add(new Vec2(x1, y1));
			}
		}
		lastTouch.set(to);
		++moves[ selectedPiece.team.ordinal() ];
		PieceMovement movement = new PieceMovement();
		movement.from.set(x0, y0);
		movement.to.set(x1, y1);
		movement.piece = selectedPiece;
		lastMove = movement;

		return movement;
	}

	private void kill(int x, int y) {
		++killsInThisTurn;
		score[ selectedPiece.team.ordinal() ] += killsInThisTurn;
		--remainingPieces[getTeamAt(x, y).ordinal()];
		toDie.add(new Vec2(x, y));
		square[x][y] = null;
	}

	public boolean isKillPossible(Vec2 from, Vec2 to) {

		int x0 = (int) (from.x);
		int y0 = (int) (from.y);
		int x1 = (int) (to.x);
		int y1 = (int) (to.y);
		boolean possible = false;

		if (!isValidPosition(x1, y1)) {
			return false;
		}

		if (square[x1][y1] != null) {
			return false;
		}
		possible = possible
				|| (((x0 - 2) == x1) && ((y0 + 2) == y1)
						&& getTeamAt(x0 - 1, y0 + 1) != null && getTeamAt(
						x0 - 1, y0 + 1) != selectedPiece.team)
				|| (((x0 + 2) == x1) && ((y0 + 2) == y1)
						&& getTeamAt(x0 + 1, y0 + 1) != null && getTeamAt(
						x0 + 1, y0 + 1) != selectedPiece.team);

		possible = possible
				|| (((x0 - 2) == x1) && ((y0 - 2) == y1)
						&& getTeamAt(x0 - 1, y0 - 1) != null && getTeamAt(
						x0 - 1, y0 - 1) != selectedPiece.team)
				|| (((x0 + 2) == x1) && ((y0 - 2) == y1)
						&& getTeamAt(x0 + 1, y0 - 1) != null && getTeamAt(
						x0 + 1, y0 - 1) != selectedPiece.team);

		return possible;
	}

	public static boolean isValidPosition(int x, int y) {
		return (x >= 0) && (y >= 0) && (x < TABLESLOTS) && (y < TABLESLOTS)
				&& ((x + y) % 2 == 0);
	}

	public void nextTurn() {

		if (currentTeamPlaying == Team.WHITE) {
			currentTeamPlaying = Team.BLACK;
		} else {
			currentTeamPlaying = Team.WHITE;
		}

		selectedPiece = null;
		killsInThisTurn = 0;
	}

	public Team getTeamAt(int x, int y) {

		if (!isValidPosition(x, y) || square[x][y] == null) {
			return null;
		} else {

			return square[x][y].team;
		}
	}

	public Team getWinner() {

		if (winner != null) {
			return winner;
		}

		if (remainingPieces[Team.WHITE.ordinal()] == 0) {
			return Team.BLACK;
		}

		if (remainingPieces[Team.BLACK.ordinal()] == 0) {
			return Team.WHITE;
		}

		return null;
	}

	public void fromString(String token) {

		int currChar = 0;
		Piece piece;
		char remotePiece;

		for (int c = 0; c < TABLESLOTS; ++c) {
			for (int d = 0; d < TABLESLOTS; ++d) {

				remotePiece = token.charAt(currChar);

				if (remotePiece == '0') {

					piece = null;
				} else {
					piece = new Piece();
					if (remotePiece == '1') {

						piece.team = GameBoard.Team.WHITE;
					} else {
						piece.team = GameBoard.Team.BLACK;
					}
				}

				square[d][c] = piece;
				++currChar;
			}
		}
	}

	public PieceMovement goForKill(Team team) {

		PieceMovement movement = new PieceMovement();

		for (int y = 0; y < GameBoard.TABLESLOTS; ++y) {
			for (int x = 0; x < GameBoard.TABLESLOTS; ++x) {

				if (getTeamAt(x, y) == team) {

					selectedPiece = square[x][y];

					movement.from.set(x, y);

					movement.to.set((x - 2), (y - 2));

					if (isKillPossible(movement.from, movement.to)) {
						return move(movement.from, movement.to);
					}

					movement.to.set((x + 2), (y - 2));

					if (isKillPossible(movement.from, movement.to)) {
						return move(movement.from, movement.to);
					}

					movement.to.set((x - 2), (y + 2));

					if (isKillPossible(movement.from, movement.to)) {
						return move(movement.from, movement.to);
					}

					movement.to.set((x + 2), (y + 2));

					if (isKillPossible(movement.from, movement.to)) {
						return move(movement.from, movement.to);
					}
				}
			}
		}

		return null;
	}

	public static Team otherTeam(Team team) {
		if (team == Team.WHITE) {
			return Team.BLACK;
		} else {
			return Team.WHITE;
		}
	}

	public Team getCurrentTeam() {
		return currentTeamPlaying;
	}

	public void declareWinner(Team otherTeam) {
		winner = otherTeam;
	}

	public int getScore(Team team) {
		return score[ team.ordinal() ];
	}
	
	public int getMoves(Team team) {
		return moves[ team.ordinal() ];
	}
}
