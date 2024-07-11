import java.io.File;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;


public class RadioPlayer extends Thread {
    private Map<Wave, Queue<Song>> songs;
    private boolean isActive;
    private Wave playingWave;
    private Wave lastPlayingWave;
    private static final int PAUSE_AFTER_SONG_IN_MILLISECOND = 500;
    private static final String CURRENT_WAVE_TEMPLATE = "Current wave is %s\n";
    private static final String CHANGE_WAVE_MESSAGE_TEMPLATE = "Wave was changed from %s to %s";

    public RadioPlayer() {}

    public RadioPlayer(Map<Wave, Queue<Song>> songs) {
        this.songs = songs;
    }

    public Map<Wave, Queue<Song>>getSongs() {
        return songs;
    }

    public boolean isActive() {
        return isActive;
    }

    public Wave getPlayingWave() {
        return playingWave;
    }

    public Wave getLastPlayingWave() {
        return lastPlayingWave;
    }

    public void setSongs(Map<Wave, Queue<Song>> songs) {
        this.songs = songs;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPlayingWave(Wave playingWave) {
        this.playingWave = playingWave;
    }

    public void setLastPlayingWave(Wave lastPlayingWave) {
        this.lastPlayingWave = lastPlayingWave;
    }

    @Override
    public void run() {
        startPlay();
    }

    private void startPlay() {
        synchronized (RadioPlayer.class) {
            try {
                this.showCurrentWave();
                while (!this.songs.get(playingWave).isEmpty() && !Thread.currentThread().isInterrupted()) {
                    Song song = songs.get(playingWave).poll();
                    song.start();
                    RadioPlayer.class.wait();
                    TimeUnit.MILLISECONDS.sleep(PAUSE_AFTER_SONG_IN_MILLISECOND);
                    this.checkWaveOnChanging();
                }
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void showCurrentWave() {
        System.out.printf(CURRENT_WAVE_TEMPLATE, this.playingWave);
    }

    private void checkWaveOnChanging() {
        if (!this.lastPlayingWave.equals(this.playingWave)) {
            System.out.printf(CHANGE_WAVE_MESSAGE_TEMPLATE, this.lastPlayingWave, this.playingWave);
            this.lastPlayingWave = this.playingWave;
        }
    }
}