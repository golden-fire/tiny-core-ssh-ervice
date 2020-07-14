package io.goldenfire.core.ssh;

import io.goldenfire.core.ssh.prop.ServerProperties;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.getProperty;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class SshServiceTest {

    private ServerProperties serverProperties = new ServerProperties(defaultIfEmpty(getProperty("server_ip"),"172.16.238.20"),
            defaultIfEmpty(getProperty("server_user"),"devops"));
    private List<String> commands = new ArrayList<>();

    private SshService sshService;
    @BeforeEach
    void before(){
        sshService = new SshService(serverProperties, commands);
        createDummyFiles();
    }

    @AfterEach()
    void after(){
        destroyDummy();
    }
    @Test
    void testMultipleCommands(){
       this.commands.addAll(Arrays.asList("cd dummy", "ls -l", "cat test.txt"));
       String result = sshService.executeSshService(true);
       assertThat(result).isNotEmpty();
       assertThat(result).contains("Hello, world.");
    }

    private void createDummyFiles(){
        this.commands.addAll(Arrays.asList("rm -rf dummy","mkdir dummy", "cd dummy", "echo 'Hello, world.' >test.txt", "cd ~"));
        this.sshService.executeSshService(false);
    }
    private void destroyDummy(){
        this.commands.addAll(Arrays.asList("cd ~","rm -rf dummy"));
        this.sshService.executeSshService(false);
    }
}
