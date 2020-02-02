package com.rooio.repairs;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AddPreferredProvidersLoginInstrumentedTest {
    @Rule
    public IntentsTestRule<AddPreferredProvidersLogin> intentRule = new IntentsTestRule<>(AddPreferredProvidersLogin.class);


    @Test
    public void testLaunchActivity() {
        onView(withId(R.id.title)).check(matches(withText("Enter the phone number of one of your preferred service providers to start requesting service from them")));
        onView(withId(R.id.textViewPhoneNumber)).check(matches(withText("PHONE NUMBER")));
        onView(withId(R.id.add_provider)).check(matches(withText("Add Provider")));
        onView(withId(R.id.cancel)).check(matches(withText("Cancel")));

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
        onView(withId(R.id.cancel)).perform(click());
        intended(hasComponent(PreferredProvidersLogin.class.getName()));
    }
}
