import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class MyDialogNumber extends JDialog {

    public MyDialogNumber(Frame owner, Map<String, Integer> positionIntegerMap) {
        super(owner, "Number",true);


        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 2));

        container.add(new JLabel("Input position:"));

        JTextField positionField = new JTextField();
        positionField.setText("teacher");
        container.add(positionField, BorderLayout.EAST);

        container.add(new JLabel());

        JButton ok = new JButton("Ok");
        JPanel panel = new JPanel();
        panel.add(ok);
        container.add(panel);

        container.add(new JLabel("Number of workers:"));

        JLabel number = new JLabel("");
        container.add(number);
        pack();


        ok.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Integer n = positionIntegerMap.get(positionField.getText());
                if(n != null)
                    number.setText(Integer.toString(n));
                else
                    number.setText("0");
            }
        });

        setLocation(200, 100);
        setVisible(true);
    }
}
