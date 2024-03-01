import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Signal extends JFrame {
    private JTextField binaire;
    private JComboBox<String> combo;
    private JButton bouton;
    
   

    private static final int WIDTH = 800;
    private static final int HEIGHT = 200;
    
    //paramètre utilisé pour tracer les signaux
    private int width = WIDTH;
    private int height = HEIGHT;
    private int hauteur = (height - 100) / 2;
    
    /**
     * Constructeur par defaut de la classe Signal.
     * Initialise la fenêtre principale de l'application.
     */
    public Signal() {
        setTitle("Signal Electrique");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //initialisation des composants 
        initCompo();

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Initialise les composants de l'interface utilisateur, 
     * y compris le champ de texte, la liste déroulante et le bouton.
     */
    private void initCompo() {
        binaire = new JTextField(20);
        String[] mesCodes = {"NRZ", "Manchester", "Manchester Différentiel", "Miller"};
        combo = new JComboBox<>(mesCodes);
        bouton = new JButton("Tracer le signal");

        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chaineBinaire = binaire.getText();
                String codeChoisi = (String) combo.getSelectedItem();
                
                //En fonction du code choisi, on trace le signal correspondant
                if (codeChoisi.equals("NRZ")) {
                    plotSignal(chaineBinaire, "NRZ");
                }else if (codeChoisi.equals("Manchester")) {
                	plotSignal(chaineBinaire, "Manchester");
                }else if (codeChoisi.equals("Manchester Différentiel")) {
                	plotSignal(chaineBinaire, "Manchester Différentiel");
                }else if (codeChoisi.equals("Miller")) {
                	plotSignal(chaineBinaire, "Miller");
                }else {
                    JOptionPane.showMessageDialog(Signal.this, "Fonction de tracé non implémentée");
                }
            }
        });
        //Ajout des autres composants 
        addCompo();
    }
    
    /**
     * Ajoute les composants à la fenêtre principale.
     */
    private void addCompo() {
        JPanel panneau = new JPanel(new GridLayout(3, 2, 10, 10));

        panneau.add(new JLabel("Entrer le code Binaire :"));
        panneau.add(binaire);

        panneau.add(new JLabel("Quel code tracer ? "));
        panneau.add(combo);

        panneau.add(new JLabel(""));
        panneau.add(bouton);

        setLayout(new BorderLayout());
        add(panneau, BorderLayout.CENTER);
    }
    
    /**
     * Fonction de traçage principale qui crée une nouvelle fenêtre 
     * pour afficher le signal en fonction du code choisi.
     */
    private void plotSignal(String binaire, String code) {
        JPanel panneau = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(code == "NRZ") {
                	traceNRZ(g, binaire);
                }else if(code == "Manchester") {
                    traceManchester(g, binaire);
                }else if(code == "Manchester Différentiel") {
                	 traceManDiff(g, binaire);
                }else {
                	traceMiller(g, binaire);
                }
            }
        };

        JFrame fenetre = new JFrame(code);
        fenetre.setSize(WIDTH, HEIGHT);
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setLayout(new BorderLayout());
        fenetre.add(panneau, BorderLayout.CENTER);
        fenetre.setLocationRelativeTo(null);
        fenetre.setVisible(true);
    }
    
    /**
     * Dessine la grille et le repère choisi.
     */
    private void dessineGrille(Graphics g, String binaire, int width, int height, int xlarg) {
        g.setColor(Color.BLACK);
        g.drawLine(50, height / 2, width/2 -5, height / 2); 
        
        g.drawString("0", 30, height/2);
        g.drawString("nV", 30, height / 2 - 50);
        g.drawString("-nV", 30, height / 2 + 50);

        // Dessiner la grille pour les temps d'horloge
        g.setColor(new Color(0, 0, 0, 50));
        for (int i = 0; i < binaire.length(); i++) {
            g.drawLine(i * xlarg + 50, 50, i * xlarg + 50, height - 50);
        }
	}
    
    /**
     * Trace un signal NRZ en fonction du code binaire fourni.
     */    
    private void traceNRZ(Graphics g, String binaire) {
        int largeur = (width - 100) / (binaire.length() * 2);
        dessineGrille(g, binaire, width, height, largeur);

        // Dessiner les transitions pour le signal électrique
        g.setColor(Color.RED); // Couleur des lignes pour NRZ
        int x = 50;
        int y = height / 2;

        for (int i = 0; i < binaire.length(); i++) {
        	Character bit = binaire.charAt(i);
        	
            //Affichage des bits au dessus du signal
            g.drawString(bit.toString(), x + largeur/2 , y - hauteur -10);
            
            if (bit == '0') {
                g.drawLine(x, y + hauteur, x + largeur, y + hauteur);
            } else if (bit == '1') {
                g.drawLine(x, y - hauteur, x + largeur, y - hauteur);
            }
            x += largeur;

            // Dessiner la transition verticale
            if (i < binaire.length() - 1 && bit != binaire.charAt(i + 1)) {
                g.drawLine(x, y + hauteur, x, y - hauteur);
            }
        }
    }
    
    /**
     * Trace un signal Manchester en fonction du code binaire fourni.     * @param g
     */
    private void traceManchester(Graphics g, String binaire) {
        int largeur = (width - 100) / (binaire.length() * 2);
        int x = 50;
        int y = height / 2;
        // Dessin de la grille
        dessineGrille(g, binaire, width, height, largeur);
        g.setColor(Color.MAGENTA);
        
        for (int i = 0; i < binaire.length(); i++) {
        	Character bit = binaire.charAt(i);
        	
            //Affichage des bits au dessus du signal
            g.drawString(bit.toString(), x + largeur/2 , y - hauteur -10);
            if (bit == '0') {
                // Pour 0, ajoute une transition de -nV vers nV
                g.drawLine(x, y + hauteur, x + largeur / 2, y + hauteur);
                g.drawLine(x + largeur / 2, y + hauteur, x + largeur / 2, y - hauteur);
                g.drawLine(x + largeur / 2, y - hauteur, x + largeur, y - hauteur);
            } else {
                // Pour 1, ajoute une transition de nV vers -nV
                g.drawLine(x, y - hauteur, x + largeur / 2, y - hauteur);
                g.drawLine(x + largeur / 2, y - hauteur, x + largeur / 2, y + hauteur);
                g.drawLine(x + largeur / 2, y + hauteur, x + largeur, y + hauteur);
            }

            x += largeur;
        }
    }
  
    /**
     * Trace un signal Manchester Différentiel en fonction du code binaire fourni.
     */
    private void traceManDiff(Graphics g, String binaire) {
        int largeur = (width - 100) / (binaire.length() * 2);
        int milieu = height / 2;
        int changeY = 0;  // Pour inverser la position verticale
        int x = 50;
        int y = height / 2 - hauteur; // Position verticale de départ

        // Dessiner la grille et le repère
        dessineGrille(g, binaire, width, height, largeur);
        g.setColor(Color.RED);
        
        for (int i = 0; i < binaire.length(); i++) {
        	Character bit = binaire.charAt(i);
            //Affichage des bits au dessus du signal
            g.drawString(bit.toString(), x + largeur/2 +5 , milieu - hauteur -10);
            if (bit == '0') {
                changeY = 2 * milieu - y;
                g.drawLine(x, changeY, x, y);
                y = changeY;
                g.drawLine(x, y, x + (largeur / 2), y);
                changeY = 2 * milieu - y;
                g.drawLine(x + (largeur/2), y, x + largeur / 2, changeY);
                y = changeY; //mis à jour
                g.drawLine(x + largeur/2, y, x + largeur, y);

                if (i < binaire.length() - 1 && binaire.charAt(i + 1) == '0') {
                    g.drawLine(x + largeur, y, x + largeur, changeY);
                }
            } else if (bit == '1') {
                g.drawLine(x, y, x + largeur / 2, y);
                changeY = 2 * milieu - y;
                g.drawLine(x + largeur/2, y, x + largeur / 2, changeY);
                y = changeY;
                g.drawLine(x + (largeur / 2), y, x + largeur, y);

                if (i < binaire.length() - 1 && binaire.charAt(i + 1) == '1') {
                    y = 2 * milieu -y;
                } else if (i < binaire.length() - 1 && binaire.charAt(i + 1) == '0') {
                    y = 2 * milieu -y;
                }
                y = 2 * milieu -y;
            }

            x += largeur; // Avancer :)
        }
    }


    
    /**
     * Trace un signal Miller en fonction du code binaire fourni.
     */
    
    
    public void traceMiller(Graphics g, String binaire) {
        int largeur = (width - 100) / (binaire.length() * 2);
        int x = 50;
        int y = height / 2;
        int dernierY = 0;
        
        // Dessin de la grille
        dessineGrille(g, binaire, width, height, largeur);
        g.setColor(Color.RED);
       
        for (int i = 0; i < binaire.length(); i++) {
            Character bit = binaire.charAt(i);
            g.drawString(bit.toString(), x + largeur / 2, y - hauteur - 10);

            if (bit == '0') {
            	if(i==0) {
            		dernierY = y - hauteur;
                    g.drawLine(x , dernierY, x + largeur/2 , dernierY);
                    g.drawLine(x + largeur/2, dernierY, x + largeur, dernierY);
            	}else if (i > 0 && binaire.charAt(i - 1) == '1') {
                    // Si le bit précédent est un '1', absence de transition
                    g.drawLine(x, dernierY, x + largeur / 2, dernierY);
                    g.drawLine(x + largeur / 2, dernierY, x + largeur, dernierY);
                } else {
                    // Si le bit précédent est un '0', ajouter une transition verticale puis absence de transition                	
                    g.drawLine(x, dernierY, x, (dernierY == y - hauteur) ? y + hauteur : y - hauteur);
                    dernierY = (dernierY == y - hauteur) ? y + hauteur : y - hauteur;
                    
                    g.drawLine(x , dernierY, x + largeur/2 , dernierY);
                    g.drawLine(x + largeur/2, dernierY, x + largeur, dernierY);
                }
            } else {
                // Pour 1, ajouter une transition en milieu (de n à -n ou -n à n)
                if ((i > 0 && binaire.charAt(i - 1) == '1') || (i > 0 && binaire.charAt(i - 1) == '0')) {
                    // Si le bit précédent est un '1' ou '0', ajouter une transition de la tension n à -n
                    g.drawLine(x, dernierY, x + largeur / 2, dernierY);
                    g.drawLine(x + largeur / 2, dernierY, x + largeur / 2, (dernierY == y - hauteur) ? y + hauteur : y - hauteur);
                    
                    dernierY = (dernierY == y - hauteur) ? y + hauteur : y - hauteur;
                    g.drawLine(x + largeur / 2, dernierY, x + largeur, dernierY);
                } else {
                    // Sinon, ajouter une transition au milieu d'horloge
                    g.drawLine(x, y - hauteur, x + largeur / 2, y - hauteur);
                    g.drawLine(x + largeur / 2, y - hauteur, x + largeur / 2, y + hauteur);

                    dernierY = y + hauteur;
                    g.drawLine(x + largeur / 2, dernierY, x + largeur, dernierY);
                }
            }

            x += largeur;
        }
    }




    /**
     * Fonction principale
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Signal::new);
    }
}
