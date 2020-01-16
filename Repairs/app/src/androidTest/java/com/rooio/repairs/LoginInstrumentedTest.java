package com.rooio.repairs;

import android.content.Context;
import android.content.Intent;


import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;

import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Rule
    public ActivityTestRule<Login> rule = new ActivityTestRule(Login.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.rooio.repairs", appContext.getPackageName());
    }

    @Test
    public void invalidPassword() {
        onView(withId(R.id.username_field)).perform(typeText("hacker"));
        onView(withId(R.id.login)).perform(click());
        onView(withText("Password must be at least 6 alphanumeric characters")).check(matches(isDisplayed()));
    }

    /*
    @Test
    public void validPassword() {
        Intents.init();
        rule.launchActivity(new Intent());
        onView(withId(R.id.username_field)).perform(typeText("testdummy@gmail.com"));
        onView(withId(R.id.username_field)).perform(typeText("iamadumdum123"));
        onView(withId(R.id.login)).check(matches(withText("Login")));
        onView(withId(R.id.login)).perform(click());
        onView(withText("Password must be at least 6 alphanumeric characters")).check(matches(isDisplayed()));
        intended(hasComponent(Login.class.getName()));
        Intents.release();
    }

     */
    
}
