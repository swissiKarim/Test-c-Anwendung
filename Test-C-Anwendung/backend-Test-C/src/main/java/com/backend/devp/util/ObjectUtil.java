package com.backend.devp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public final class ObjectUtil {
    private ObjectUtil() {

    }

    public static byte[] deepCopy(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] objectBytes = null;
        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            objectBytes = bos.toByteArray();
        } catch (IOException e) {
            // ignore close exception
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return objectBytes;
    }
}
