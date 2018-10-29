package com.example.cs310.mysmartusc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;

import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TestLoginActivity {
    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

    @Test
    public void changeText_sameActivity() {

        // Type text and then press the button.
//        onView(withId(R.id.editTextUserInput))
//                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
//        onView(withId(R.id.changeTextBt)).perform(click());
//
//        // Check that the text was changed.
//        onView(withId(R.id.textToBeChanged))
//                .check(matches(withText(mStringToBetyped)));
    }
}
