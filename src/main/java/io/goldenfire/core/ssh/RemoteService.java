package io.goldenfire.core.ssh;

import io.goldenfire.core.ssh.prop.ServerProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.lang.System.getProperty;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

@Slf4j
abstract class RemoteService {

    private static final int WAIT_TIME = Integer.parseInt(defaultIfEmpty(getProperty("wait_time"),"20"));
    private static final TimeUnit TIME_UNIT = TimeUnit.valueOf(defaultIfEmpty(getProperty("time_unit"),TimeUnit.SECONDS.name()).toUpperCase());

    private RemoteLoggingHandler remoteLoggingHandler;

    protected abstract Optional<ProcessBuilder> createProcessBuilder();

    protected RemoteService execute() {
        ProcessBuilder pb = createProcessBuilder()
                .orElseThrow(() -> new UnsupportedOperationException("un-supported process builder"));

        pb.redirectErrorStream(true);
        try {
            final Process process = pb.start();
            setOutput(process.getInputStream());
            process.waitFor(WAIT_TIME, TIME_UNIT);
        } catch (IOException e) {
            log.error("found exception {}", e.getMessage());
        } catch (InterruptedException e) {
            log.error("found exception {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        return this;
    }

    protected String getRemoteResult(final boolean displayOnConsole) {
        final String output = parseOutput();
        if (displayOnConsole) {
            log.info("\n{}", output);
        }
        this.closeLoggingHandler();
        return output;
    }

    protected String getRemoteAccessString(ServerProperties serverProperties) {
        return serverProperties.getUsername() + "@" + serverProperties.getHostName();
    }

    private void setOutput(InputStream inputStream) {
        this.closeLoggingHandler();

        this.remoteLoggingHandler = new RemoteLoggingHandler(inputStream);
        this.remoteLoggingHandler.start();
    }

    private void closeLoggingHandler() {
        if (this.remoteLoggingHandler != null) {
            this.remoteLoggingHandler.close();
        }
    }

    private String parseOutput() {
        return this.remoteLoggingHandler.getOutput().replace("\r", "");
    }

    private static class RemoteLoggingHandler extends Thread {

        private final InputStream inputStream;
        private final StringBuilder output;
        private boolean isFinished;

        private RemoteLoggingHandler(InputStream inputStream) {
            this.inputStream = inputStream;
            this.output = new StringBuilder();
            this.isFinished = false;
        }

        @Override
        public void run() {
            try (Scanner scanner = new Scanner(new InputStreamReader(this.inputStream))) {
                hasAllOutput(scanner);
                this.isFinished = true;
            }
        }

        private int calculateWaitingTime(){
            switch (TIME_UNIT){
                case SECONDS:
                    return WAIT_TIME;
                case MINUTES:
                    return WAIT_TIME / 60;
                default:
                    throw new UnsupportedOperationException("only support seconds and minutes");

            }
        }

        String getOutput() {
            log.info("{} wait for console result", Thread.currentThread().getName());
            int loop =0;
            while (!this.isFinished) {
                try {
                    Thread.sleep(2000);
                    if (loop*2 > calculateWaitingTime()){
                        this.isFinished = true;
                    }
                    loop++;
                } catch (InterruptedException e) {
                    log.error("{}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            return this.output.toString();
        }

        void close() {
            this.output.setLength(0);
        }

        private void hasAllOutput(final Scanner scanner) {
            while (scanner.hasNextLine()) {
                this.output.append(scanner.nextLine())
                        .append("\n");
            }
        }

    }
}
