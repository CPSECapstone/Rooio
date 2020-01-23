package com.rooio.repairs;

import androidx.arch.core.util.Function;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.NoCache;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginUnitTest {
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

    @Test
    public void testSendLoginInfo() {
        Login l = new Login();
        HashMap<String, Object> params = new HashMap<>();
        JsonRequest r = new JsonRequest(true,
                "url",
                l.createRequest(params, "user","pass"),
                l.responseFunc,
                l.errorFunc,
                false);
        l.sendLoginInfo(r);
    }
    /*
    @Test
    public void testSendLoginInfo() {
        Login l = new Login();
        HashMap<String, Object> hash = new HashMap<>();
        Function<String,Void> errorFunc = (string) -> {
            return null;
        };
        Function<JSONObject,Void> responseFunc = (string) -> {
            return null;
        };
        JsonRequest r = new JsonRequest(true, "hey", hash, responseFunc, errorFunc, false);
        l.sendLoginInfo("user", "pass", r);
    }
    */



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