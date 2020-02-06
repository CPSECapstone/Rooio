package com.rooio.repairs;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class PreferredProvidersLoginInstrumentedTest {
    @Rule
    public IntentsTestRule<PreferredProvidersLogin> intentRule = new IntentsTestRule<>(PreferredProvidersLogin.class);


    @Test
    public void testLaunchActivity() {
        onView(withId(R.id.title)).check(matches(withText("Preferred Service Providers")));
        onView(withId(R.id.addAnother)).check(matches(withText("+ Add Another Service Provider")));
        onView(withId(R.id.Done)).check(matches(withText("Continue")));
    }
}
