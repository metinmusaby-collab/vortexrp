package com.rpserver.core.phone;

import java.util.UUID;

public class SocialPost {

    private final UUID yazar;
    private final String yazarAdi;
    private final String mesaj;
    private final long zaman;

    public SocialPost(UUID yazar, String yazarAdi, String mesaj, long zaman) {
        this.yazar = yazar;
        this.yazarAdi = yazarAdi;
        this.mesaj = mesaj;
        this.zaman = zaman;
    }

    public UUID getYazar() {
        return yazar;
    }

    public String getYazarAdi() {
        return yazarAdi;
    }

    public String getMesaj() {
        return mesaj;
    }

    public long getZaman() {
        return zaman;
    }
}
