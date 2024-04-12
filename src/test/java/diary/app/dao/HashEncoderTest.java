package diary.app.dao;

import diary.app.auxiliaryfunctions.HashEncoder;
import org.junit.Test;

import java.beans.Encoder;

import static org.junit.Assert.assertEquals;

public class HashEncoderTest {

    @Test
    public void encodeTest() {
        HashEncoder hashEncoder = new HashEncoder();
        assertEquals(hashEncoder.encode("test"), hashEncoder.encode("test"));
    }
}
