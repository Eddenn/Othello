package app.model.player;

import java.awt.*;

public interface Player {

    /**
     * Méthode qui attend que le joueur joue
     * @return position jouée
     * @throws InterruptedException
     */
    Point askToPlay() throws InterruptedException;
}
