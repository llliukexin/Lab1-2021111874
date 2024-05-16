import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;



public class TextToGraph extends JFrame{

    //protected static Graph graph;
    private static HashMap<String, Map<String, Integer>> textToGraph = new HashMap<>();
    private JPanel mainPanel, topPanel, centerPanel;

    //private TextToGraph textToGraph;
    private File selectedFile;

    public TextToGraph() {
        setTitle("Text Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel = new JPanel();

        JButton chooseFileButton = new JButton("Choose Text File");
        chooseFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAnalysis(0);
            }
        });
        topPanel.add(chooseFileButton);

        // 创建按钮并添加监听器
        JButton buildGraphButton = new JButton("Build Graph");
        buildGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAnalysis(1);
            }
        });
        topPanel.add(buildGraphButton);

        JButton queryButton = new JButton("Query BridgeWords");
        topPanel.add(queryButton);
        queryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAnalysis(2);
            }
        });


        JButton generateTextButton = new JButton("Insert BridgeWords");
        generateTextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAnalysis(3);
            }
        });
        topPanel.add(generateTextButton);

        JButton shortestPathButton = new JButton("Calculate Shortest Path");
        shortestPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAnalysis(4);
            }
        });
        topPanel.add(shortestPathButton);

        JButton randomPathButton = new JButton("Random Path");
        randomPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAnalysis(5);
            }
        });
        topPanel.add(randomPathButton);

        JButton EXITBUTTON = new JButton("exit!");
        EXITBUTTON.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAnalysis(6);
            }
        });
        topPanel.add(EXITBUTTON);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TextToGraph ui = new TextToGraph();
                ui.setVisible(true);
            }
        });
    }
    private void startAnalysis(int choice) {
        centerPanel.removeAll();
        centerPanel.repaint();
        JLabel label1 = new JLabel("请输入第一个单词：");
        JTextField textField1 = new JTextField(20);
        JLabel label2 = new JLabel("请输入第二个单词：");
        JTextField textField2 = new JTextField(20);
        JButton button = new JButton("查询");
        JTextArea textArea = new JTextArea(null, 50, 100);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        textArea.setEditable(false);
        switch (choice) {
            case 0:
                centerPanel.add(textArea);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
                int result = fileChooser.showOpenDialog(TextToGraph.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    textArea.append("Selected file: " + selectedFile.getAbsolutePath() + "\n");
                }
                break;
            case 1:
                if (selectedFile != null) {
                    try {
                        String dotPath = "./result/directed_graph.dot";
                        String imagePath = "./result/directed_graph.png";
                        buildDirectedGraph(selectedFile.getAbsolutePath());
                        generateDotFile(dotPath);
                        convertDotToImage(dotPath,imagePath);
                        displayImage(imagePath);
                        textArea.append("Directed graph built successfully.\n");
                    } catch (Exception ex) {
                        textArea.append("Error building directed graph: " + ex.getMessage() + "\n");
                    }
                } else {
                    textArea.append("Please choose a text file first.\n");
                }
                break;
            case 2:
                centerPanel.add(label1);
                centerPanel.add(textField1);
                centerPanel.add(label2);
                centerPanel.add(textField2);
                centerPanel.add(button);
                centerPanel.add(textArea);
                // 添加按钮来触发操作
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String word1 = textField1.getText().toLowerCase();
                        String word2 = textField2.getText().toLowerCase();
                        if (word1 == null || word1.isEmpty() || word2 == null || word2.isEmpty()) {
                            textArea.append("No words entered. Bridge word query canceled.\n");
                            return;
                        }
                        if (!textToGraph.containsKey(word1) && !textToGraph.containsKey(word2)) {
                            textArea.append("No " + word1 + " or " + word2 + " in the graph!\n");
                            return;
                        } else if (!textToGraph.containsKey(word1)) {
                            textArea.append("No " + word1 + " in the graph!\n");
                            return;
                        } else if (!textToGraph.containsKey(word2)) {
                            textArea.append("No " + word2 + " in the graph!\n");
                            return;
                        }
                        // 执行桥接词查询
                        List<String> bridgeWords = queryBridgeWords(word1.trim().toLowerCase(), word2.trim().toLowerCase(), true);
                        if (bridgeWords.isEmpty()) {
                            textArea.append("No bridge words found.\n");
                        } else if(bridgeWords.size()==1){
                            textArea.append("The bridge words from " + word1 + " to " + word2 + " is: "+bridgeWords.get(0) + "\n");
                        }else{
                            textArea.append("The bridge words from " +word1 + " to " +word2 + " are: ");
                            for (int i = 0; i < bridgeWords.size(); i++) {
                                textArea.append(bridgeWords.get(i));
                                if (i < bridgeWords.size() - 2) {
                                    textArea.append(", ");
                                } else if (i == bridgeWords.size() - 2) {
                                    textArea.append(", and ");
                                }
                            }
                            textArea.append("\n");
                        }
                    }
                });
                break;
            case 3:
                label1.setText("请输入一行文本：");
                centerPanel.add(label1);
                textField1.setColumns(100);
                centerPanel.add(textField1);
                button.setText("生成新文本");
                centerPanel.add(button);
                centerPanel.add(textArea);
                // 添加按钮来触发操作
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String newText = textField1.getText().toLowerCase();
                        // 如果用户未输入任何内容，则不进行操作
                        if (newText == null || newText.isEmpty()) {
                            textArea.append("No text entered. Bridge word insertion canceled.\n");
                            return;
                        }

                        // 生成带有桥接词的新文本
                        String newTextWithBridgeWords = insertBridgeWords(newText);
                        textArea.append("your text:" + newText + "\n");
                        textArea.append("new text:" + newTextWithBridgeWords + "\n");
                    }
                });
                break;
            case 4:
                centerPanel.add(label1);
                centerPanel.add(textField1);
                centerPanel.add(label2);
                centerPanel.add(textField2);
                button.setText("计算最短路径");
                centerPanel.add(button);
                centerPanel.add(textArea);
                // 添加按钮来触发操作
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String word1 = textField1.getText().toLowerCase();
                        String word2 = textField2.getText().toLowerCase();
                        String result = "";
                        String dotFilePath = "./result/marked_graph.dot"; // 标注后的 DOT 文件路径
                        if(word2.isEmpty() && textToGraph.containsKey(word1)){
                            textArea.append(shortestPathsFromSingleWord(word1));
                        } else if (textToGraph.containsKey(word1) && textToGraph.containsKey(word2)) {
                            List<String> shortestPath = calcShortestPath(word1, word2);
                            if (!shortestPath.isEmpty()) {
                                textArea.append("Shortest path from " + word1 + " to " + word2 + ": " + WordListFormatter(shortestPath) + "\n");
                                dotFilePath = markAndDisplayShortestPath(shortestPath,dotFilePath);
                                if (dotFilePath != null) {
                                    textArea.append("Shortest path image generated: " + dotFilePath + "\n");
                                } else {
                                    textArea.append("Failed to generate shortest path image.\n");
                                }
                            } else {
                                textArea.append("No path found between " + word1 + " and " + word2 + "\n");
                            }
                        }else {
                            textArea.append("Invalid input.\n");
                        }
                    }
                });
                break;
            case 5:
                button.setText("开始随机游走");
                centerPanel.add(button);
                centerPanel.add(textArea);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String dotRandFilePath = "./result/marked_rand_graph.dot"; // 标注后的 DOT 文件路径
                        List<String> randomPath = randomTraversalForGUI(textArea);
                        dotRandFilePath = markAndDisplayShortestPath(randomPath,dotRandFilePath);
