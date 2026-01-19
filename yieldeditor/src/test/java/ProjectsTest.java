import com.xebisco.yield2d.editor.projects.ProjectManager;
import com.xebisco.yield2d.editor.Utils;

public class ProjectsTest {
    public static void main(String[] args) {
        Utils.setupAndRun(() -> {
            ProjectManager pm = new ProjectManager();
        });
    }
}
