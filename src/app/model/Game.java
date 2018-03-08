package app.model;

import app.model.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Classe principale du jeu
 */
public class Game extends Observable implements Runnable{

    private Tile[][] board;
    private boolean[][] playsAllowed;
    private Player playerBlack;
    private Player playerWhite;
    private Tile playerTurn;

    /**
     * Constructeur
     * @param playerBlack le joueur jouant les tuiles noires
     * @param playerWhite le joueur joiant les tuiles blanches
     */
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

        //Init plays allowed
        this.playsAllowed = new boolean[8][8];
        for (int x = 0; x< playsAllowed[0].length; x++) {
            for (int y = 0; y< playsAllowed.length; y++) {
                playsAllowed[x][y] = false;
            }
        }
        calculatePlaysAllowed();
    }

    /**
     * Calculate the number of plays allowed in the board for the current player
     * @return number of plays allowed in the board for the current player
     */
    private int nbOfPlaysAllowed() {
        int cpt = 0;
        for (int x=0; x<playsAllowed[0].length; x++) {
            for (int y=0; y<playsAllowed.length; y++) {
                if( playsAllowed[x][y] ) cpt++;
            }
        }
        return cpt;
    }

    /**
     * Calculate the number of empty tile in the board for the current player
     * @return number of empty tile in the board for the current player
     */
    private int nbOfEmptyTile() {
        int cpt = 0;
        for (int x=0; x<board[0].length; x++) {
            for (int y=0; y<board.length; y++) {
                if( board[x][y] == Tile.EMPTY ) {
                    cpt++;
                }
            }
        }
        return cpt;
    }

    /**
     * Calcule les positions jouables
     *  (met à jour playsAllowed)
     */
    private void calculatePlaysAllowed() {
        for (int x=0; x<playsAllowed[0].length; x++) {
            for (int y=0; y<playsAllowed.length; y++) {
                playsAllowed[x][y] = canPlayAt(x,y);
            }
        }
    }

    /**
     * Calcule si la position (x,y) est une position jouable pour le joueur "playerTurn"
     * @param x valeur x de la position
     * @param y valeur y de la position
     * @return true si la position est jouable, false sinon
     */
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
                for(int i=2; i<=7; i++) {
                    int dx = d.getDx() * i;
                    int dy = d.getDy() * i;
                    if(exists(x + dx,y + dy)) {
                        if (board[x + dx][y + dy] == playerTurn) {
                            return true;
                        }
                        if (board[x + dx][y + dy] == Tile.EMPTY) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Méthode jouant un jeton à la position (x,y)
     * @param x valeur x de la position
     * @param y valeur y de la position
     * @throws BadPlayException Exception levée quand la position n'est pas correct
     */
    private void playAt(int x, int y) throws BadPlayException {
        if(!playsAllowed[x][y] || !exists(x,y)) {
            throw new BadPlayException();
        }
        Tile ennemyTile = Tile.opposite(playerTurn);

        //Search for direction who can be tested
        List<Direction> canTestDirection = new ArrayList<>();
        for (Direction d : Direction.values()) {
            //If there is an ennemy Tile around the point
            if (exists(x + d.getDx(), y + d.getDy()) && board[x + d.getDx()][y + d.getDy()] == ennemyTile) {
                canTestDirection.add( d );
            }
        }

        //For every direction who can be tested
        for (Direction d : canTestDirection ) {
            //For every Tile in the direction
            for(int i=2; i<=7; i++) {
                int dx = d.getDx() * i;
                int dy = d.getDy() * i;
                //If Tile exist
                if(exists(x + dx,y + dy)) {
                    //If player Tile is found
                    if (board[x + dx][y + dy] == playerTurn) {
                        //Replace Tile to player Tile
                        for (int j=i; j>=0; j--) {
                            int djx = d.getDx() * j;
                            int djy = d.getDy() * j;
                            board[x + djx][y + djy] = playerTurn;
                        }
                    }
                    //If empty Tile is found
                    if (board[x + dx][y + dy] == Tile.EMPTY) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Permet de savoir si la position (x,y) est une position valide
     * @param x valeur x de la position
     * @param y valeur y de la position
     * @return true si la position est valide, false sinon
     */
    private boolean exists(int x, int y) {
        return ( x>=0 && y>=0 && x<=7 && y<=7 );
    }

    /**
     * Méthode principale du jeu
     */
    public void run() {
        Point play;
        try {
            //Loop of the game
            do {
                if (playerTurn == Tile.BLACK) {
                    play = playerBlack.askToPlay();
                } else {
                    play = playerWhite.askToPlay();
                }
                if(!playsAllowed[play.x][play.y]) {
                    throw new BadPlayException();
                }

                playAt(play.x,play.y);

                playerTurn = Tile.opposite(playerTurn);
                calculatePlaysAllowed();

                //Notify the view
                setChanged();
                notifyObservers();
            } while (!gameEnded());
        } catch (InterruptedException | BadPlayException e) {
            e.printStackTrace();
        }
    }

    private boolean gameEnded() {
        return nbOfEmptyTile() == 0;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public boolean[][] getPlaysAllowed() {
        return playsAllowed;
    }

    public Tile getPlayerTurn() {
        return playerTurn;
    }

    public Player getPlayerWhoPlay() {
        if(playerTurn == Tile.BLACK)
            return playerBlack;
        return playerWhite;
    }

    public Player getPlayerBlack() {
        return playerBlack;
    }

    public int getPlayerBlackScore() {
        int cpt = 0;
        for (int x=0; x<board[0].length; x++) {
            for (int y=0; y<board.length; y++) {
                if( board[x][y] == Tile.BLACK ) {
                    cpt++;
                }
            }
        }
        return cpt;
    }

    public Player getPlayerWhite() {
        return playerWhite;
    }

    public int getPlayerWhiteScore() {
        int cpt = 0;
        for (int x=0; x<board[0].length; x++) {
            for (int y=0; y<board.length; y++) {
                if( board[x][y] == Tile.WHITE ) {
                    cpt++;
                }
            }
        }
        return cpt;
    }

    public String toString() {
        String sRet = "";
        sRet += "Player Turn : "+playerTurn.toString()+"\n";
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
}
