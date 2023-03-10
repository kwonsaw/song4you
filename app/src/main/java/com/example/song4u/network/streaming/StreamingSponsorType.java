package com.example.song4u.network.streaming;

public enum StreamingSponsorType {

    MELON("melon"), GENIE("genie"), BUGS("bugs"), NONE("none");

    final private String name;

    StreamingSponsorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static StreamingSponsorType type(String str) {
        if (str.equals("melon")) {
            return MELON;
        }
        if (str.equals("ginnie")) {
            return GENIE;
        }

        if (str.equals("bugs")) {
            return BUGS;
        }
        return NONE;
    }

}
