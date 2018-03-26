package test.java;

import main.java.InputOutputHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;

public class InputOutputHelperTest extends Assert {
    private InputOutputHelper inputOutputHelper;

    @Before
    public void init() {
        inputOutputHelper = new InputOutputHelper("utf-8", "utf-8");
    }

    @Test (expected = Exception.class)
    public void testInvalidOpenFile() {
        inputOutputHelper.openFile("ifjwhugehguhuheughu8huhhhuhuvhuhwhwu8y23t478278c2t8t.txt");
    }

    @Test (expected = Exception.class)
    public void testInvalidCloseFile() {
        InputOutputHelper.closeFile(null);
    }

    @Test
    public void testOpenCloseFile() {
        BufferedReader reader = inputOutputHelper.openFile("./data/separators.txt");
        try {
            reader.readLine();
        } catch (Exception e) {
            assert false;
        }
        InputOutputHelper.closeFile(reader);
    }

    @Test
    public void testSplitString() {
        String[] substr = InputOutputHelper.splitString("123 12 94 1q", '2');
        assertEquals(3, substr.length);
        assertEquals("1", substr[0]);
        assertEquals("3 1", substr[1]);
        assertEquals(" 94 1q", substr[2]);
    }

    @Test
    public void testGetInstance() {
        assert InputOutputHelper.getInstance() == inputOutputHelper;
    }
}
