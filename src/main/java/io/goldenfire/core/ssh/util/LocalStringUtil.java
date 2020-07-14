package io.goldenfire.core.ssh.util;

import java.util.List;

public final class LocalStringUtil {
    private LocalStringUtil(){
        throw new AssertionError("constructor of an utility class");
    }

    public static String joinToOneString(List<String> commands) {
        if (commands.isEmpty()){
            throw new AssertionError("there should be at least one command");
        }
        return String.join(";", commands);
    }
}
