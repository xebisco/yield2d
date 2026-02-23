import com.xebisco.yield2d.engine.system.AppData;

public class DataTest {
    public static void main(String[] args) {
        AppData appData = new AppData("testname", "testversion", 0);
        System.out.println(appData.getwDir());
        appData.addPackage("test", "aaaa");
    }
}
