import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MyFrame extends JFrame implements ActionListener {

    Map<String, Integer> posisionMap = new HashMap<>();
    ArrayList<Worker> workerList = new ArrayList<>();       //слева лучше List<Worker>
    Set<String> sameSurnamesSet = new HashSet<>();

    private JTabbedPane tabbedPane;

    private JMenuItem openItem;
    private JMenuItem byPositionItem;
    private JMenuItem bySalaryItem;
    private JMenuItem sameSurnamesItem;
    private JMenuItem positionsItem;
    private JMenuItem searchItem;
    private JMenuItem numberItem;

    public MyFrame(String title) throws HeadlessException {
        super(title);

        setLocation(300, 200);
        setSize(400, 300);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenu dataMenu = new JMenu("Data");
        menuBar.add(dataMenu);

        openItem = new JMenuItem("Open...");
        fileMenu.add(openItem);
        byPositionItem = new JMenuItem("By position");
        bySalaryItem = new JMenuItem("By salary");
        sameSurnamesItem = new JMenuItem("Same surnames");
        positionsItem = new JMenuItem("Positions");
        searchItem = new JMenuItem("Search...");
        numberItem = new JMenuItem("Number...");
        dataMenu.add(byPositionItem);
        dataMenu.add(bySalaryItem);
        dataMenu.add(sameSurnamesItem);
        dataMenu.add(positionsItem);
        dataMenu.add(searchItem);
        dataMenu.add(numberItem);

        openItem.addActionListener(this);
        byPositionItem.addActionListener(this);
        bySalaryItem.addActionListener(this);
        sameSurnamesItem.addActionListener(this);
        positionsItem.addActionListener(this);
        searchItem.addActionListener(this);
        numberItem.addActionListener(this);

        tabbedPane = new JTabbedPane();

        add(tabbedPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        if(object == openItem)
            openItemAction();
        else if(object == byPositionItem)
            byPositionItemAction();
        else if(object == bySalaryItem)
            bySalaryItemAction();
        else if(object == sameSurnamesItem)
            sameSurnamesItemAction();
        else if(object == positionsItem)
            positionsItemAction();
        else if(object == searchItem)
            searchItemAction();
        else if(object == numberItem)
            numberItemAction();
    }

    private void openItemAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File inputFile = fileChooser.getSelectedFile();
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                MyHandler handler = new MyHandler();
                saxParser.parse(inputFile, handler);

                workerList = handler.getWorkerArrayList();
                posisionMap = handler.getPositionIntegerMap();
                sameSurnamesSet = handler.getSurnamesSet();
                tabbedPane.removeAll();

                ArrayList<String> stringWorkers = new ArrayList<>();
                for(Worker w : workerList)
                    stringWorkers.add(w.toString());
                tabbedPane.addTab("Opened", new MyPanel(stringWorkers));

            } catch (ParserConfigurationException err) {
                JOptionPane.showMessageDialog(null, "XML parser error.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException err) {
                JOptionPane.showMessageDialog(null, "File cannot be opened.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (org.xml.sax.SAXException err) {
                JOptionPane.showMessageDialog(null, "SAX Exception.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void byPositionItemAction() {
        ArrayList<Worker> workersClone = (ArrayList<Worker>) workerList.clone();
        Collections.sort(workersClone, Worker.COMPARATOR);
        ArrayList<String> stringWorkers = new ArrayList<>();
        for (Worker c : workersClone)
            stringWorkers.add(c.toString());
        tabbedPane.addTab("By position", new MyPanel(stringWorkers));
    }
    private void bySalaryItemAction() {
        ArrayList<Worker> workersClone = (ArrayList<Worker>) workerList.clone();
        Collections.sort(workersClone);
        ArrayList<String> stringWorkers = new ArrayList<>();
        for (Worker c : workersClone)
            stringWorkers.add(c.toString());
        tabbedPane.addTab("By salary", new MyPanel(stringWorkers));
    }
    private void sameSurnamesItemAction() {
        ArrayList<String> stringSurnames = new ArrayList<>();
        for (String s : sameSurnamesSet)
            stringSurnames.add(s);
        tabbedPane.addTab("Same surnames", new MyPanel(stringSurnames));
    }
    private void positionsItemAction() {
        Set<String> positionSet = new TreeSet<String>();
        for(Worker w : workerList)
            positionSet.add(w.getPosition());
        ArrayList<String> stringPositions = new ArrayList<>();
        for(String p : positionSet)
            stringPositions.add(p);
        tabbedPane.addTab("Positions", new MyPanel(stringPositions));
    }
    private void searchItemAction() {
        Worker toFind = null;
        int minQualification = Collections.min(workerList, (a, b) -> ((Integer)(a.qualification())).compareTo(b.qualification())).qualification();
        //дальше тоже лучше стандартный поиск (binarySearch, может ещё что-то можно)
        for(Worker w : workerList)
            if(w.qualification() == minQualification * 3) {
                toFind = w;
            }
        if(toFind != null)
            JOptionPane.showMessageDialog(this, "Worker with qualification 3 times greater than min:  " + toFind.toString(), "Search", JOptionPane.INFORMATION_MESSAGE/* .ERROR_MESSAGE*/);
        else
            JOptionPane.showMessageDialog(this, "Worker with qualification 3 times greater than min:  " + "There is no such worker.", "Search", JOptionPane.INFORMATION_MESSAGE/* .ERROR_MESSAGE*/);
    }
    private void numberItemAction() {
        new MyDialogNumber(this, posisionMap);
    }



    private class MyHandler extends DefaultHandler {

        private boolean boolWorker = false;
        private boolean boolSurname = false;
        private boolean boolSalary = false;
        private boolean boolPosition = false;
        private int salary;
        private String surname;
        private String position;


        private ArrayList<Worker> workerArrayList;
        private Map<String, Integer> positionIntegerMap;
        private Set<String> surnamesSet;

        public ArrayList<Worker> getWorkerArrayList() {
            return workerArrayList;
        }
        public Map<String, Integer> getPositionIntegerMap() {
            return positionIntegerMap;
        }
        public Set<String> getSurnamesSet() {
            return surnamesSet;
        }

        @Override
        public void startDocument() {
            workerArrayList = new ArrayList<>();
            positionIntegerMap = new HashMap<>();
            surnamesSet = new HashSet<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equalsIgnoreCase("worker")) {
                boolWorker = true;
            } else if (qName.equalsIgnoreCase("salary"))
                boolSalary = true;
            else if (qName.equalsIgnoreCase("position"))
                boolPosition = true;
            else if (qName.equalsIgnoreCase("surname"))
                boolSurname = true;
        }

        @Override
        public void characters(char ch[], int start, int length) {
            if (boolWorker) {
                if (boolSurname) {
                    surname = new String(ch, start, length);
                    boolSurname = false;
                } else if (boolSalary) {
                    salary = Integer.parseInt(new String(ch, start, length));
                    boolSalary = false;
                }
                else if(boolPosition) {
                    position = new String(ch, start, length);
                    boolPosition = false;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            try {
                if (qName.equalsIgnoreCase("worker")) {
                    if(!boolWorker)
                        throw new MyException("XML file is incorrect.");
                    Integer frequency = positionIntegerMap.get(position);
                    positionIntegerMap.put(position, frequency == null ? 1 : frequency + 1);
                    for(Worker w : workerArrayList)
                        if(surname.equals(w.getSurname()))
                            surnamesSet.add(surname);
                    workerArrayList.add(new Worker(surname, position, salary));
                    boolWorker = false;
                }
            } catch(MyException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        new MyFrame("Vasilevskaya");
    }
}
