package JavaCode.resources;

import JavaCode.UI.MainWindow;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.plaf.IconUIResource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void saveGraph() {
        StringBuilder input = new StringBuilder();
        Graph graph = new Graph();

        MainWindow mainWindow = new MainWindow();
        {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setDialogTitle("Выбор директории");
            // Определение режима - только каталог
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(mainWindow);



            graph.addNode(0,0);
            graph.addNode(50, 20);
            graph.addNode(100, 110);
            graph.addNode(132165156,16518163);
            graph.addNode(0,654);
            graph.addNode(6562,0);
            graph.addNode(27,527);

            graph.saveGraph(fileChooser, result);
        }

        StringBuilder output = new StringBuilder("BEGIN " + graph.nodes.size() + "\n");
        for (Graph.Node node : graph.nodes.values()){
            output.append(node.ID).append(" ").append((int)node.x).append(" ").append((int)node.y).append("\n");
        }

        output.append("////////////////\n");
        for(Graph.Node node : graph.nodes.values()){
            output.append(node.ID).append(" : " + node.childs.size() + " -");
            for (Graph.Node child : node.childs){
                output.append(" ").append(child.ID);
            }
            output.append("\n");
        }
        output.append("END");






        List<Double> expected = new ArrayList<>();

        for (Graph.Node node : graph.nodes.values()){
            expected.add(node.x);
            expected.add(node.y);
        }

        graph.nodes.clear();

        StringBuilder input1 = new StringBuilder();

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Выбор директории");
        // Определение режима - только каталог
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(mainWindow);

        graph.loadGraph(fileChooser, result);

        List<Double> actual = new ArrayList<>();

        for (Graph.Node node : graph.nodes.values()){
            actual.add(node.x);
            actual.add(node.y);
        }

        if (result == 0)
            assertEquals(expected, actual);

    }

    @Test
    void loadGraph() {
        String test1 = "BEGIN 7\n" +
                "0 0 0\n" +
                "1 50 20\n" +
                "2 100 110\n" +
                "3 132165156 16518163\n" +
                "4 0 654\n" +
                "5 6562 0\n" +
                "6 27 527\n" +
                "////////////////\n" +
                "0 : 0 -\n" +
                "1 : 0 -\n" +
                "2 : 0 -\n" +
                "3 : 0 -\n" +
                "4 : 0 -\n" +
                "5 : 0 -\n" +
                "6 : 0 -\n" +
                "END";

        String test2 = "BEGIN 26\n" +
                "6 307 37\n" +
                "7 161 102\n" +
                "8 249 100\n" +
                "9 381 98\n" +
                "10 561 95\n" +
                "12 170 209\n" +
                "13 310 210\n" +
                "14 421 216\n" +
                "15 527 213\n" +
                "16 605 207\n" +
                "17 76 309\n" +
                "18 256 305\n" +
                "19 383 308\n" +
                "20 476 317\n" +
                "21 553 314\n" +
                "22 85 400\n" +
                "23 214 417\n" +
                "24 310 410\n" +
                "25 457 400\n" +
                "26 558 392\n" +
                "27 110 496\n" +
                "28 273 488\n" +
                "29 391 488\n" +
                "30 531 483\n" +
                "32 297 559\n" +
                "33 95 219\n" +
                "////////////////\n" +
                "6 : 5 - 8 7 9 10 32\n" +
                "7 : 1 - 33\n" +
                "8 : 2 - 7 9\n" +
                "9 : 3 - 14 12 32\n" +
                "10 : 5 - 9 14 15 16 21\n" +
                "12 : 2 - 7 18\n" +
                "13 : 3 - 12 18 19\n" +
                "14 : 3 - 20 13 19\n" +
                "15 : 2 - 14 32\n" +
                "16 : 1 - 15\n" +
                "17 : 1 - 32\n" +
                "18 : 4 - 19 17 22 24\n" +
                "19 : 3 - 25 24 26\n" +
                "20 : 2 - 19 21\n" +
                "21 : 2 - 23 32\n" +
                "22 : 3 - 29 30 25\n" +
                "23 : 1 - 32\n" +
                "24 : 1 - 27\n" +
                "25 : 1 - 32\n" +
                "26 : 0 -\n" +
                "27 : 1 - 32\n" +
                "28 : 1 - 32\n" +
                "29 : 1 - 32\n" +
                "30 : 1 - 32\n" +
                "32 : 0 -\n" +
                "33 : 2 - 18 10\n" +
                "END";
        String test3 = "BEGIN 26\n" +
                "6 307 37\n" +
                "7 161 102\n" +
                "8 249 100\n" +
                "9 381 98\n" +
                "10 561 95\n" +
                "12 170 209\n" +
                "13 310 210\n" +
                "14 421 216\n" +
                "15 527 213\n" +
                "16 605 207\n" +
                "17 76 309\n" +
                "18 256 305\n" +
                "19 383 308\n" +
                "20 476 317\n" +
                "21 553 314\n" +
                "22 85 400\n" +
                "23 214 417\n" +
                "24 310 410\n" +
                "25 457 400\n" +
                "26 558 392\n" +
                "27 110 496\n" +
                "28 273 488\n" +
                "29 391 488\n" +
                "30 531 483\n" +
                "32 297 559\n" +
                "33 95 219\n" +
                "////////////////\n" +
                "6 : 5 - 8 7 9 10 32\n" +
                "7 : 1 - 33\n" +
                "8 : 2 - 7 9\n" +
                "9 : 3 - 14 12 32\n" +
                "10 : 5 - 9 14 15 16 21\n" +
                "12 : 2 - 7 18\n" +
                "13 : 3 - 12 18 19\n" +
                "14 : 3 - 20 13 19\n" +
                "15 : 2 - 14 32\n" +
                "16 : 1 - 15\n" +
                "17 : 1 - 32\n" +
                "18 : 4 - 19 17 22 24\n" +
                "19 : 3 - 25 24 26\n" +
                "20 : 2 - 19 21\n" +
                "21 : 2 - 23 32\n" +
                "22 : 3 - 29 30 25\n" +
                "23 : 1 - 32\n" +
                "24 : 1 - 27\n" +
                "25 : 1 - 32\n" +
                "26 : 0 -\n" +
                "27 : 1 - 32\n" +
                "28 : 1 - 32\n" +
                "29 : 1 - 32\n" +
                "30 : 1 - 32\n" +
                "32 : 0 -\n" +
                "33 : 1 - 18\n" +
                "END";
        String test4 = "BEGIN 5\n" +
                "0 263 128\n" +
                "1 367 262\n" +
                "2 280 384\n" +
                "3 340 232\n" +
                "4 398 170\n" +
                "////////////////\n" +
                "0 : 0 -\n" +
                "1 : 0 -\n" +
                "2 : 0 -\n" +
                "3 : 0 -\n" +
                "4 : 0 -\n" +
                "END";
        String test5 = "BEGIN 5\n" +
                "1 304 151\n" +
                "2 107 289\n" +
                "4 336 479\n" +
                "5 171 477\n" +
                "6 386 294\n" +
                "/////////////////\n" +
                "1 : 0 -\n" +
                "2 : 0 -\n" +
                "4 : 1 - 6\n" +
                "5 : 3 - 1 2 4\n" +
                "6 : 1 - 1\n" +
                "END";
        List<Boolean> expected = new ArrayList<>();

        expected.add(test1.matches("^BEGIN \\d+\n(\\d+ \\d+ \\d+\n){"+7+"}[/]{16}\n(\\d+ : \\d+ -( \\d+)*\n){"+7+"}END$"));
        expected.add(test2.matches("^BEGIN \\d+\n(\\d+ \\d+ \\d+\n){"+26+"}[/]{16}\n(\\d+ : \\d+ -( \\d+)*\n){"+26+"}END$"));
        expected.add(test3.matches("^BEGIN \\d+\n(\\d+ \\d+ \\d+\n){"+26+"}[/]{16}\n(\\d+ : \\d+ -( \\d+)*\n){"+26+"}END$"));
        expected.add(test4.matches("^BEGIN \\d+\n(\\d+ \\d+ \\d+\n){"+5+"}[/]{16}\n(\\d+ : \\d+ -( \\d+)*\n){"+5+"}END$"));
        expected.add(test5.matches("^BEGIN \\d+\n(\\d+ \\d+ \\d+\n){"+5+"}[/]{16}\n(\\d+ : \\d+ -( \\d+)*\n){"+5+"}END$"));

        List<Boolean> actual = new ArrayList<>();
        actual.add(true);
        actual.add(true);
        actual.add(true);
        actual.add(true);
        actual.add(false);

        Assert.assertEquals(expected,actual);
    }

    @Test
    void addNode() {
        Graph graph = new Graph();

        graph.addNode(0,0);
        graph.addNode(-50, -20);
        graph.addNode(100, 110);
        graph.addNode(132165156,16518163);
        graph.addNode(0,654);
        graph.addNode(6562,0);
        graph.addNode(-27,527);

        List<Double> expected = new ArrayList<>();




        for(Graph.Node node : graph.nodes.values()){
            expected.add(node.x);
        }
        for(Graph.Node node : graph.nodes.values()){
            expected.add(node.y);
        }

        Graph.Node node1 = new Graph.Node(0,0,0);
        Graph.Node node2 = new Graph.Node(1,-50, -20);
        Graph.Node node3 = new Graph.Node(2,100, 110);
        Graph.Node node4 = new Graph.Node(3,132165156,16518163);
        Graph.Node node5 = new Graph.Node(4,0,654);
        Graph.Node node6 = new Graph.Node(5,6562,0);
        Graph.Node node7 = new Graph.Node(6,-27,527);

        List<Double> actual = new ArrayList<>();
        actual.add(node1.x);
        actual.add(node2.x);
        actual.add(node3.x);
        actual.add(node4.x);
        actual.add(node5.x);
        actual.add(node6.x);
        actual.add(node7.x);

        actual.add(node1.y);
        actual.add(node2.y);
        actual.add(node3.y);
        actual.add(node4.y);
        actual.add(node5.y);
        actual.add(node6.y);
        actual.add(node7.y);


        Assert.assertEquals(expected, actual);




    }

    @Test
    void deleteNode() {
        Graph graph = new Graph();

        graph.addNode(0,0);
        graph.addNode(-50, -20);
        graph.addNode(100, 110);
        graph.addNode(132165156,16518163);
        graph.addNode(0,654);
        graph.addNode(6562,0);
        graph.addNode(-27,527);

        List<Double> expected = new ArrayList<>();




        for(Graph.Node node : graph.nodes.values()){
            expected.add(node.x);
        }
        for(Graph.Node node : graph.nodes.values()){
            expected.add(node.y);
        }

        Graph.Node node1 = new Graph.Node(0,0,0);
        Graph.Node node2 = new Graph.Node(1,-50, -20);
        Graph.Node node3 = new Graph.Node(2,100, 110);
        Graph.Node node4 = new Graph.Node(3,132165156,16518163);
        Graph.Node node5 = new Graph.Node(4,0,654);
        Graph.Node node6 = new Graph.Node(5,6562,0);
        Graph.Node node7 = new Graph.Node(6,-27,527);

        List<Double> actual = new ArrayList<>();
        actual.add(node1.x);
        actual.add(node2.x);
        actual.add(node3.x);
        actual.add(node4.x);
        actual.add(node5.x);
        actual.add(node6.x);
        actual.add(node7.x);

        actual.add(node1.y);
        actual.add(node2.y);
        actual.add(node3.y);
        actual.add(node4.y);
        actual.add(node5.y);
        actual.add(node6.y);
        actual.add(node7.y);


        Assert.assertEquals(expected, actual);

        graph.deleteNode(new Graph.Node(0,0,0));
        graph.deleteNode(new Graph.Node(1,-50, -20));

        expected.clear();

        for(Graph.Node node : graph.nodes.values()){
            expected.add(node.x);
        }
        for(Graph.Node node : graph.nodes.values()){
            expected.add(node.y);
        }

        actual.clear();

        actual.add(node3.x);
        actual.add(node4.x);
        actual.add(node5.x);
        actual.add(node6.x);
        actual.add(node7.x);

        actual.add(node3.y);
        actual.add(node4.y);
        actual.add(node5.y);
        actual.add(node6.y);
        actual.add(node7.y);

        Assert.assertEquals(expected, actual);

    }

    @Test
    void addEdge() {
        Graph graph = new Graph();

        graph.addNode(0,0);
        graph.addNode(-50, -20);
        graph.addNode(100, 110);
        graph.addNode(132165156,16518163);
        graph.addNode(0,654);
        graph.addNode(6562,0);
        graph.addNode(-27,527);

        graph.addEdge(graph.nodes.get(0),graph.nodes.get(0));
        graph.addEdge(graph.nodes.get(0),graph.nodes.get(1));
        graph.addEdge(graph.nodes.get(0),graph.nodes.get(2));
        graph.addEdge(graph.nodes.get(0),graph.nodes.get(3));
        graph.addEdge(graph.nodes.get(0),graph.nodes.get(4));
        graph.addEdge(graph.nodes.get(0),graph.nodes.get(5));

        graph.addEdge(graph.nodes.get(2),graph.nodes.get(0));
        graph.addEdge(graph.nodes.get(0),graph.nodes.get(1));
        graph.addEdge(graph.nodes.get(4),graph.nodes.get(2));
        graph.addEdge(graph.nodes.get(5),graph.nodes.get(3));
        graph.addEdge(graph.nodes.get(6),graph.nodes.get(4));
        graph.addEdge(graph.nodes.get(1),graph.nodes.get(5));


        List<Double> expected = new ArrayList<>();


        int counter = 0;

        for(Graph.Node node : graph.nodes.values()){
            for(Graph.Node child : node.childs){
                counter++;
            }
        }

        Graph.Node node1 = new Graph.Node(0,0,0);
        Graph.Node node2 = new Graph.Node(1,-50, -20);
        Graph.Node node3 = new Graph.Node(2,100, 110);
        Graph.Node node4 = new Graph.Node(3,132165156,16518163);
        Graph.Node node5 = new Graph.Node(4,0,654);
        Graph.Node node6 = new Graph.Node(5,6562,0);
        Graph.Node node7 = new Graph.Node(6,-27,527);

        List<Double> actual = new ArrayList<>();
        actual.add(node1.x);
        actual.add(node2.x);
        actual.add(node3.x);
        actual.add(node4.x);
        actual.add(node5.x);
        actual.add(node6.x);
        actual.add(node7.x);

        actual.add(node1.y);
        actual.add(node2.y);
        actual.add(node3.y);
        actual.add(node4.y);
        actual.add(node5.y);
        actual.add(node6.y);
        actual.add(node7.y);


        Assert.assertEquals(counter, 11);
    }
}