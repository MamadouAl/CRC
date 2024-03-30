package edu.diallo;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Routage {

    private static Map<String, Map<String, Integer>> graph = new HashMap<>();
    private static JTextArea textArea;

    public static void creerCommutateur() {
        String nomCommutateur = JOptionPane.showInputDialog("Entrez le nom du commutateur:");
        if (nomCommutateur != null && !nomCommutateur.isEmpty()) {
            if (graph.containsKey(nomCommutateur)) {
                afficherMessage("Un commutateur avec le même nom existe déjà.");
            } else {
                graph.put(nomCommutateur, new HashMap<>());
                afficherMessage("Commutateur créé : " + nomCommutateur);
            }
        }
    }

    public static void ajouterLiaison() {
        if (graph.size() < 2) {
            afficherMessage("Au moins deux commutateurs doivent être créés pour ajouter une liaison.");
            return;
        }
        String[] commutateurs = graph.keySet().toArray(new String[0]);
        String commutateurSource = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur source:",
                "Ajouter liaison", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
        if (commutateurSource == null) {
            return; // L'utilisateur a annulé
        }
        String commutateurDestination = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur destination:",
                "Ajouter liaison", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
        if (commutateurDestination == null) {
            return; // L'utilisateur a annulé
        }
        if (commutateurSource.equals(commutateurDestination)) {
            afficherMessage("Les commutateurs source et destination ne peuvent pas être les mêmes.");
            return;
        }
        if (graph.get(commutateurSource).containsKey(commutateurDestination)) {
            afficherMessage("Une liaison entre ces deux commutateurs existe déjà.");
            return;
        }
        String costString = JOptionPane.showInputDialog("Entrez le coût de la liaison:");
        if (costString == null) {
            return; // L'utilisateur a annulé
        }
        int cost;
        try {
            cost = Integer.parseInt(costString);
        } catch (NumberFormatException e) {
            afficherMessage("Veuillez entrer un nombre entier pour le coût.");
            return;
        }
        if (cost <= 0) {
            afficherMessage("Le coût de la liaison doit être un nombre positif.");
            return;
        }
        graph.get(commutateurSource).put(commutateurDestination, cost);
        graph.get(commutateurDestination).put(commutateurSource, cost);
        afficherMessage("Liaison ajoutée entre " + commutateurSource + " et " + commutateurDestination + " avec un coût de " + cost);
    }


    public static void supprimerCommutateur() {
        if (!graph.isEmpty()) {
            String[] commutateurs = graph.keySet().toArray(new String[0]);
            String nomCommutateur = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur à supprimer:",
                    "Supprimer commutateur", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
            if (nomCommutateur != null) {
                graph.remove(nomCommutateur);
                for (Map<String, Integer> voisins : graph.values()) {
                    voisins.remove(nomCommutateur);
                }
                afficherMessage("Commutateur supprimé : " + nomCommutateur);
            }
        } else {
            afficherMessage("Aucun commutateur à supprimer.");
        }
    }

    public static void supprimerLiaison() {
        if (graph.size() < 2) {
            afficherMessage("Au moins deux commutateurs doivent être créés pour supprimer une liaison.");
            return;
        }
        String[] commutateurs = graph.keySet().toArray(new String[0]);
        String commutateurSource = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur source:",
                "Supprimer liaison", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
        if (commutateurSource == null) {
            return; // L'utilisateur a annulé
        }
        String commutateurDestination = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur destination:",
                "Supprimer liaison", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
        if (commutateurDestination == null) {
            return; // L'utilisateur a annulé
        }
        if (!graph.get(commutateurSource).containsKey(commutateurDestination)) {
            afficherMessage("Il n'y a pas de liaison entre " + commutateurSource + " et " + commutateurDestination);
            return;
        }
        graph.get(commutateurSource).remove(commutateurDestination);
        graph.get(commutateurDestination).remove(commutateurSource);
        afficherMessage("Liaison supprimée entre " + commutateurSource + " et " + commutateurDestination);
    }

    public static void trouverCheminLePlusCourt() {
        if (graph.size() < 2) {
            afficherMessage("Au moins deux commutateurs doivent être créés pour trouver un chemin.");
            return;
        }
        String[] commutateurs = graph.keySet().toArray(new String[0]);
        String commutateurDepart = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur de départ:",
                "Trouver chemin le plus court", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
        if (commutateurDepart == null) {
            return; // L'utilisateur a annulé
        }
        String commutateurDestination = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur de destination:",
                "Trouver chemin le plus court", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
        if (commutateurDestination == null) {
            return; // L'utilisateur a annulé
        }

        List<String> cheminPlusCourt = trouverCheminPlusCourt(graph, commutateurDepart, commutateurDestination);
        if (cheminPlusCourt.isEmpty()) {
            afficherMessage("Aucun chemin trouvé entre les deux commutateurs.");
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Chemin le plus court entre ").append(commutateurDepart).append(" et ").append(commutateurDestination).append(" : ");
            message.append(String.join(" -> ", cheminPlusCourt));
            afficherMessage(message.toString());
        }
    }

    private static List<String> trouverCheminPlusCourt(Map<String, Map<String, Integer>> graph, String commutateurDepart, String commutateurDestination) {
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> distanceMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(commutateurDepart);
        visited.add(commutateurDepart);
        parentMap.put(commutateurDepart, null);
        distanceMap.put(commutateurDepart, 0);

        while (!queue.isEmpty()) {
            String node = queue.poll();
            if (node.equals(commutateurDestination)) {
                return construireChemin(parentMap, commutateurDepart, commutateurDestination);
            }
            for (String neighbor : graph.get(node).keySet()) {
                int newDistance = distanceMap.get(node) + graph.get(node).get(neighbor);
                if (!visited.contains(neighbor) || newDistance < distanceMap.get(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, node);
                    distanceMap.put(neighbor, newDistance);
                }
            }
        }
        return new ArrayList<>();
    }

    private static List<String> construireChemin(Map<String, String> parentMap, String start, String end) {
        List<String> chemin = new ArrayList<>();
        String current = end;
        while (current != null) {
            chemin.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(chemin);
        return chemin;
    }

    public static void afficherTablesDeRoutages() {
        if (!graph.isEmpty()) {
            JFrame frame = new JFrame("Tables de routage");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 300);

            JPanel panel = new JPanel(new BorderLayout());

            textArea = new JTextArea(10, 30);
            textArea.setEditable(false);
            afficherTablesDeRoutagesDansTextArea();

            JScrollPane scrollPane = new JScrollPane(textArea);
            panel.add(scrollPane, BorderLayout.CENTER);

            frame.getContentPane().add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } else {
            afficherMessage("Aucun commutateur pour afficher les tables de routage.");
        }
    }
    
    static class VoisinAvecPoids implements Comparable<VoisinAvecPoids> {
        String nomVoisin;
        int poidsTotal;

        public VoisinAvecPoids(String nomVoisin, int poidsTotal) {
            this.nomVoisin = nomVoisin;
            this.poidsTotal = poidsTotal;
        }

        @Override
        public int compareTo(VoisinAvecPoids autre) {
            return Integer.compare(this.poidsTotal, autre.poidsTotal);
        }
    }

    private static void afficherTablesDeRoutagesDansTextArea() {
        textArea.setText("");
        for (String commutateurCourant : graph.keySet()) {
            textArea.append("Table de routage pour le commutateur " + commutateurCourant + " :\n");

            Map<String, Integer> voisins = graph.get(commutateurCourant);

            Map<String, List<VoisinAvecPoids>> routes = new HashMap<>();
            for (String destination : graph.keySet()) {
                if (!destination.equals(commutateurCourant)) {
                    List<VoisinAvecPoids> voisinsTries = new ArrayList<>();
                    for (Map.Entry<String, Integer> voisin : voisins.entrySet()) {
                        int poidsTotal = voisin.getValue(); // Poids de la liaison directe
                        poidsTotal += trouverCheminPlusCourt(graph, voisin.getKey(), destination).size(); // Poids du reste du chemin
                        voisinsTries.add(new VoisinAvecPoids(voisin.getKey(), poidsTotal));
                    }
                    Collections.sort(voisinsTries);
                    routes.put(destination, voisinsTries);
                }
            }

            for (Map.Entry<String, List<VoisinAvecPoids>> entry : routes.entrySet()) {
                String destination = entry.getKey();
                List<VoisinAvecPoids> voisinsPourDestination = entry.getValue();
                StringBuilder voisinsString = new StringBuilder();
                for (VoisinAvecPoids voisin : voisinsPourDestination) {
                    voisinsString.append(voisin.nomVoisin).append(", ");
                }
                if (voisinsString.length() > 0) {
                    voisinsString.setLength(voisinsString.length() - 2);
                }
                textArea.append("    Destination: " + destination + " -> " + voisinsString + "\n");
            }
            textArea.append("\n");
        }
    }


    private static void afficherMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static Map<String, Map<String, Integer>> getGraph() {
        return graph;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Fenetre::new);
    }
}
