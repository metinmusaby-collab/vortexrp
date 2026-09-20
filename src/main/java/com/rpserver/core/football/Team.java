package com.rpserver.core.football;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    private final String ad;
    private UUID kaptan;
    private final List<UUID> uyeler = new ArrayList<>();
    private double kasa;
    private int oynanan, galibiyet, beraberlik, maglubiyet, atilanGol, yenenGol;

    public Team(String ad, UUID kaptan) {
        this.ad = ad;
        this.kaptan = kaptan;
        this.uyeler.add(kaptan);
    }

    public String getAd() {
        return ad;
    }

    public UUID getKaptan() {
        return kaptan;
    }

    public void setKaptan(UUID kaptan) {
        this.kaptan = kaptan;
    }

    public List<UUID> getUyeler() {
        return uyeler;
    }

    public double getKasa() {
        return kasa;
    }

    public void setKasa(double kasa) {
        this.kasa = kasa;
    }

    public void ekleKasa(double miktar) {
        this.kasa += miktar;
    }

    public int getPuan() {
        return galibiyet * 3 + beraberlik;
    }

    public int getOynanan() {
        return oynanan;
    }

    public int getGalibiyet() {
        return galibiyet;
    }

    public int getBeraberlik() {
        return beraberlik;
    }

    public int getMaglubiyet() {
        return maglubiyet;
    }

    public int getAtilanGol() {
        return atilanGol;
    }

    public int getYenenGol() {
        return yenenGol;
    }

    public void maçSonucu(int attigi, int yedigi) {
        oynanan++;
        atilanGol += attigi;
        yenenGol += yedigi;
        if (attigi > yedigi) galibiyet++;
        else if (attigi == yedigi) beraberlik++;
        else maglubiyet++;
    }

    public void setStats(int oynanan, int galibiyet, int beraberlik, int maglubiyet, int atilanGol, int yenenGol) {
        this.oynanan = oynanan;
        this.galibiyet = galibiyet;
        this.beraberlik = beraberlik;
        this.maglubiyet = maglubiyet;
        this.atilanGol = atilanGol;
        this.yenenGol = yenenGol;
    }
}
