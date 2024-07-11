import java.util.concurrent.TimeUnit;

public class Song extends Thread {
    private String songName;
    private String songAuthor;
    private int volume;
    private int durationInSeconds;
    private String text;
    private RadioPlayer fromRadioPlayer;
    private static final int ONE_SONG_ELEMENT_PLAY_TIME_IN_SECONDS = 1;
    private static final String SONG_START = "Song name: %s\tSong author: %s\tCurrent volume: %d\tDuration in seconds: %d\nSong start\n";
    private static final String SONG_FINISH = "\nSong finished";

    public Song(String name, String author, int volume, int durationInSeconds, String text, RadioPlayer fromRadioPlayer) {
        this.songName = name;
        this.songAuthor = author;
        this.volume = volume;
        this.durationInSeconds = durationInSeconds;
        this.text = text;
        this.fromRadioPlayer = fromRadioPlayer;
    }


    public String getSongName() {
        return songName;
    }

    public int getVolume() {
        return volume;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public String getText() {
        return text;
    }

    public RadioPlayer getFromRadioPlayer() {
        return fromRadioPlayer;
    }

    public void setSongName(String name) {
        this.songName = name;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFromRadioPlayer(RadioPlayer fromRadioPlayer) {
        this.fromRadioPlayer = fromRadioPlayer;
    }

    @Override
    public void run() {
        playSong();
    }

    private void playSong() {
        synchronized (RadioPlayer.class) {
            this.checkActive();
            this.printCurrentSingInformation();
            while (this.durationInSeconds > 0) {
                printPartOfSongsText();
                reduceDurationInSeconds();
            }
            this.printFinishSongInformation();
            RadioPlayer.class.notifyAll();
        }
    }

    private void printCurrentSingInformation() {
        System.out.printf(SONG_START, this.songName, this.songAuthor, this.volume, this.durationInSeconds);
    }

    private void printPartOfSongsText() {
        System.out.print(this.text);
    }

    private void printFinishSongInformation() {
        System.out.println(SONG_FINISH);
    }

    private int checkActive() {
        if (!fromRadioPlayer.isActive()) {
             return this.volume = 0;
        }

        return this.volume;
    }

    private void reduceDurationInSeconds() {
        try {
            this.durationInSeconds--;
            TimeUnit.SECONDS.sleep(ONE_SONG_ELEMENT_PLAY_TIME_IN_SECONDS);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}