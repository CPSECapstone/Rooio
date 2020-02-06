package com.rooio.repairs;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddPreferredProvidersSettingInstrumentedTest {
    @Rule
    public IntentsTestRule<AddPreferredProvidersSettings> intentRule = new IntentsTestRule<>(AddPreferredProvidersSettings.class);

    @Test
    public void testLaunchActivity() {
        onView(withId(R.id.textView)).check(matches(withText("Add Service Provider")));
        onView(withId(R.id.title)).check(matches(withText("Enter the phone number of one of your preferred service providers to start requesting service from them")));
        onView(withId(R.id.textViewPhoneNumber)).check(matches(withText("PHONE NUMBER")));
        onView(withId(R.id.add_provider)).check(matches(withText("Add Provider")));
    }

    @Test
    public void testAddButtonNoPhoneNumber() {
        onView(withId(R.id.add_provider)).perform(click());
        onView(withId(R.id.error)).check(matches(withText("Please enter a valid phone number.")));
    }

    @Test
    public void testAddButtonShortPhoneNumber() {
        onView(withId(R.id.new_phone)).perform(typeText("123"));
        onView(withId(R.id.add_provider)).perform(click());
        onView(withId(R.id.error)).check(matches(withText("Please enter a valid phone number.")));
    }

    @Test
    public void testAddButtonLongPhoneNumber() {
        onView(withId(R.id.new_phone)).perform(typeText("123123123123123"));
        onView(withId(R.id.add_provider)).perform(click());
        onView(withId(R.id.error)).check(matches(withText("Please enter a valid phone number.")));
    }

    @Test
    public void testCancelButton() {
        onView(withId(R.id.back_button)).perform(click());
        intended(hasComponent(PreferredProvidersSettings.class.getName()));
    }
}
