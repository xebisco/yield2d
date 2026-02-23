package com.xebisco.yield2d.engine.system;

import com.xebisco.yield2d.engine.Pair;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class AppData {
    private File wDir;
    private final static Random RAND = new Random();

    private final String appName, appVersion;
    private final long revision;
    private HashMap<Pair<String, Long>, DataPackage> packageMap;

    private boolean allowSearchingOldRevs = true;

    public AppData(String appName, String appVersion, long revision) {
        if (revision < 0) throw new IllegalStateException("REVISION LESS THAN ZERO");
        this.appName = appName;
        this.appVersion = appVersion;
        this.revision = revision;
        loadDefaultDir();
        loadPackageMap();
    }

    private void loadDefaultDir() {
        AppDirs appDirs = AppDirsFactory.getInstance();
        setwDir(new File(appDirs.getUserDataDir("yeappdata_" + getAppName(), getAppName() + "-" + getAppVersion(), getAppName())));
        getwDir().mkdirs();
    }

    private DataPackage getPkgDat(String pkg, boolean searchOldRevs) {
        long rev = getRevision();
        if (rev < 0) throw new IllegalStateException("REVISION LESS THAN ZERO");
        DataPackage dataPackage;
        do {
            dataPackage = packageMap.get(new Pair<>(pkg, rev--));
        } while (dataPackage == null && searchOldRevs && rev >= 0);
        if (dataPackage == null) throw new IllegalStateException("Could not find package");
        return dataPackage;
    }

    public Object getPackage(String pkg) {
        return getPackage(pkg, Serializable.class);
    }

    public <T extends Serializable> T getPackage(String pkg, Class<T> clazz) {
        loadPackageMap();
        return clazz.cast(packageFromDisk(getPkgDat(pkg, isAllowSearchingOldRevs())));
    }

    public boolean containsPackage(String pkg) {
        try {
            getPkgDat(pkg, isAllowSearchingOldRevs());
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public void addPackage(String pkg, Serializable obj) {
        addPackage(pkg, obj, obj.getClass());
    }

    public void addIfNullPackage(String pkg, Serializable obj) {
        if (!containsPackage(pkg))
            addPackage(pkg, obj, obj.getClass());
    }

    public void addPackage(String pkg, Serializable obj, Class<?> clazz) {
        removeSameRevisionPackage(pkg);
        DataPackage dataPackage = new DataPackage(ThreadLocalRandom.current().nextInt(1000, Integer.MAX_VALUE), revision, clazz);
        packageMap.put(new Pair<>(pkg, getRevision()), dataPackage);
        packageToDisk(dataPackage, obj);
        savePackageMap();
    }

    private void packageToDisk(DataPackage dataPackage, Serializable obj) {
        File file = new File(getwDir(), "n" + dataPackage.getId() + ".yield_package");

        saveGZipObject(obj, file);
    }

    private void saveGZipObject(Serializable obj, File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             BufferedOutputStream bos = new BufferedOutputStream(gzos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeSameRevisionPackage(String pkg) {
        if (packageMap.containsKey(new Pair<>(pkg, getRevision()))) removePackage(pkg);
    }

    private void removePackage(String pkg) {
        File file = new File(getwDir(), "n" + packageMap.get(new Pair<>(pkg, getRevision())).getId() + ".yield_package");
        file.delete();
    }


    private Object packageFromDisk(DataPackage dataPackage) {
        File file = new File(getwDir(), "n" + dataPackage.getId() + ".yield_package");
        try (FileInputStream fis = new FileInputStream(file);
             GZIPInputStream gzis = new GZIPInputStream(fis);
             BufferedInputStream bis = new BufferedInputStream(gzis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private void loadPackageMap() {
        File file = new File(getwDir(), "pkgmap");
        if (!file.exists()) {
            setPackageMap(new HashMap<>());
            savePackageMap();
        } else {
            try (FileInputStream fis = new FileInputStream(file);
                 GZIPInputStream gzis = new GZIPInputStream(fis);
                 BufferedInputStream bis = new BufferedInputStream(gzis);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                //noinspection unchecked
                setPackageMap((HashMap<Pair<String, Long>, DataPackage>) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void savePackageMap() {
        File file = new File(getwDir(), "pkgmap");

        saveGZipObject(packageMap, file);
    }

    public DataPackage searchPackage() {
        return null;
    }

    public File getwDir() {
        return wDir;
    }

    public AppData setwDir(File wDir) {
        this.wDir = wDir;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public long getRevision() {
        return revision;
    }

    public boolean isAllowSearchingOldRevs() {
        return allowSearchingOldRevs;
    }

    public AppData setAllowSearchingOldRevs(boolean allowSearchingOldRevs) {
        this.allowSearchingOldRevs = allowSearchingOldRevs;
        return this;
    }

    public HashMap<Pair<String, Long>, DataPackage> getPackageMap() {
        return packageMap;
    }

    public AppData setPackageMap(HashMap<Pair<String, Long>, DataPackage> packageMap) {
        this.packageMap = packageMap;
        return this;
    }
}
