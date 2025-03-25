package quina;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Popup extends JFrame{
	public Popup(String text, String titulo, int x, int y, int larg, int alt) {
        super(titulo);
        container = getContentPane();
        textArea = new JTextArea();
        textArea.append(text);
        container.add(new JScrollPane(textArea), BorderLayout.CENTER);
        setSize(larg, alt);
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);     
    }
    private JTextArea textArea;
    private Container container;

}
