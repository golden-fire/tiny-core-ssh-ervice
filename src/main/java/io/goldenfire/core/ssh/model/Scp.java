package io.goldenfire.core.ssh.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
@Getter
public class Scp {
    private final String type;
    private final Path localPath;
    private final Path remotePath;

    public Scp(String type, Path localPath, Path remotePath) {
        this.type = type;
        this.localPath = localPath;
        this.remotePath = remotePath;
    }
}
