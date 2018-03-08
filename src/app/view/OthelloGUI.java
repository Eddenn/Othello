package app.view;

import app.model.Game;
import app.model.Tile;
import app.model.player.RealPlayer;
import app.view.components.OthelloBoard;
import app.view.components.OthelloTile;
import app.view.components.TileStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Interface graphique de l'application OthelloGUI
 */
public class OthelloGUI {

    private Game game;

    private Font font;
    private JFrame mainFrame;
    private Border paddedBorderRight, paddedBorderLeft;
    private OthelloBoard board;
    private JPanel playerBlackPanel, playerWhitePanel;
    private JLabel playerBlack, playerWhite;
    private JLabel playerBlackScore, playerWhiteScore;

    /**
     * Constructeur appellant createModel(), createComponents(), placeComponents() et createControllers()
     */
    public OthelloGUI() {
        createModel();
        createComponents();
        placeComponents();
        createControllers();
        refreshView();
        (new Thread(game)).start();
    }

    /**
     * Affiche la fenêtre
     */
    public void display() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Initialise le model
     */
    private void createModel() {
        game = new Game(new RealPlayer(),new RealPlayer());
        try {
            board = new OthelloBoard(8,8);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,
                    e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Initialise les composants de la fenêtre
     */
    private void createComponents() {
        //---- Chargement de la police de texte ----//
        try {
            InputStream is = OthelloGUI.class.getResourceAsStream("/coolvetica.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("coolvetica.ttf ne s'est pas chargée.  Utilisation de la font de base.");
            font = new Font("serif", Font.PLAIN, 24);
        }

        //---- La fenêtre ----//
        mainFrame = new JFrame("OthelloGUI");
        mainFrame.setSize(800,600);
        mainFrame.setMinimumSize(new Dimension(800,600));
        mainFrame.setLayout(new BorderLayout());
        //mainFrame.setUndecorated(true);
        mainFrame.setBackground(new Color(130, 204, 221));

        //jpHead = new JPanel(new BorderLayout());

        //---- Player à droite et à gauche ----//
        paddedBorderRight = BorderFactory.createCompoundBorder(new MatteBorder(0,0,0,3,Color.BLACK),new EmptyBorder(10, 10, 10, 10));
        paddedBorderLeft = BorderFactory.createCompoundBorder(new MatteBorder(0,3,0,0,Color.BLACK),new EmptyBorder(10, 10, 10, 10));

        playerBlack = new JLabel("Joueur 1",new ImageIcon(getClass().getResource("/player_black.png")),SwingConstants.CENTER);
        playerBlack.setVerticalTextPosition(SwingConstants.BOTTOM);
        playerBlack.setHorizontalTextPosition(SwingConstants.CENTER);
        playerBlack.setFont(font.deriveFont(Font.PLAIN,24));
        playerBlackScore = new JLabel("16",SwingConstants.CENTER);
        playerBlackScore.setFont(font.deriveFont(Font.PLAIN,32));

        playerWhite = new JLabel("Joueur 2",new ImageIcon(getClass().getResource("/player_white.png")),SwingConstants.CENTER);
        playerWhite.setVerticalTextPosition(SwingConstants.BOTTOM);
        playerWhite.setHorizontalTextPosition(SwingConstants.CENTER);
        playerWhite.setFont(font.deriveFont(Font.PLAIN,24));
        playerWhiteScore = new JLabel("16",SwingConstants.CENTER);
        playerWhiteScore.setFont(font.deriveFont(Font.PLAIN,32));
    }

    /**
     * Place les composants sur la fenêtre
     */
    private void placeComponents() {
        //Joueur 1
        playerBlackPanel = new JPanel(new GridBagLayout()); {
            JPanel q = new JPanel(new GridLayout(0,1)); {
                q.add(playerBlack);
                q.add(playerBlackScore);
            }
            q.setOpaque(false);
            playerBlackPanel.add(q, new GridBagConstraints());
        }
        playerBlackPanel.setBackground(new Color(220, 220, 220));
        playerBlackPanel.setBorder(paddedBorderRight);
        mainFrame.add(playerBlackPanel,BorderLayout.WEST);
        //Joueur 2
        playerWhitePanel = new JPanel(new GridBagLayout()); {
            JPanel q = new JPanel(new GridLayout(0,1)); {
                q.add(playerWhite);
                q.add(playerWhiteScore);
            }
            q.setOpaque(false);
            playerWhitePanel.add(q, new GridBagConstraints());
        }
        playerWhitePanel.setBackground(new Color(220, 220, 220));
        playerWhitePanel.setBorder(paddedBorderLeft);
        mainFrame.add(playerWhitePanel,BorderLayout.EAST);
        //Plateau de jeu
        JPanel p = new BoardContainer(new GridBagLayout()); {
            p.add(board, new GridBagConstraints());
        }
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainFrame.add(p,BorderLayout.CENTER);

    }
    /**
     * Initilise les controlleurs de la fenêtre
     */
    private void createControllers() {
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Game observer
        game.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                refreshView();
            }
        });

        //MouseClicked, MouseEntered and MouseExited on all OthelloTile
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                board.getTile(i,j).addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        OthelloTile tile = (OthelloTile)(e.getSource());
                        if(tile.getStatus() == TileStatus.EMPTY_PLAYABLE
                            || tile.getStatus() == TileStatus.EMPTY_PLAYABLE_HOVERED) {
                            if( game.getPlayerWhoPlay() instanceof RealPlayer) {
                                RealPlayer p = (RealPlayer) game.getPlayerWhoPlay();
                                p.setPlay(tile.getPosition());
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        OthelloTile tile = (OthelloTile)(e.getSource());
                        if(tile.getStatus() == TileStatus.EMPTY_PLAYABLE) {
                            tile.setStatus(TileStatus.EMPTY_PLAYABLE_HOVERED);
                            tile.repaint();
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        OthelloTile tile = (OthelloTile)(e.getSource());
                        if(tile.getStatus() == TileStatus.EMPTY_PLAYABLE_HOVERED) {
                            tile.setStatus(TileStatus.EMPTY_PLAYABLE);
                            tile.repaint();
                        }
                    }
                });
            }
        }
        board.requestFocus();
    }

    /**
     * Méthode mettant à jour la vue
     */
    private void refreshView() {
        if(game.getPlayerTurn() == Tile.BLACK) {
            playerBlackPanel.setBackground(new Color(130, 220, 220));
            playerWhitePanel.setBackground(new Color(220, 220, 220));
        } else {
            playerWhitePanel.setBackground(new Color(130, 220, 220));
            playerBlackPanel.setBackground(new Color(220, 220, 220));
        }
        playerBlackScore.setText(""+game.getPlayerBlackScore());
        playerWhiteScore.setText(""+game.getPlayerWhiteScore());
        playerBlackPanel.repaint();
        playerWhitePanel.repaint();
        board.refreshModel(game);
    }

    /**
     * Méthode principale instanciant l'OthelloGUI
     * @param args Arguments d'entrée de l'application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OthelloGUI().display();
            }
        });
    }

    /**
     * Panel contenant le plateau de jeu
     * Celui-ci permet le coté "responsive" de la fenêtre
     */
    class BoardContainer extends JPanel {

        BoardContainer(LayoutManager layout) {
            super(layout);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                Image img = ImageIO.read(getClass().getResource("/background.jpg"));
                g.drawImage(img,0,0,getWidth(),getHeight(), null);
            } catch(IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
