package com.rooio.repairs;


import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Test
    public void testCancelLogin() {
        ActivityScenario.launch(Login.class);
        onView(withId(R.id.cancelLogin)).check(matches(withText("Cancel")));
    }
    /*
    @Test
    public void testEmptyUsername() {
        ActivityScenario.launch(Login.class);
        onView(withId(R.id.usernameField)).perform(typeText(""));
        onView(withId(R.id.passwordField)).perform(typeText("hacker"));
        //onView(withId(R.id.connectAccount)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Incorrect username and/or password.")));
    }

    @Test
    public void testEmptyPassword() {
        ActivityScenario.launch(Login.class);
        //onView(withId(R.id.usernameField)).perform(typeText("hacker"));
        //onView(withId(R.id.passwordField)).perform(typeText(""));
        onView(withId(R.id.connectAccount)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Incorrect username and/or password.")));
    }

     */

}
