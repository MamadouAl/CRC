package edu.diallo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class Commutateurs extends JFrame {

    private JButton createButton;
    private JButton deleteButton;
    private JButton addLinkButton;
    private JButton removeLinkButton;
    private JButton findShortestPathButton;
    private JButton nextButton;
    private JTextField costField;
    private JTextArea graphArea;

    public Commutateurs() {
        super("Gestion des commutateurs et liaisons");
        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Gestion des commutateurs et liaisons");
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        add(headerLabel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Créer commutateur");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.creerCommutateur();
                updateGraphArea();
            }
        });
        buttonPanel.add(createButton);

        deleteButton = new JButton("Supprimer commutateur");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.supprimerCommutateur();
                updateGraphArea();
            }
        });
        buttonPanel.add(deleteButton);

        addLinkButton = new JButton("Ajouter liaison");
        addLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.ajouterLiaison();
                updateGraphArea();
            }
        });
        buttonPanel.add(addLinkButton);

        removeLinkButton = new JButton("Supprimer liaison");
        removeLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.supprimerLiaison();
                updateGraphArea();
            }
        });
        buttonPanel.add(removeLinkButton);

        findShortestPathButton = new JButton("Trouver chemin le plus court");
        findShortestPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.trouverCheminLePlusCourt();
            }
        });
        buttonPanel.add(findShortestPathButton);

        nextButton = new JButton("Afficher tables de routage");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.afficherTablesDeRoutages();
            }
        });
        buttonPanel.add(nextButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Cost field
        JPanel costPanel = new JPanel();
        costPanel.add(new JLabel("Coût de la liaison :"));
        costField = new JTextField(5);
        costPanel.add(costField);
        add(costPanel, BorderLayout.SOUTH);

        // Graph area
        graphArea = new JTextArea(); 
        graphArea.setRows(5); //augmente la hauteur de la zone de texte
        graphArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(graphArea);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void updateGraphArea() {
        StringBuilder graphText = new StringBuilder();
        graphText.append("Graphe :\n");
        for (Map.Entry<String, Map<String, Integer>> entry : Main.getGraph().entrySet()) {
            graphText.append(entry.getKey()).append(" : ");
            for (Map.Entry<String, Integer> neighbor : entry.getValue().entrySet()) {
                graphText.append(neighbor.getKey()).append("(").append(neighbor.getValue()).append(") ");
            }
            graphText.append("\n");
        }
        graphArea.setText(graphText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Commutateurs::new);
    }
}
