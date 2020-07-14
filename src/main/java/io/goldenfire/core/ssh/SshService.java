package io.goldenfire.core.ssh;

import io.goldenfire.core.ssh.prop.ServerProperties;
import io.goldenfire.core.ssh.util.LocalStringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SshService extends RemoteService {

    private final ServerProperties serverProperties;
    private final List<String> commands;

    public SshService(ServerProperties serverProperties, List<String> commands) {
        this.serverProperties = serverProperties;
        this.commands = commands;
    }

    @Override
    protected Optional<ProcessBuilder> createProcessBuilder() {
        final String commandsToString = LocalStringUtil.joinToOneString(commands);
        log.info("user: {} trys to run a list of commands {} on th server {}", this.serverProperties.getUsername(),
                commandsToString, this.serverProperties.getHostName());

        return Optional.of(new ProcessBuilder("ssh", super.getRemoteAccessString(serverProperties),
                commandsToString));
    }

    public String executeSshService(boolean displayOnConsole){
        return super.execute().getRemoteResult(displayOnConsole);
    }

}
