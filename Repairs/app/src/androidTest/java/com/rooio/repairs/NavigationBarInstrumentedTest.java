
package com.rooio.repairs;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationBarInstrumentedTest {

    @Rule
    public IntentsTestRule<Dashboard> intentRule = new IntentsTestRule<>(Dashboard.class);

    @Test
    public void testLaunchActivity2() {
        onView(withId(R.id.dashboard_text)).check(matches(withText("Dashboard")));
        onView(withId(R.id.equipment_text)).check(matches(withText("Equipment")));
        onView(withId(R.id.settings_text)).check(matches(withText("Settings")));
        onView(withId(R.id.jobs_text)).check(matches(withText("Jobs")));
        onView(withId(R.id.collapse_text)).check(matches(withText("Collapse")));

    }

    @Test
    public void testEquipmentButton() {
        onView(withId(R.id.equipment)).perform(click());
        intended(hasComponent(Equipment.class.getName()));
    }

    @Test
    public void testSettingsButton() {
        onView(withId(R.id.settings)).perform(click());
        intended(hasComponent(Settings.class.getName()));
    }

    @Test
    public void testJobsButton() {
        onView(withId(R.id.jobs)).perform(click());
        intended(hasComponent(Jobs.class.getName()));
    }

    @Test
    public void testCollapseButton() {
        onView(withId(R.id.collapse)).perform(click());
        onView(withId(R.id.equipment_text)).check(matches(not(isDisplayed())));
        onView(withId(R.id.settings_text)).check(matches(not(isDisplayed())));
        onView(withId(R.id.jobs_text)).check(matches(not(isDisplayed())));
        onView(withId(R.id.dashboard_text)).check(matches(not(isDisplayed())));
        onView(withId(R.id.dashboard)).check(matches(isDisplayed()));
        onView(withId(R.id.equipment)).check(matches(isDisplayed()));
        onView(withId(R.id.jobs)).check(matches(isDisplayed()));
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }

}
