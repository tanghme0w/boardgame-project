package refactor.client.components;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BottomPanel extends JPanel {
    BottomPanel(List<JButton> buttons) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(getWidth() - 200, 30));
        for(JButton button: buttons) {
            add(button);
        }
    }
}
