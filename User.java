import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class User {
    private String name;
    private static final String STOP_RADIO_PLAYER_TEMPLATE = "________________Radio player is stopped________________";
    private final static Scanner SCANNER = new Scanner(System.in);
    private final static String INPUT_MESSAGE_TEMPLATE = "Please, chouse the wave (FIRST, SECOND or THIRD)";
    private final static String WRONG_INPUT_MESSAGE_TEMPLATE = "You choose wrong wave, you can choose FIRST, SECOND or THIRD\n";
    private final static int SLEEP_BEFORE_CHOOSING_IN_MILLISECONDS = 500;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void startPlayer(RadioPlayer radioPlayer) {
        radioPlayer.setActive(true);
        radioPlayer.start();
    }

    public void stopPlayer(RadioPlayer radioPlayer) {
        System.out.println(STOP_RADIO_PLAYER_TEMPLATE);
        radioPlayer.setActive(false);
    }

    public Wave chooseWave() {
        synchronized (RadioPlayer.class) {
            System.out.println(INPUT_MESSAGE_TEMPLATE);
            while (true) {
                String wave = SCANNER.nextLine();
                this.sleepBeforeChoosing();
                try {
                    Enum.valueOf(Wave.class, wave);
                    RadioPlayer.class.notifyAll();
                    return Wave.valueOf(wave);
                } catch (IllegalArgumentException illegalArgumentException) {
                    System.out.println(WRONG_INPUT_MESSAGE_TEMPLATE);
                }
            }
        }
    }

    private void sleepBeforeChoosing() {
        try {
            TimeUnit.MILLISECONDS.sleep(SLEEP_BEFORE_CHOOSING_IN_MILLISECONDS);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
