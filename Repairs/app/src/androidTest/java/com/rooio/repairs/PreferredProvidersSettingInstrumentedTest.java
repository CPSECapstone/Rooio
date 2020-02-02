package com.rooio.repairs;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;

public class PreferredProvidersSettingInstrumentedTest {
    @Rule
    public IntentsTestRule<PreferredProvidersSetting> intentRule = new IntentsTestRule<>(PreferredProvidersSetting.class);


    @Test
    public void testLaunchActivity() {
        onView(withId(R.id.title)).check(matches(withText("Preferred Service Providers")));
        onView(withId(R.id.addAnother)).check(matches(withText("+ Add Another Service Provider")));
    }


    @Test
    public void testSpinner() {
        onView(withId(R.id.settings_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Service Location"))).perform(click());

        //intended(hasComponent(.class.getName()));
    }

}
