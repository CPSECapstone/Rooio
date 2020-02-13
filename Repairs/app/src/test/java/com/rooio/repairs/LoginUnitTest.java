package com.rooio.repairs;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginUnitTest {

    @Test
    public void sendCorrectLoginInfo() {
        Login l = new Login();
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "hey");
        params.put("password", "hey");
        JsonRequest r = new JsonRequest(
                true,
                "url",
                params,
                l.responseFunc,
                l.errorFunc,
                false);
        l.sendLoginInfo(r);
    }

}