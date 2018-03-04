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
            backgroundTile = ImageIO.read(getClass().getResource("/board_tile.png"));
        } catch(Exception e) {
            throw new IOException("L'image représentant le fond du plateau pas pu être chargée.");
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

        g.drawImage(backgroundTile,0,0,getWidth(),getHeight(),null);
        Image img = status.getImage();
        if(status != TileStatus.EMPTY) g.drawImage(img,5,5,getWidth()-10,getHeight()-10, null);

    }

    @Override
    public String toString() {
        return "OthelloTile{" +
                "status=" + status +
                ", size=" + size +
                '}';
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size,size);
    }
}