//                textToGraph.displayImage(dotRandFilePath);
                        textArea.append(WordListFormatter(randomPath) + "\n");
                        textArea.append("Random traversal completed. Results written to './result/random_traversal.txt'.\n");

                    }
                });
                break;
            case 6:
                System.exit(0);
                break;
            default:
        }
        centerPanel.updateUI();
    }
    public static void addNode(String node) {
        textToGraph.putIfAbsent(node, new HashMap<>());
    }
    //添加边
    public static void addEdge(String source, String destination) {
        textToGraph.get(source).merge(destination, 1, Integer::sum);
    }
    //功能一：构建有向图
    public static void buildDirectedGraph(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" "); // 将每行内容添加到 content 中，并添加空格作为单词间的分隔符
            }
        }
        // 将非字母字符替换为空格，并转换为小写
        String processedContent = content.toString().replaceAll("[^a-zA-Z\\n\\r]", " ").toLowerCase();
        String[] words = processedContent.split("\\s+"); // 按空格分割单词
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i];
            String nextWord = words[i + 1];
            addNode(currentWord);
            addNode(nextWord);
            addEdge(currentWord, nextWord);
        }
    }

    //打印有向图
//    public void printGraph() {
//        System.out.println("Vertices and edges in the graph:");
//        for (Map.Entry<String, Map<String, Integer>> entry : textToGraph.entrySet()) {
//            String vertex = entry.getKey();
//            Map<String, Integer> edges = entry.getValue();
//            System.out.print(vertex + ": ");
//            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
//                String destination = edge.getKey();
//                int weight = edge.getValue();
//                System.out.print("(" + vertex + " -> " + destination + ": " + weight + "), ");
//            }
//            System.out.println();
//        }
//    }
    //功能2：有向图可视化
    public void generateDotFile(String dotFilePath) {
        try (PrintWriter writer = new PrintWriter(dotFilePath)) {
            writer.println("digraph G {");
            for (Map.Entry<String, Map<String, Integer>> entry : textToGraph.entrySet()) {
                String vertex = entry.getKey();
                Map<String, Integer> edges = entry.getValue();
                for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                    String destination = edge.getKey();
                    int weight = edge.getValue();
                    System.out.println(weight);
                    writer.println("\t" + vertex + " -> " + destination + " [label=\"" + weight + "\"];");
                }
            }
            writer.println("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void convertDotToImage(String dotFilePath, String imageFilePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", dotFilePath, "-o", imageFilePath);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
//            System.out.println(exitCode);
            if (exitCode == 0) {
                System.out.println("Image file generated: " + imageFilePath);
            } else {
                System.out.println("Failed to generate image file.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //功能三：桥接词查询
    public List<String> queryBridgeWords(String start, String end, Boolean print) {
        List<String> bridgeWords = new ArrayList<>();
        if (!textToGraph.containsKey(start) && !textToGraph.containsKey(end)&&print)
        {
            return bridgeWords;
        }
        if (!textToGraph.containsKey(start) && print) {
            return bridgeWords;
        }
        if (!textToGraph.containsKey(end) && print) {
            return bridgeWords;
        }
        Map<String, Integer> edges1 = textToGraph.get(start);
        List<String> neighbors1 = new ArrayList<>(edges1.keySet());
        //System.out.println(neighbors1.contains("life"));
        // Print other information
        for(String neighbor1:neighbors1)
        {
            Map<String, Integer> edges2 = textToGraph.get(neighbor1);
            List<String> neighbors2=new ArrayList<>(edges2.keySet());
            if(neighbors2.contains(end))
            {
                bridgeWords.add(neighbor1);
            }
        }
        return bridgeWords;
    }

    //功能4：根据bridge word生成新文本
    public String insertBridgeWords(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+"); // 按空格分割单词
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i];
            String nextWord = words[i + 1];
            result.append(currentWord).append(" ");
            if (textToGraph.containsKey(currentWord) && textToGraph.containsKey(nextWord)) {
                List<String> bridgeWords = queryBridgeWords(currentWord, nextWord, false);
                if (!bridgeWords.isEmpty()) {
                    // 如果存在桥接词，则随机选择一个桥接词插入
                    Random random = new Random();
                    String selectedBridgeWord = new ArrayList<>(bridgeWords).get(random.nextInt(bridgeWords.size()));
                    result.append(selectedBridgeWord).append(" ");
                }
            }
        }
        result.append(words[words.length - 1]); // 添加最后一个单词
        return result.toString();
    }

    //功能5：最短路径
    public static List<String> calcShortestPath(String start, String end) {
        List<String> shortestPath = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> distance = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);
        distance.put(start, 0);
//        boolean found = false;

        while (!queue.isEmpty() ) {
            String current = queue.poll();
            Map<String, Integer> neighbors = textToGraph.getOrDefault(current, Collections.emptyMap());

            for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                String next = neighbor.getKey();
                int weight = neighbor.getValue();
                int newDistance = distance.get(current) + weight;

                if (!visited.contains(next) || newDistance < distance.getOrDefault(next, Integer.MAX_VALUE)) {
                    visited.add(next);
                    parent.put(next, current);
                    distance.put(next, newDistance);
                    queue.add(next);
                }
            }
        }

        if (!visited.contains(end)) {
            System.out.println("No path exists between " + start + " and " + end);
            return shortestPath;
        }

        String current = end;
        while (!current.equals(start)) {
            shortestPath.add(0, current);
            current = parent.get(current);
        }
        shortestPath.add(0, start);
        return shortestPath;
    }

    public static String markAndDisplayShortestPath(List<String> shortestPath,String markedDotFilePath) {
//        String dotFilePath = "./result/marked_graph.dot"; // 标注后的 DOT 文件路径
        try {
            // 写入标注后的 DOT 文件
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(markedDotFilePath))) {
                bw.write("digraph G {");
                try (BufferedReader br = new BufferedReader(new FileReader("./result/directed_graph.dot"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.contains("->")) {
                            String[] parts = line.split("->");
                            String fromNode = parts[0].trim();
                            String toNode = parts[1].trim().split("\\[")[0].trim();
                            // 如果当前边是最短路径上的一部分，则修改箭头颜色为红色
                            if (shortestPath.contains(fromNode) && shortestPath.contains(toNode) && !fromNode.equals(shortestPath.get(shortestPath.size() - 1))) {
                                if (parts[1].trim().endsWith(";")) {
                                    // 如果是，删除最后一个字符';'
                                    parts[1] = parts[1].trim().substring(0, parts[1].trim().length() - 1);
                                }
                                bw.write("\t"+parts[0].trim() + " -> " + parts[1] + " [color=red];");
                                bw.newLine();
                            } else {
                                bw.write(line);
                                bw.newLine();
                            }
                        }
                    }
                    bw.write("}"); // 结束有向图
                }
            }

            // 调用 Graphviz 生成 PNG 文件
            String pngName = markedDotFilePath.substring(0, markedDotFilePath.lastIndexOf(".")) + ".png";
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", markedDotFilePath, "-o", pngName);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Image file generated: " + pngName);
                // 在屏幕上显示图像
                displayImage(pngName);
            } else {
                System.out.println("Failed to generate image file.");
            }

            return markedDotFilePath; // 返回标注后的 DOT 文件路径
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String shortestPathsFromSingleWord(String word) {
        StringBuilder sb = new StringBuilder();
        for (String node : textToGraph.keySet()) {
            if (!node.equals(word)) {
                List<String> shortestPath = calcShortestPath(word, node);
                if(shortestPath.isEmpty()){
                    sb.append("no path from " + word + " to " + node + ".\n");
                }else {
                    sb.append("Shortest path from " + word + " to " + node + ": " + WordListFormatter(shortestPath) + "\n");
                }
//                System.out.println("Shortest path from " + word + " to " + node + ": " + WordListFormatter(shortestPath));
            }
        }
        return sb.toString();
    }

    public static String  WordListFormatter (List<String> wordList){
        StringBuilder formattedString = new StringBuilder();
        for (int i = 0; i < wordList.size(); i++) {
            formattedString.append(wordList.get(i));
            if (i < wordList.size() - 1) {
                formattedString.append(" → ");
            }
        }
        return formattedString.toString();
    }

    // 在屏幕上显示图像
    public static void displayImage(String imagePath) {
        // 读取 PNG 图像文件
        ImageIcon icon = new ImageIcon(imagePath);

        // 创建标签，用于显示图像
        JLabel label = new JLabel(icon);

        // 创建窗口
        JFrame frame = new JFrame();
        frame.setTitle("PNG Image Viewer");
        frame.setSize(600, 600);

        // 设置窗口关闭时不退出程序
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 添加标签到窗口
        frame.getContentPane().add(label, BorderLayout.CENTER);

        // 设置窗口可见
        frame.setVisible(true);
    }


    //功能6：游走路径
    public static void randomTraversal() {
        try {
            // 创建文件写入器
            PrintWriter writer = new PrintWriter(new FileWriter("./result/random_traversal.txt"));

            Random random = new Random();
            List<String> nodesVisited = new ArrayList<>(); // 记录经过的节点
            Set<String> edgesVisited = new HashSet<>(); // 记录经过的边

            // 随机选择起始节点
            List<String> nodes = new ArrayList<>(textToGraph.keySet());
            String currentNode = nodes.get(random.nextInt(nodes.size()));
            nodesVisited.add(currentNode);

            // 开始随机遍历
            while (true) {
                // 检查用户是否希望停止遍历
                if (shouldStopTraversal()) {
                    break;
                }

                // 获取当前节点的出边
                Map<String, Integer> edges = textToGraph.get(currentNode);
                if (edges == null || edges.isEmpty()) {
                    break; // 当前节点没有出边，遍历结束
                }

                // 随机选择下一个节点
                List<String> nextNodes = new ArrayList<>(edges.keySet());
                String nextNode = nextNodes.get(random.nextInt(nextNodes.size()));

                // 记录经过的边
                String edge = currentNode + " -> " + nextNode;
                if (edgesVisited.contains(edge)) {
                    break; // 出现重复的边，遍历结束
                }
                edgesVisited.add(edge);

                // 记录经过的节点
                nodesVisited.add(nextNode);

                // 更新当前节点
                currentNode = nextNode;
            }

            // 将遍历的节点写入文件
            for (String node : nodesVisited) {
                writer.print(node+" ");
            }
            writer.println();
            // 关闭文件写入器
            writer.close();

            // 提示用户遍历已完成并文件已生成
            System.out.println("Random traversal completed. Results written to './result/random_traversal.txt'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 检查用户是否希望停止随机游走
    private static boolean shouldStopTraversal() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Press 'q' to stop random traversal, or press any other key to continue: ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("q");
    }

    public static List<String> randomTraversalForGUI(JTextArea textArea) {
        List<String> nodesVisited = new ArrayList<>(); // 记录经过的节点
        try {
            // 创建文件写入器
            PrintWriter writer = new PrintWriter(new FileWriter("./result/random_traversal.txt"));
            Random random = new Random();
            // 随机选择起始节点
            List<String> nodes = new ArrayList<>(textToGraph.keySet());
            final String[] currentNode = {nodes.get(random.nextInt(nodes.size()))}; // 声明为 final 的数组(final String不行！

//            String currentNode = nodes.get(random.nextInt(nodes.size()));
            // first node
            nodesVisited.add(currentNode[0]);


            // begin
            while (true) {
                // 在控制台输出当前顶点
                System.out.println("Current node: " + currentNode[0]);

                // 在 UI 中显示当前顶点
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textArea.append("Current node: " + currentNode[0] + "\n");
                        textArea.setCaretPosition(textArea.getDocument().getLength()); // 将文本区域滚动到最后一行
                    }
                });
                // continue?
                int option = JOptionPane.showConfirmDialog(null, "Continue traversal?", "Continue", JOptionPane.YES_NO_OPTION);
                if (option != JOptionPane.YES_OPTION) {
//                    System.out.println("3");
                    textArea.append("quit!\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    break;
                }

                // 获取当前节点的出边
                Map<String, Integer> edges = textToGraph.get(currentNode[0]);
                if (edges == null || edges.isEmpty()) {
//                    System.out.println("1");
                    textArea.append("no edge!\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    break; // 当前节点没有出边，遍历结束
                }

                // 随机选择下一个节点
                List<String> nextNodes = new ArrayList<>(edges.keySet());
                String nextNode = nextNodes.get(random.nextInt(nextNodes.size()));

//                String edge = currentNode[0] + " -> " + nextNode;
                // 出现重复的顶点，遍历结束
                if (nodesVisited.contains(nextNode)) {
                    // 添加重复节点
                    nodesVisited.add(nextNode);
//                    System.out.println("2");
                    textArea.append("Current node: " + nextNode + "\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    textArea.append("node repeat!\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    break;
                }

                // 记录经过的节点
                nodesVisited.add(nextNode);

                // 更新当前节点
                currentNode[0] = nextNode;


            }

            // 将遍历的节点写入文件
            for (String node : nodesVisited) {
                writer.print(node+" ");
            }
            writer.println();
            // 关闭文件写入器
            writer.close();

            // 提示用户遍历已完成并文件已生成
//            System.out.println(nodesVisited);
//            System.out.println("Random traversal completed. Results written to './result/random_traversal.txt'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return WordListFormatter(nodesVisited);
        return nodesVisited;
    }


}
