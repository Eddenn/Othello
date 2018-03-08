package app.view.components;

import app.model.Game;
import app.model.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * JPanel représentant un plateau composé de tuiles (OthelloTile)
 */
public class OthelloBoard extends JPanel {

    private static Image woodBorder;

    /**
     * Grille contenant les tuiles (OthelloTile)
     */
    private OthelloTile[][] grid;

    /**
     * Constructeur initialisant le tableau de tuiles (grid),
     * ainsi que le layout de base (GridLayout) de ce plateau
     * @param width
     * @param height
     */
    public OthelloBoard(int width, int height) throws IOException {
        grid = new OthelloTile[width][height];

        try {
            woodBorder = ImageIO.read(getClass().getResource("/board_wood.png"));
        } catch (Exception e) {
            throw new IOException("Les images correspondant aux poins n'ont pas pu être chargées.");
        }

        setLayout(new GridBagLayout());
        JPanel p = new JPanel(new GridLayout(width,height));

        for(int x=0; x<grid.length; x++) {
            for(int y=0; y<grid[0].length; y++) {
                grid[x][y] = new OthelloTile(50,x,y);
                p.add(grid[x][y]);
            }
        }

        add(p, new GridBagConstraints());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(woodBorder,0,0,getWidth(),getHeight(),null);
    }

    /**
     * Methode permettant de changer le status d'une tuile du plateau
     * @param x
     * @param y
     * @param ts
     * @throws IndexOutOfBoundsException
     */
    public void setTileStatus(int x, int y, TileStatus ts) throws IndexOutOfBoundsException{
        if(x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            throw new IndexOutOfBoundsException("Il n'existe pas de tuile à la position ("+x+","+y+")");
        }
        grid[x][y].setStatus(ts);
    }

    /**
     * Getter retournant la tuile board[i][j]
     * @param i
     * @param j
     * @return OthelloTile à la position board[i][j]
     */
    public OthelloTile getTile(int i, int j) {
        if(i<0 || j<0 || i>=grid.length || j>=grid[0].length) {
            return null;
        }
        return grid[i][j];
    }

    /**
     * Touve l'OthelloTile correspondante au Point p
     * @param p
     * @return OthelloTile
     */
    public OthelloTile getTile(Point p) {
        if(p == null) {
            return null;
        }
        int x = (p.x-35)/grid[0][0].getPreferredSize().width;
        int y = (p.y-35)/grid[0][0].getPreferredSize().height;
        if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) {
            return null;
        }
        return grid[y][x];
    }

    /**
     * Met à jour le plateau de jeu (la vue)
     * @param g Game
     */
    public void refreshModel(Game g) {
        Tile[][] board = g.getBoard();
        for (int x=0; x<board[0].length; x++) {
            for (int y=0; y<board.length; y++) {
                switch (board[x][y]) {
                    case WHITE: setTileStatus(x,y,TileStatus.WHITE); break;
                    case BLACK: setTileStatus(x,y,TileStatus.BLACK); break;
                    default : setTileStatus(x,y,TileStatus.EMPTY);
                }
                if(g.getPlaysAllowed()[x][y]) {
                    setTileStatus(x,y,TileStatus.EMPTY_PLAYABLE);
                }
            }
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(grid[0][0].getPreferredSize().width * grid.length + 70,
                             grid[0][0].getPreferredSize().height * grid[0].length + 70);
    }
}
