package com.rpserver.core.player;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private double bakiye;
    private String meslek;      // null = mesleksiz, aksi halde JobType.name()
    private boolean polisNobette;
    private double susama = 100.0;
    private String evAdi;       // sahip olunan/kiraladigi ev
    private String isyeriAdi;
    private String restoranAdi;
    private String arabaId;
    private String takimAdi;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getBakiye() {
        return bakiye;
    }

    public void setBakiye(double bakiye) {
        this.bakiye = Math.max(0, bakiye);
    }

    public void ekleBakiye(double miktar) {
        this.bakiye += miktar;
    }

    public boolean harca(double miktar) {
        if (bakiye < miktar) return false;
        bakiye -= miktar;
        return true;
    }

    public String getMeslek() {
        return meslek;
    }

    public void setMeslek(String meslek) {
        this.meslek = meslek;
    }

    public boolean isPolisNobette() {
        return polisNobette;
    }

    public void setPolisNobette(boolean polisNobette) {
        this.polisNobette = polisNobette;
    }

    public double getSusama() {
        return susama;
    }

    public void setSusama(double susama) {
        this.susama = Math.max(0, Math.min(100, susama));
    }

    public String getEvAdi() {
        return evAdi;
    }

    public void setEvAdi(String evAdi) {
        this.evAdi = evAdi;
    }

    public String getIsyeriAdi() {
        return isyeriAdi;
    }

    public void setIsyeriAdi(String isyeriAdi) {
        this.isyeriAdi = isyeriAdi;
    }

    public String getRestoranAdi() {
        return restoranAdi;
    }

    public void setRestoranAdi(String restoranAdi) {
        this.restoranAdi = restoranAdi;
    }

    public String getArabaId() {
        return arabaId;
    }

    public void setArabaId(String arabaId) {
        this.arabaId = arabaId;
    }

    public String getTakimAdi() {
        return takimAdi;
    }

    public void setTakimAdi(String takimAdi) {
        this.takimAdi = takimAdi;
    }
}
