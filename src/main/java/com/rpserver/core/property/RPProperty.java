package com.rpserver.core.property;

import org.bukkit.Location;

import java.util.UUID;

public class RPProperty {

    public enum Tip {
        EV, ISYERI, RESTORAN, MARKET
    }

    private final String ad;
    private final Tip tip;
    private Location konum;
    private double fiyat;
    private double kiraFiyati;
    private UUID sahip;   // null = satilik/bos
    private boolean kirada;

    public RPProperty(String ad, Tip tip, Location konum, double fiyat, double kiraFiyati) {
        this.ad = ad;
        this.tip = tip;
        this.konum = konum;
        this.fiyat = fiyat;
        this.kiraFiyati = kiraFiyati;
    }

    public String getAd() {
        return ad;
    }

    public Tip getTip() {
        return tip;
    }

    public Location getKonum() {
        return konum;
    }

    public void setKonum(Location konum) {
        this.konum = konum;
    }

    public double getFiyat() {
        return fiyat;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }

    public double getKiraFiyati() {
        return kiraFiyati;
    }

    public void setKiraFiyati(double kiraFiyati) {
        this.kiraFiyati = kiraFiyati;
    }

    public UUID getSahip() {
        return sahip;
    }

    public void setSahip(UUID sahip) {
        this.sahip = sahip;
    }

    public boolean isSahipli() {
        return sahip != null;
    }

    public boolean isKirada() {
        return kirada;
    }

    public void setKirada(boolean kirada) {
        this.kirada = kirada;
    }

    public void bosalt() {
        this.sahip = null;
        this.kirada = false;
    }
}
