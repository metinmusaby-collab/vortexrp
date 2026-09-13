package com.rpserver.core;

import com.rpserver.core.car.ArabaCommand;
import com.rpserver.core.car.CarManager;
import com.rpserver.core.economy.BankaCommand;
import com.rpserver.core.football.LigCommand;
import com.rpserver.core.football.LeagueManager;
import com.rpserver.core.football.TakimCommand;
import com.rpserver.core.gui.GUIListener;
import com.rpserver.core.job.IsCommand;
import com.rpserver.core.job.JobListener;
import com.rpserver.core.job.JobManager;
import com.rpserver.core.job.PolisCommand;
import com.rpserver.core.phone.PhoneCommand;
import com.rpserver.core.phone.SocialManager;
import com.rpserver.core.phone.SosyalCommand;
import com.rpserver.core.player.PlayerDataManager;
import com.rpserver.core.property.EvCommand;
import com.rpserver.core.property.IsyeriCommand;
import com.rpserver.core.property.MarketCommand;
import com.rpserver.core.property.PropertyManager;
import com.rpserver.core.property.RPCommand;
import com.rpserver.core.property.RestoranCommand;
import com.rpserver.core.thirst.SuCommand;
import com.rpserver.core.thirst.ThirstManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPCore extends JavaPlugin {

    private static RPCore instance;

    private PlayerDataManager playerDataManager;
    private PropertyManager propertyManager;
    private JobManager jobManager;
    private CarManager carManager;
    private LeagueManager leagueManager;
    private SocialManager socialManager;
    private ThirstManager thirstManager;

    public static RPCore get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getDataFolder().mkdirs();

        this.playerDataManager = new PlayerDataManager(this);
        this.propertyManager = new PropertyManager(this);
        this.jobManager = new JobManager(this);
        this.carManager = new CarManager(this);
        this.leagueManager = new LeagueManager(this);
        this.socialManager = new SocialManager(this);
        this.thirstManager = new ThirstManager(this);

        playerDataManager.load();
        propertyManager.load();
        jobManager.load();
        carManager.load();
        leagueManager.load();
        socialManager.load();

        // Komutlar
        getCommand("rp").setExecutor(new RPCommand(this));
        getCommand("ev").setExecutor(new EvCommand(this));
        getCommand("isyeri").setExecutor(new IsyeriCommand(this));
        getCommand("market").setExecutor(new MarketCommand(this));
        getCommand("restoran").setExecutor(new RestoranCommand(this));
        getCommand("is").setExecutor(new IsCommand(this));
        getCommand("polis").setExecutor(new PolisCommand(this));
        getCommand("banka").setExecutor(new BankaCommand(this));
        getCommand("araba").setExecutor(new ArabaCommand(this));
        getCommand("takim").setExecutor(new TakimCommand(this));
        getCommand("lig").setExecutor(new LigCommand(this));
        getCommand("telefon").setExecutor(new PhoneCommand(this));
        getCommand("sosyal").setExecutor(new SosyalCommand(this));
        getCommand("su").setExecutor(new SuCommand(this));

        // Listenerlar
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new JobListener(this), this);
        getServer().getPluginManager().registerEvents(playerDataManager, this);

        thirstManager.start();
        jobManager.startPoliceSalaryTask();
        leagueManager.startMatchSimulationTask();

        getLogger().info("RPCore etkinlestirildi.");
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) playerDataManager.saveAll();
        if (propertyManager != null) propertyManager.saveAll();
        if (jobManager != null) jobManager.saveAll();
        if (carManager != null) carManager.saveAll();
        if (leagueManager != null) leagueManager.saveAll();
        if (socialManager != null) socialManager.saveAll();
        getLogger().info("RPCore devre disi birakildi.");
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public CarManager getCarManager() {
        return carManager;
    }

    public LeagueManager getLeagueManager() {
        return leagueManager;
    }

    public SocialManager getSocialManager() {
        return socialManager;
    }

    public ThirstManager getThirstManager() {
        return thirstManager;
    }
}
