package io.goldenfire.core.ssh.prop;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServerProperties {
    private final String hostName;
    private final String username;
}
