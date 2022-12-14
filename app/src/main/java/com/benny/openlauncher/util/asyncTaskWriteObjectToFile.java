package com.benny.openlauncher.util;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;

public class asyncTaskWriteObjectToFile extends asyncTask {

    private final Object value;
    private final String name;
    private final WeakReference<Context> cref;
    private final writeFileStatus wfs;

    public asyncTaskWriteObjectToFile(Context c, String filename, Object value, writeFileStatus wfs) {
        cref = new WeakReference<>(c.getApplicationContext());
        name = filename;
        this.value = value;
        this.wfs = wfs;
    }

    @Override
    public void doWork() {
        try {
            ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(cref.get().getFilesDir() + "/" + name));
            oo.writeObject(value);
            oo.flush();
            oo.close();
            wfs.writeStatus(true, name);
        } catch (Exception e) {
            wfs.writeStatus(false, name);
            e.printStackTrace();
        }
    }

    public interface writeFileStatus {
        void writeStatus(boolean success, String name);
    }
}
