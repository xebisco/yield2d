import com.xebisco.yield2d.editor.PropertiesPanel;
import com.xebisco.yield2d.editor.Utils;
import com.xebisco.yield2d.engine.AddList;
import com.xebisco.yield2d.engine.Desc;
import com.xebisco.yield2d.engine.StartSection;
import com.xebisco.yield2d.engine.Visible;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PropertiesPanelTest {


    @StartSection("test")
    @Visible
    @Desc("NOME Delee")
    String joasdasdsaFdsarje = "UMA PESSOA";

    @Visible
    @Desc("test")
    boolean comeu, andou, pula, le;

    @Desc("test description")
    @Visible
    float a = 1;

    @Visible
    Test1 aaaaa;

    @Visible
    @AddList(AddListString.class)
    ArrayList<String> test;

    public PropertiesPanelTest() {
        JFrame frame = new JFrame();

        frame.add(new JScrollPane(new PropertiesPanel(this, null, true, true)));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(270, 260));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Utils.setupAndRun(PropertiesPanelTest::new);
    }

    public static class AddListString implements AddList.AddListMethod {
        public AddListString() {

        }

        @Override
        public Object instance() {
            return "null";
        }
    }
}
