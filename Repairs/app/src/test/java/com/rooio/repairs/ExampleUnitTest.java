package com.rooio.repairs;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void validateTrue() {
        String username = "yusu2f";
        String password = "t5y6u7";

        Registration r = new Registration();
        assertTrue(r.validate(username, password));
    }

    /*
    @Test
    public void validateFalse() {
        String username = "";
        String password = "t5y6u7";

        Login login = new Login();
        assertFalse(login.validate(username, password));
    }

    @Test
    public void isValid() {
        String username = "yusuf";
        String password = "t5y6u7";

        Login login = new Login();
        assertTrue(login.isValid(username, password));
    }

    @Test
    public void isValid_usernameLength() {
        String username = "";
        String password = "t5y6u7";

        Login login = new Login();
        assertFalse(login.isValid(username, password));
    }

    @Test
    public void isValid_alphanumeric() {
        String username = "yusuf";
        String password = "toopio";

        Login login = new Login();
        assertFalse(login.isValid(username, password));
    }

    @Test
    public void isValid_passwordLength() {
        String username = "yusuf";
        String password = "to1";

        Login login = new Login();
        assertFalse(login.isValid(username, password));
    }
    */
}