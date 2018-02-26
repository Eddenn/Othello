package app.view;

import app.model.Game;
import app.view.components.OthelloBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Interface graphique de l'application OthelloGUI
 */
public class OthelloGUI {

    private Game game;
    private Font font;
    private JFrame mainFrame;
    private Border paddedBorderRight, paddedBorderLeft;
    private OthelloBoard board;
    private JLabel player1,player2;
    private JLabel player1score, player2score;
    /*private JMenuBar menuBar;
    private JMenu menuFile, menuAbout;
    private JMenuItem mif1, mif2, mif3;
    private JMenuItem mia1;
    private JPanel jpHead;
    private JButton btnMinimize, btnMaximize, btnClose;*/

    /**
     * Constructeur appellant createModel(), createComponents(), placeComponents() et createControllers()
     */
    public OthelloGUI() {
        createModel();
        createComponents();
        placeComponents();
        createControllers();
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
        game = new Game(null,null);
        try {
            board = new OthelloBoard(8,8);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,
                    e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        board.refreshModel(game);
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

        player1 = new JLabel("Joueur 1",new ImageIcon(getClass().getResource("/player_black.png")),SwingConstants.CENTER);
        player1.setVerticalTextPosition(SwingConstants.BOTTOM);
        player1.setHorizontalTextPosition(SwingConstants.CENTER);
        player1.setFont(font.deriveFont(Font.PLAIN,24));
        player1score = new JLabel("16",SwingConstants.CENTER);
        player1score.setFont(font.deriveFont(Font.PLAIN,32));

        player2 = new JLabel("Joueur 2",new ImageIcon(getClass().getResource("/player_white.png")),SwingConstants.CENTER);
        player2.setVerticalTextPosition(SwingConstants.BOTTOM);
        player2.setHorizontalTextPosition(SwingConstants.CENTER);
        player2.setFont(font.deriveFont(Font.PLAIN,24));
        player2score = new JLabel("16",SwingConstants.CENTER);
        player2score.setFont(font.deriveFont(Font.PLAIN,32));

        /*
        //---- MenuBar ----//
        menuBar = new JMenuBar();
        menuFile = new JMenu("Fichier");
        mif1 = new JMenuItem("Ouvrir");
        menuFile.add(mif1);
        mif2 = new JMenuItem("Enregistrer");
        menuFile.add(mif2);
        mif3 = new JMenuItem("Quitter");
        menuFile.add(mif3);
        menuBar.add(menuFile);

        menuAbout = new JMenu("A propos");
        mia1 = new JMenuItem("?");
        menuAbout.add(mia1);
        menuBar.add(menuAbout);

        //---- Buttons ----//
        btnMinimize = createFlatButton(getClass().getResource("/button/minimize.png"));
        btnMaximize = createFlatButton(getClass().getResource("/button/maximize.png"));
        btnClose = createFlatButton(getClass().getResource("/button/close.png"));*/
    }

    private JButton createFlatButton(URL icon) {
        JButton btn = new JButton(new ImageIcon(icon));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setOpaque(false);
        btn.setMargin(new Insets(0,0,0,0));
        return btn;
    }

    /**
     * Place les composants sur la fenêtre
     */
    private void placeComponents() {
        /*
        // En tête de l'appli
        {
            FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT);
            fLayout.setHgap(0);
            fLayout.setVgap(0);
            JPanel q = new JPanel(fLayout); {
                q.add(menuBar);
            }
            q.setOpaque(false);
            jpHead.add(q,BorderLayout.WEST);
            fLayout.setAlignment(FlowLayout.RIGHT);
            q = new JPanel(fLayout); {
                q.add(btnMinimize);
                q.add(btnMaximize);
                q.add(btnClose);
            }
            q.setOpaque(false);
            jpHead.add(q,BorderLayout.EAST);
        }
        jpHead.setBackground(new Color(130, 204, 221));
        mainFrame.add(jpHead,BorderLayout.NORTH);*/

        //Joueur 1
        JPanel p = new JPanel(new GridBagLayout()); {
            JPanel q = new JPanel(new GridLayout(0,1)); {
                q.add(player1);
                q.add(player1score);
            }
            q.setOpaque(false);
            p.add(q, new GridBagConstraints());
        }
        p.setBackground(new Color(130, 204, 221));
        p.setBorder(paddedBorderRight);
        mainFrame.add(p,BorderLayout.WEST);
        //Joueur 2
        p = new JPanel(new GridBagLayout()); {
            JPanel q = new JPanel(new GridLayout(0,1)); {
                q.add(player2);
                q.add(player2score);
            }
            q.setOpaque(false);
            p.add(q, new GridBagConstraints());
        }
        p.setBackground(new Color(130, 204, 221));
        p.setBorder(paddedBorderLeft);
        mainFrame.add(p,BorderLayout.EAST);
        //Plateau de jeu
        p = new BoardContainer(new GridBagLayout()); {
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

        /*
        //---- Button Close/Maximize/Minimize ----//
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                System.exit(0);
            }
        });
        btnMaximize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mainFrame.getExtendedState() == JFrame.NORMAL) {
                    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    mainFrame.setExtendedState(JFrame.NORMAL);
                }
            }
        });
        btnMinimize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setState(JFrame.ICONIFIED);
            }
        });
        */
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
