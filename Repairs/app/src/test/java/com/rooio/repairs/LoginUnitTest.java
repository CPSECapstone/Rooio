package com.rooio.repairs;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {

    @Mock
    RequestQueue queue;

    @Mock
    JsonObjectRequest request;

    @Test
    public void sendCorrectLoginInfo() {
        Login l = new Login();
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "hey");
        params.put("password", "hey");
        JsonRequest r = new JsonRequest(true,
                "url",
                params,
                l.responseFunc,
                l.errorFunc,
                false);
        l.sendLoginInfo(queue, r, request);
    }
}