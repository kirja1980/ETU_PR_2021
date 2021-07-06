package JavaCode.UI;

import JavaCode.resources.Graph;
import JavaCode.resources.UserMeta;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import static java.awt.Cursor.*;

public class MainWindow extends JFrame {
    static Graph graph = new Graph();
    public static final UserMeta userMeta = new UserMeta();

    static DrawPanel graphCreatePanel = addGraphCreatePanel();
    static DrawPanel algorithmCreatePanel = addAlgorithmCreatePanel();
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MainWindow() {
        initUI();
    }
    private void initUI() {

        // Устанавливаем настройки окна приложения.
        addSettingsToPane(this);
        // Cоздаём меню.
        addMenuToPane(this);
        // Создание панели для отображения графа.
        addDisplayPanelToPane(this);
        // Создание эвенов.
        addEventsToPane(this);
        revalidate();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void addSettingsToPane(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension appSize = new Dimension(toolkit.getScreenSize().width / 2, toolkit.getScreenSize().height / 2);
        Dimension screenSize = toolkit.getScreenSize();

        // Устанавливаем размеры окна.
        frame.setBounds(screenSize.width / 2 - appSize.width / 2, screenSize.height / 2 - appSize.height / 2, appSize.width, appSize.height);

        // Устанавливаем горизонтальный слой.
        frame.getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    public static void addDisplayPanelToPane(JFrame frame) {
        Box box = Box.createHorizontalBox();
        box.add(graphCreatePanel);
        box.add(algorithmCreatePanel);
        frame.setContentPane(box);
    }

    private static DrawPanel addGraphCreatePanel() {
        DrawPanel drawPanel = new DrawPanel();
        drawPanel.setBorder(new TitledBorder(null, "Граф", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        return drawPanel;
    }

    private static DrawPanel addAlgorithmCreatePanel() {
        DrawPanel drawPanel = new DrawPanel();
        drawPanel.setBorder(new TitledBorder(null, "Алгоритм", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        return drawPanel;
    }

    public static void addMenuToPane(JFrame frame) {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu file = new JMenu("Файл");
        jMenuBar.add(file);

        JMenuItem load = new JMenuItem("Загрузить");
        JMenuItem save = new JMenuItem("Созранить");

        file.add(load);
        file.add(save);

        JMenu editing = new JMenu("Создание");
        jMenuBar.add(editing);

        JMenuItem creating = new JMenuItem("Добавление вершин");
        JMenuItem deleting = new JMenuItem("Удаление вершин");
        JMenuItem moving = new JMenuItem("Перемещение вершин");
        JMenuItem linking = new JMenuItem("Добавление рёбер");

        editing.add(creating);
        editing.add(deleting);
        editing.add(moving);
        editing.add(linking);

        creating.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Creating);
        deleting.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Deleting);
        moving.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Moving);
        linking.addActionListener(e -> userMeta.editMode = UserMeta.EditMode.Linking);

        JMenu algorithm = new JMenu("Алгоритм");
        jMenuBar.add(algorithm);

        JMenuItem run = new JMenuItem("Вывести результат");
        JMenuItem debug = new JMenuItem("Вывести пошагово");

        algorithm.add(run);
        algorithm.add(debug);

        frame.setJMenuBar(jMenuBar);
    }

    public static void addEventsToPane(JFrame frame) {
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                switch (userMeta.editMode) {
                    case None:
                        break;
                    case Creating:
                        if (graphCreatePanel.contains(e.getX() - 7, e.getY() - 52)) {
                            graph.addNode(e.getX() - 7, e.getY() - 52);
                        }
                        break;
                    case Deleting:
                        Graph.Node node = graph.getNodeOnClicked(e.getX(), e.getY());
                        graph.deleteNode(node);
                        break;
                    case Linking:
                        userMeta.finishPoint = new Point2D.Double(e.getX(), e.getY());
                        Graph.Node first = graph.getNodeOnClicked(userMeta.startPoint.x, userMeta.startPoint.y);
                        Graph.Node second = graph.getNodeOnClicked(userMeta.finishPoint.x, userMeta.finishPoint.y);

                        graph.addEdge(first, second);

                        userMeta.startPoint = null;
                        userMeta.finishPoint = null;
                        break;
                }
            }
        });

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (userMeta.editMode) {
                    case Creating, Deleting, Moving, None -> {
                    }
                    case Linking -> userMeta.startPoint = new Point2D.Double(e.getX(), e.getY());
                }
            }
        });

        // Перемещение вершин.
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (userMeta.editMode) {
                    case Moving -> {
                        Graph.Node node = graph.getNodeOnClicked(e.getX(), e.getY());
                        if (node != null && graphCreatePanel.contains(e.getX() - 7, e.getY() - 52)) {
                            node.move(e.getX(), e.getY());
                        }
                    }
                    case Linking -> userMeta.finishPoint = new Point2D.Double(e.getX(), e.getY());
                }
            }
        });

        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (userMeta.editMode == UserMeta.EditMode.Moving) {
                    Graph.Node node = graph.getNodeOnClicked(e.getX(), e.getY());
                    frame.setCursor(getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    if (node != null) {
                        frame.setCursor(getPredefinedCursor(Cursor.MOVE_CURSOR));
                    }
                }
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        var mainWindow = new MainWindow();

    }
}