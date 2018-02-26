package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Game extends Observable{

    private Tile[][] board;
    private boolean[][] playsAllowed;
    private Player playerBlack;
    private Player playerWhite;
    private Tile playerTurn;

    public Game(Player playerBlack, Player playerWhite) {
        super();
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
        this.playerTurn = Tile.BLACK;
        // Init board
        board = new Tile[8][8];
        for (int x=0; x<board[0].length; x++) {
            for (int y=0; y<board.length; y++) {
                board[x][y] = Tile.EMPTY;
            }
        }
        board[3][3] = Tile.WHITE;
        board[3][4] = Tile.BLACK;
        board[4][3] = Tile.BLACK;
        board[4][4] = Tile.WHITE;

        /*board[3][3] = Tile.WHITE;
        board[3][4] = Tile.WHITE;
        board[4][3] = Tile.BLACK;
        board[4][4] = Tile.BLACK;
        board[5][4] = Tile.BLACK;
        board[3][5] = Tile.WHITE;*/

        //Init plays allowed
        this.playsAllowed = new boolean[8][8];
        for (int x = 0; x< playsAllowed[0].length; x++) {
            for (int y = 0; y< playsAllowed.length; y++) {
                playsAllowed[x][y] = false;
            }
        }
        calculatePlaysAllowed();
    }

    private void calculatePlaysAllowed() {
        for (int x=0; x<playsAllowed[0].length; x++) {
            for (int y=0; y<playsAllowed.length; y++) {
                playsAllowed[x][y] = canPlayAt(x,y);
            }
        }
    }

    private boolean canPlayAt(int x, int y) {
        if( exists(x,y) && board[x][y] == Tile.EMPTY ) {
            Tile ennemyTile = Tile.opposite(playerTurn);

            List<Direction> canTestDirection = new ArrayList<>();
            for (Direction d : Direction.values()) {
                if (exists(x + d.getDx(), y + d.getDy()) && board[x + d.getDx()][y + d.getDy()] == ennemyTile) {
                    canTestDirection.add( d );
                }
            }

            for (Direction d : canTestDirection ) {
                //System.out.println(d.name());
                for(int i=2; i<=7; i++) {
                    int dx = d.getDx() * i;
                    int dy = d.getDy() * i;
                    if(exists(x + dx,y + dy)) {
                        if (board[x + dx][y + dy] == playerTurn) {
                            //System.out.println("[" + (x + dx) + "][" + (y + dy) + "] - true");
                            return true;
                        }
                        if (board[x + dx][y + dy] == Tile.EMPTY) {
                            //System.out.println("[" + (x + dx) + "][" + (y + dy) + "] - break");
                            break;
                        }
                    }
                }
            }
            //System.out.println("("+x+";"+y+")End");
        }
        return false;
    }

    private boolean exists(int x, int y) {
        return ( x>=0 && y>=0 && x<=7 && y<=7 );
    }

    public void doGame() {
        //do {
            if(playerTurn == Tile.BLACK) {
                playerBlack.askToPlay();
            } else {
                playerWhite.askToPlay();
            }
            playerTurn = Tile.opposite(playerTurn);
            notifyObservers();
        //} while( !gameEnded() );
    }

    private boolean gameEnded() {
        //TODO if no plays allowed
        return true;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public boolean[][] getPlaysAllowed() {
        return playsAllowed;
    }

    public Tile getPlayerturn() {
        return playerTurn;
    }

    public Player getPlayerBlack() {
        return playerBlack;
    }

    public Player getPlayerWhite() {
        return playerWhite;
    }

    public String toString() {
        String sRet = "";
        for (int x=0; x<board[0].length; x++) {
            for (int y=0; y<board.length; y++) {
                sRet += "["+board[y][x]+"]";
            }
            sRet += "\n";
        }
        sRet += "----------------------------\n";
        for (int x = 0; x< playsAllowed[0].length; x++) {
            for (int y = 0; y< playsAllowed.length; y++) {
                if( playsAllowed[y][x] ) {
                    sRet += "[0]";
                } else {
                    sRet += "[ ]";
                }
            }
            sRet += "\n";
        }
        return sRet;
    }

    public static void main(String[] args) {
        Player p1 = new IA();
        Player p2 = new IA();
        Game g = new Game(p1,p2);

        System.out.println(g.toString());
    }
}
