import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyPanel extends JPanel {

    public MyPanel(ArrayList<String> stringData) {
        setLayout(new BorderLayout());
        DefaultListModel listModel = new DefaultListModel();
        JList list = new JList(listModel);
        for(String s : stringData)
            listModel.addElement(s);
        JScrollPane listScroller = new JScrollPane(list);
        add(listScroller, BorderLayout.CENTER);
    }
}
