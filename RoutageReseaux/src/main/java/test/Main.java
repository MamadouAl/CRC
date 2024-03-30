package test;

import java.util.HashMap;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Main {

    private static Map<String, Map<String, Integer>> graph = new HashMap<>();

    // Méthode pour initialiser et afficher le graphe avec GraphStream
    
    public static void afficherGraphAvecGraphStream() {
        // Création d'une instance de graphe GraphStream
        Graph graphStream = new SingleGraph("Graphe");

        // Ajout des nœuds et des arêtes au graphe
        for (String commutateur : graph.keySet()) {
            // Ajouter un nœud au graphe
            Node node = graphStream.addNode(commutateur);

            // Pour chaque voisin du commutateur actuel, ajouter une arête avec le poids de la liaison
            for (Map.Entry<String, Integer> voisin : graph.get(commutateur).entrySet()) {
                // Ajouter une arête du commutateur actuel vers le voisin avec le poids de la liaison comme attribut
            	// Ajouter une arête du commutateur actuel vers le voisin avec le poids de la liaison comme attribut
            	org.graphstream.graph.Edge edge = graphStream.addEdge(commutateur + "-" + voisin.getKey(), commutateur, voisin.getKey(), true);
            	edge.setAttribute("poids", voisin.getValue());

            }
        }

        // Affichage du graphe avec une visualisation GraphStream
        graphStream.display();
    }

    // Vos autres méthodes de gestion de graphe existantes...

    public static void main(String[] args) {
        // Appel de la méthode pour afficher le graphe avec GraphStream
    	System.setProperty("org.graphstream.ui", "swing");

        afficherGraphAvecGraphStream();
    }
}
