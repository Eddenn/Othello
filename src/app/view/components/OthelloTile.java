package app.view.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * JComponent utilisé pour l'affichage d'une tuile de l'OthelloPanel
 */
public class OthelloTile extends JComponent {

    /**
     * Statut de la tuile
     */
    private TileStatus status;
    private int size;
    private static Image imgWhitePawn;
    private static Image imgBlackPawn;
    private static Image imgPlayable;
    private static Image backgroundTile;

    /**
     * Constructeur de la tuile, par defaut le statut est EMPTY
     */
    public OthelloTile(int size) throws IOException {
        super();
        this.size = size;
        this.status = TileStatus.EMPTY;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        try {
            imgBlackPawn = ImageIO.read(getClass().getResource("/black_pawn.png"));
            imgWhitePawn = ImageIO.read(getClass().getResource("/white_pawn.png"));
            imgPlayable = ImageIO.read(getClass().getResource("/playable_place.png"));
            backgroundTile = ImageIO.read(getClass().getResource("/board_tile.png"));
        } catch(Exception e) {
            throw new IOException("Les images correspondant aux poins n'ont pas pu être chargées.");
        }
    }

    /**
     * Retourne le statut de la tuile
     * @return statut de la tuile
     */
    public TileStatus getStatus() {
        return status;
    }

    /**
     * Permet de changer de statut de la tuile
     * @param status Le nouveau statut de la tuile
     */
    public void setStatus(TileStatus status) {
        this.status = status;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = ((Graphics2D)g);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        //g.setColor(new Color(75,135,40));
        //g.fillRect(0,0,getWidth(),getHeight());
        g.drawImage(backgroundTile,0,0,getWidth(),getHeight(),null);
        Image img = null;
        switch (status) {
            case BLACK : img = imgBlackPawn;
                break;
            case WHITE : img = imgWhitePawn;
                break;
            case EMPTY_PLAYABLE : img = imgPlayable;
                break;
            default    : //Merci le compilateur (obligé)
        }
        if(status != TileStatus.EMPTY) g.drawImage(img,5,5,getWidth()-10,getHeight()-10, null);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size,size);
    }
}
