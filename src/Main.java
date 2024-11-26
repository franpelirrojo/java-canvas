import java.awt.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Canvas");

        frame.setSize(1080, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panelLateral = new JPanel();
        Render renderPanel = new Render();
        renderPanel.setPreferredSize(new Dimension((int)(frame.getWidth()*0.8), frame.getHeight()));
        panelLateral.setPreferredSize(new Dimension((int)(frame.getWidth()*0.2), frame.getHeight()));

        frame.add(renderPanel, BorderLayout.CENTER);
        //frame.add(panelLateral, BorderLayout.EAST);

        frame.setVisible(true);

        renderPanel.createBufferStrategy(2);
        renderPanel.start();
    }
}