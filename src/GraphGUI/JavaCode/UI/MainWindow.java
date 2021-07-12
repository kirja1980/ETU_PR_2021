package JavaCode.UI;

import JavaCode.resources.DataAlgorithm;
import JavaCode.resources.Graph;
import JavaCode.resources.UserMeta;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import static java.awt.Cursor.*;

public class MainWindow extends JFrame {
    public Graph graph = new Graph();
    public static UserMeta userMeta = new UserMeta();

    public DrawPanel graphCreatePanel = addGraphCreatePanel();
    public AlgorithmPanel algorithmCreatePanel = addAlgorithmCreatePanel();



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MainWindow() {
        initUI();
    }
    private void initUI() {

        // Устанавливаем настройки окна приложения.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension appSize = new Dimension((int)(toolkit.getScreenSize().width / 1.5d), (int)(toolkit.getScreenSize().height / 1.5d));
        Dimension screenSize = toolkit.getScreenSize();

        // Устанавливаем размеры окна.
        setBounds(screenSize.width / 2 - appSize.width / 2, screenSize.height / 2 - appSize.height / 2, appSize.width, appSize.height);

        getContentPane().setLayout(new GridLayout());
        // Cоздаём меню.
        addMenuToPane();
        // Создание панели для отображения графа.
        addPanelsToPane();

        revalidate();
    }

    private void addPanelsToPane() {
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());

        JRadioButton creating = new JRadioButton("Создать вершину");
        JRadioButton deleting = new JRadioButton("Удалить вершину");
        JRadioButton moving = new JRadioButton("Переместить вершину");
        JRadioButton linking = new JRadioButton("Добавить ребро");

        ButtonGroup editing = new ButtonGroup();
        editing.add(creating);
        editing.add(deleting);
        editing.add(moving);
        editing.add(linking);

        creating.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Creating);
        deleting.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Deleting);
        moving.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Moving);
        linking.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Linking);

        JPanel editingPanel = new JPanel();
        editingPanel.setLayout(new GridLayout(1, 3));

        editingPanel.add(creating);
        editingPanel.add(deleting);
        editingPanel.add(moving);
        editingPanel.add(linking);

        leftPanel.add(editingPanel, BorderLayout.NORTH);

        leftPanel.setBorder(new TitledBorder(null, "Окно создания графа", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        leftPanel.add(graphCreatePanel, BorderLayout.CENTER);

        rightPanel.setBorder(new TitledBorder(null, "Окно просмотра алгоритма", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        rightPanel.add(algorithmCreatePanel, BorderLayout.CENTER);

        getContentPane().add(leftPanel);
        getContentPane().add(rightPanel);
    }

    private DrawPanel addGraphCreatePanel() {
        DrawPanel drawPanel = new DrawPanel();
        drawPanel.setGraph(graph);
        drawPanel.setManaged(true);
        drawPanel.setCurved(false);

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                switch (userMeta.editMode) {
                    case None:
                        break;
                    case Creating:
                        if (graphCreatePanel.contains(e.getX(), e.getY())) {
                            graph.addNode(e.getX(), e.getY());
                        }
                        break;
                    case Deleting:
                        Graph.Node node = graph.getNodeOnFocus(e.getX(), e.getY());
                        graph.deleteNode(node);
                        break;
                    case Linking:
                        userMeta.finishPoint = new Point2D.Double(e.getX(), e.getY());
                        Graph.Node first = graph.getNodeOnFocus(userMeta.startPoint.x, userMeta.startPoint.y);
                        Graph.Node second = graph.getNodeOnFocus(userMeta.finishPoint.x, userMeta.finishPoint.y);
                        graph.addEdge(first, second);
                        userMeta.startPoint = null;
                        userMeta.finishPoint = null;
                        break;
                    case Moving:
                        userMeta.buffer = null;
                }
            }
        });

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getX() + " " + e.getY());
                switch (userMeta.editMode) {
                    case Creating, Deleting, None -> {
                    }
                    case Linking -> userMeta.startPoint = new Point2D.Double(e.getX(), e.getY());
                    case Moving -> userMeta.buffer = graph.getNodeOnFocus(e.getX(), e.getY());
                }
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (userMeta.editMode) {
                    case Moving -> {
                        //Graph.Node node = graph.getNodeOnFocus(e.getX(), e.getY());
                        //if (node != null && graphCreatePanel.contains(e.getX(), e.getY())) {
                        //    node.move(e.getX(), e.getY());
                        //}
                        if (userMeta.buffer != null && graphCreatePanel.contains(e.getX(), e.getY())) {
                            userMeta.buffer.move(e.getX(), e.getY());
                        }
                    }
                    case Linking -> userMeta.finishPoint = new Point2D.Double(e.getX(), e.getY());
                }
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (userMeta.editMode == UserMeta.EditMode.Moving) {
                    Graph.Node node = graph.getNodeOnFocus(e.getX(), e.getY());
                    setCursor(getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    if (node != null) {
                        setCursor(getPredefinedCursor(Cursor.MOVE_CURSOR));
                    }
                }
            }
        });

        return drawPanel;
    }

    private AlgorithmPanel addAlgorithmCreatePanel() {
        AlgorithmPanel algorithmPanel = new AlgorithmPanel();
        return algorithmPanel;
    }

    public void addMenuToPane() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu file = new JMenu("Файл");
        jMenuBar.add(file);

        JMenuItem load = new JMenuItem("Загрузить");

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder input = new StringBuilder();

                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setDialogTitle("Выбор директории");
                // Определение режима - только каталог
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(MainWindow.this);

                algorithmCreatePanel.setTextjLabel(graph.loadGraph(fileChooser, result));
            }
        });

        JMenuItem save = new JMenuItem("Сохранить");

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setDialogTitle("Сохранение файла");

                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(MainWindow.this);

                algorithmCreatePanel.setTextjLabel(graph.saveGraph(fileChooser, result));
            }
        });

        file.add(load);
        file.add(save);

        JMenu algorithm = new JMenu("Алгоритм");
        jMenuBar.add(algorithm);

        JMenuItem run = new JMenuItem("Вывести результат");
        JMenuItem debug = new JMenuItem("Вывести пошагово");
        JMenuItem restart = new JMenuItem("Заново");

        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<DataAlgorithm> dataAlgorithm = graph.Algorithm();
                algorithmCreatePanel.setStepIndex(dataAlgorithm.size() - 1);
                algorithmCreatePanel.setDataAlgorithms(dataAlgorithm);
            }
        });

        debug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<DataAlgorithm> dataAlgorithm = graph.Algorithm();
                algorithmCreatePanel.setStepIndex(0);
                algorithmCreatePanel.setDataAlgorithms(dataAlgorithm);
            }
        });

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                graph = new Graph();
                userMeta = new UserMeta();

                graphCreatePanel = addGraphCreatePanel();
                algorithmCreatePanel = addAlgorithmCreatePanel();
                addPanelsToPane();
                revalidate();
            }
        });

        algorithm.add(run);
        algorithm.add(debug);
        algorithm.addSeparator();
        algorithm.add(restart);

        setJMenuBar(jMenuBar);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }
}