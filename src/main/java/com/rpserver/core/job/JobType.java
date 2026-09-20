package com.rpserver.core.job;

public enum JobType {
    CIFTCI("Ciftci"),
    MADENCI("Madenci"),
    BALIKCI("Balikci"),
    POLIS("Polis");

    private final String gosterimAdi;

    JobType(String gosterimAdi) {
        this.gosterimAdi = gosterimAdi;
    }

    public String getGosterimAdi() {
        return gosterimAdi;
    }
}
