interface Game {
	public static final int INVALID = 0, CONTINUE = 1, DONE = 2, PLAYER1 = 3, PLAYER2 = 4, TIE = 5, WIN = 6, LOSS = 7;
	
	public int move(int move);
	
	public String prompt();
	
	public bool isValidInput();
}
