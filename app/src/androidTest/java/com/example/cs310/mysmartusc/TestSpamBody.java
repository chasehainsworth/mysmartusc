package com.example.cs310.mysmartusc;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestSpamBody {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testSpamBody() {
        mActivityTestRule.getActivity().deleteDatabase("MySmartUSC");
        ViewInteraction ip = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        ip.perform(click());

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {

        }

        mActivityTestRule.getActivity().sendEmail("empty", "spamBody");

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.homepage_button), withText("Homepage"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.settingsButton), withText("Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.spamKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                6),
                        isDisplayed()));
        editText.perform(replaceText("spamBody"), closeSoftKeyboard());

//        pressBack();

        ViewInteraction button2 = onView(
                allOf(withId(R.id.saveBody), withText("Save Body"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                2),
                        isDisplayed()));
        button2.perform(click());

        pressBack();

        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {

        }

        ViewInteraction button3 = onView(
                allOf(withId(R.id.notificationsButton), withText("Notifications"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.spamButton), withText("Spam"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        button4.perform(click());

        DataInteraction textView = onData(anything())
                .inAdapterView(allOf(withId(R.id.listView),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        textView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.emailBody), withText("spamBody"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                3),
                        isDisplayed()));
        textView2.check(matches(withText("spamBody")));

        pressBack();

        pressBack();

        pressBack();

        ViewInteraction button5 = onView(
                allOf(withId(R.id.account_button), withText("Account"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        button5.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.sign_out_button), withText("Sign Out"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
