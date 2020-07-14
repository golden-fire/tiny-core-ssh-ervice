package io.goldenfire.core.ssh;

import io.goldenfire.core.ssh.model.Scp;
import io.goldenfire.core.ssh.prop.ServerProperties;

import java.util.Optional;

public class ScpService extends RemoteService{
    private final ServerProperties serverProperties;
    private final Scp scp;

    public ScpService(ServerProperties serverProperties, Scp scp) {
        this.serverProperties = serverProperties;
        this.scp = scp;
    }

    public String executeScpService(boolean displayOnConsole){
        return super.execute().getRemoteResult(displayOnConsole);
    }

    @Override
    protected Optional<ProcessBuilder> createProcessBuilder() {
        return this.scp.getType().equalsIgnoreCase("upload")? createScpUploader()
                : this.scp.getType().equalsIgnoreCase("download")? createScpDownloader()
                : Optional.empty();
    }

    private Optional<ProcessBuilder> createScpDownloader() {
        return Optional.of(new ProcessBuilder("scp", "",
                super.getRemoteAccessString(this.serverProperties)+":"+this.scp.getRemotePath(), this.scp.getLocalPath().toString()));
    }

    private Optional<ProcessBuilder> createScpUploader(){
        return Optional.of(new ProcessBuilder("scp", this.scp.getLocalPath().toString(),
                super.getRemoteAccessString(this.serverProperties)+":"+this.scp.getRemotePath()));
    }
}