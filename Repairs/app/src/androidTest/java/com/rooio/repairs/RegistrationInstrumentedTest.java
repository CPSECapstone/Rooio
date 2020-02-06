
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

        import static androidx.test.espresso.action.ViewActions.typeText;
        import static androidx.test.espresso.assertion.ViewAssertions.matches;
        import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
        import static androidx.test.espresso.matcher.ViewMatchers.withId;
        import static androidx.test.espresso.matcher.ViewMatchers.withText;
        import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistrationInstrumentedTest{
    @Rule
    public IntentsTestRule<Registration> intentRule = new IntentsTestRule<>(Registration.class);

    @Test
    public void testLaunchActivity() {
        onView(withId(R.id.title)).check(matches(withText("Register for a Roopairs account")));
        onView(withId(R.id.cancelRegistration)).check(matches(withText("Cancel")));
        onView(withId(R.id.register)).check(matches(withText("Register")));
    }


    @Test
    public void testCancelButton() {
        onView(withId(R.id.cancelRegistration)).perform(click());
        intended(hasComponent(Landing.class.getName()));
    }

}
