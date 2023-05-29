interface Game {
	public static final int INVALID = 0;
	public static final int CONTINUE = 1;
	public static final int DONE = 2;
	public static final int PLAYER1 = 3;
	public static final int PLAYER2 = 4;
	public static final int TIE = 5;
	public static final int WIN = 6;
	public static final int LOSS = 7;
	
	public int move(int move);
	
	public String prompt();
	
	public int getMinInput();
	
	public int getMaxInput();
}
