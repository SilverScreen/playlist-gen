package com.william.playlistgen.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.validation.SongDataValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author William
 */
@RunWith(value = Parameterized.class)
public class SongDataValidatorTest {

    private Song song;
    private boolean valid;

    public SongDataValidatorTest(final Song song, final boolean valid) {
        this.song = song;
        this.valid = valid;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Song(1, "artist", "album", "title", "year", "filepath", null), true},
                {new Song(1, "", "album", "title", "year", "filepath", null), true},
                {new Song(1, "", "", "", "", "", null), true},
                {new Song(1, null, "album", "title", "year", "filepath", null), false},
                {new Song(1, null, null, null, null, null, null), false}
        });
    }

    @Test
    public void testSongDataValidate() {
        assertThat(SongDataValidator.isValid(song), is(valid));
    }
}
