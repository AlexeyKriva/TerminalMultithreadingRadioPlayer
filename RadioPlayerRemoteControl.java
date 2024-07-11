import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RadioPlayerRemoteControl {
    private RadioPlayer radioPlayer;
    private static final String DEFAULT_SONG_NAME_WITH_ = "SONG_";
    private static final String DEFAULT_AUTHOR_NAME_WITH_ = "AUTHOR_";
    private static final int MIN_VOLUME = 0;
    private static final int DEFAULT_VOLUME = 9;
    private static final int MAX_VOLUME = 15;
    private static final int MIN_DURATION = 5;
    private static final int MAX_DURATION = 10;
    private static final int NUMBERS_OF_SONGS = 15;
    private static final List<String> SONG_TEXTS = List.of("nananannanananana", "tutututuutututututututu", "eeeeeeeeeeeeeeeeee");

    public RadioPlayerRemoteControl(RadioPlayer radioPlayer) {
        this.radioPlayer = radioPlayer;
    }

    public RadioPlayerRemoteControl() {}

    public Map<Wave, Queue<Song>> initRadioPlayer() {
        Random random = new Random();
        return IntStream.rangeClosed(1, NUMBERS_OF_SONGS).
                mapToObj(i -> {
                    Wave wave = Wave.values()[random.nextInt(Wave.values().length)];
                    Song song = new Song(DEFAULT_SONG_NAME_WITH_ + i, DEFAULT_AUTHOR_NAME_WITH_ + i, DEFAULT_VOLUME,
                        random.nextInt(MIN_DURATION, MAX_DURATION), SONG_TEXTS.get(random.nextInt(SONG_TEXTS.size())), this.radioPlayer);
                    return new AbstractMap.SimpleEntry<>(wave, song);
                }).
                collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        HashMap::new,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toCollection(LinkedList::new))
                ));
    }
}
