package com.example.deploylab.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "deploy-lab")
public class DeployLabProperties {

    private String instanceId = "local";
    private String appVersion = "1.0.0";
    private Diagnostics diagnostics = new Diagnostics();
    private Storage storage = new Storage();

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Diagnostics getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(Diagnostics diagnostics) {
        this.diagnostics = diagnostics;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public static class Diagnostics {
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Storage {
        private int maxMessages = 200;
        private int maxRequestRecords = 100;

        public int getMaxMessages() {
            return maxMessages;
        }

        public void setMaxMessages(int maxMessages) {
            this.maxMessages = maxMessages;
        }

        public int getMaxRequestRecords() {
            return maxRequestRecords;
        }

        public void setMaxRequestRecords(int maxRequestRecords) {
            this.maxRequestRecords = maxRequestRecords;
        }
    }
}
