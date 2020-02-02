package com.rooio.repairs;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class PreferredProvidersLoginInstrumentedTest {
    @Rule
    public IntentsTestRule<PreferredProvidersLogin> intentRule = new IntentsTestRule<>(PreferredProvidersLogin.class);


    @Test
    public void testLaunchActivity() {
        onView(withId(R.id.title)).check(matches(withText("Preferred Service Providers")));
        onView(withId(R.id.addAnother)).check(matches(withText("+ Add Another Service Provider")));
        onView(withId(R.id.Done)).check(matches(withText("Continue")));
    }


    @Test
    public void testContinueButton() {
        onView(withId(R.id.Done)).perform(click());
        intended(hasComponent(Dashboard.class.getName()));
    }

}
