public class RPS implements Game {
	private static final String[] options = {"ROCK", "PAPER", "SCISSORS"};
	
	private static final int ROCK = 0;
	private static final int PAPER = 1;
	private static final int SCISSORS = 2;
	
	private int p1;
	
	public RPS() {
		p1 = -1;
	}
	
	public int move(int move) {
		if (p1 == -1) {
			p1 = move;
			return Game.DONE;
		}
		else {
			if (p1 == move) return Game.TIE;
			if ((p1 == ROCK && move == SCISSORS) || (p1 == SCISSORS && move == PAPER) || (p1 == PAPER && move == ROCK)) return Game.PLAYER1;
			else return Game.PLAYER2;
		}
	}
	
	public boolean isValidInput(int move) {
	  return move >= 0 && move <= 2;
	}
	
	public String prompt() {
		return GameStrategy.toMenu(options);
	}
}
