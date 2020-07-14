package io.goldenfire.core.ssh;

import io.goldenfire.core.ssh.model.Scp;
import io.goldenfire.core.ssh.prop.ServerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static io.goldenfire.core.ssh.util.LocalFileUtil.createDirectories;
import static io.goldenfire.core.ssh.util.LocalFileUtil.deleteDirectories;
import static java.lang.System.getProperty;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class ScpServiceTest {
    private ServerProperties serverProperties = new ServerProperties(defaultIfEmpty(getProperty("server_ip"),"172.16.238.20"),
            defaultIfEmpty(getProperty("server_user"),"devops"));
    private ScpService scpService;
    private Path testFile ;

    @BeforeEach
    void before() throws IOException {
        testFile = Paths.get(ResourceUtils.getFile("classpath:test/test.json").getCanonicalPath());
        destroyRemoteFile();
    }
    @Test
    void testUploadFile(){
        this.scpService = new ScpService(serverProperties, new Scp("upload", testFile, Paths.get(".")));
        this.scpService.executeScpService(true);

        String result = new SshService(this.serverProperties, Arrays.asList("cd ~", "ls -l test.json")).executeSshService(true);
        assertThat(result).isNotEmpty();
        assertThat(result).contains("devops").contains("test.json");
    }

    @Test
    void testDownload(){
        new ScpService(serverProperties, new Scp("upload", testFile, Paths.get("."))).executeScpService(false);

        Path workspace = Paths.get(".").resolve("workspace");
        destroyLocalFile(workspace);
        Path local = createDirectories(workspace);

        this.scpService = new ScpService(serverProperties, new Scp("download", local, Paths.get(".").resolve("test.json")));
        String result = this.scpService.executeScpService(true);
        assertThat(result).doesNotContain("scp: ambiguous target");

    }
    void destroyLocalFile(Path path){
        if (path.toFile().exists()){
            deleteDirectories(path);
        }
    }
    void destroyRemoteFile(){
        new SshService(this.serverProperties, Arrays.asList("cd ~", "rm -rf test.json")).executeSshService(true);
    }
}
