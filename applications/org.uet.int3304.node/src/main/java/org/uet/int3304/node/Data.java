package org.uet.int3304.node;

import java.io.Serializable;

public class Data implements Serializable {
    private String type;
    private float data;

    public Data(String type,float data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Data{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
