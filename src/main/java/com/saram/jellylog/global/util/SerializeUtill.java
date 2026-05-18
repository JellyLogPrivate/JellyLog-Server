package com.saram.jellylog.global.util;

import java.io.*;

public class SerializeUtill {

    public static byte[] serialize(Object object) {
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {

            oos.writeObject(object);

            return bos.toByteArray();

        } catch (IOException e) {

            throw new RuntimeException("Cookie serialize error", e);
        }
    }

    public static Object deserialize(byte[] bytes) {

        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)
        ) {

            return ois.readObject();

        } catch (IOException | ClassNotFoundException e) {

            throw new RuntimeException("Cookie deserialize error", e);
        }
    }
}
