import java.util.EnumSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@SuppressWarnings("all")
public class Main {

    public static void main(String[] args) throws InterruptedException {
        startProgram();
    }

    public static void startProgram() throws InterruptedException {
        User vasya = new User("Vasya");
        User petya = new User("Petya");

        RadioPlayer radioPlayer = new RadioPlayer();
        RadioPlayerRemoteControl radioPlayerRemoteControl = new RadioPlayerRemoteControl(radioPlayer);
        radioPlayer.setSongs(radioPlayerRemoteControl.initRadioPlayer());

        Wave playingWave = vasya.chooseWave();
        radioPlayer.setPlayingWave(playingWave);
        radioPlayer.setLastPlayingWave(playingWave);
        vasya.startPlayer(radioPlayer);

        TimeUnit.SECONDS.sleep(1);
        radioPlayer.setPlayingWave(vasya.chooseWave());
        petya.stopPlayer(radioPlayer);
    }
}