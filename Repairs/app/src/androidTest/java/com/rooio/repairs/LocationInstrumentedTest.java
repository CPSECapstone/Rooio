package com.rooio.repairs;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LocationInstrumentedTest {


    @Rule
    public IntentsTestRule<LocationLogin> intentRule = new IntentsTestRule<>(LocationLogin.class);


    @Test
    public void testLaunchActivity() {
        onView(withId(R.id.equipment)).check(matches(withText("Choose Service Location")));
        onView(withId(R.id.Choose_small)).check(matches(withText("CHOOSE A SERVICE LOCATION")));
        onView(withId(R.id.addLocation)).check(matches(withText("+ Add Another Service Location")));

    }

    /*
    @Test
    public void testCancelButton() {
        onView(withId(R.id.addLocation)).perform(click());
        intended(hasComponent(AddLocationLogin.class.getName()));
    }

     */



}
