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

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestNoKeywordPresent {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testNoKeywordPresent() {
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
                allOf(withId(R.id.urgentKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        editText.perform(replaceText("NoKeywordFound"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.saveSubject), withText("Save Subject"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.urgentKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        editText2.perform(replaceText("NoKeywordFound"), closeSoftKeyboard());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.saveBody), withText("Save Body"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                2),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.savedKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                4),
                        isDisplayed()));
        editText3.perform(replaceText("NoKeywordFound"), closeSoftKeyboard());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.saveSubject), withText("Save Subject"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0),
                        isDisplayed()));
        button4.perform(click());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.savedKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                4),
                        isDisplayed()));
        editText4.perform(replaceText("NoKeywordFound"), closeSoftKeyboard());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.saveBody), withText("Save Body"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                2),
                        isDisplayed()));
        button5.perform(click());


        ViewInteraction editText5 = onView(
                allOf(withId(R.id.spamKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                6),
                        isDisplayed()));
        editText5.perform(replaceText("NoKeywordFound"), closeSoftKeyboard());

        ViewInteraction button8 = onView(
                allOf(withId(R.id.saveSubject), withText("Save Subject"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0),
                        isDisplayed()));
        button8.perform(click());

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.spamKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                6),
                        isDisplayed()));
        editText6.perform(replaceText("NoKeywordFound"), closeSoftKeyboard());


        ViewInteraction editText9 = onView(
                allOf(withId(R.id.urgentKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        editText9.perform(replaceText("NoUser@usc.edu"), closeSoftKeyboard());


        ViewInteraction button10 = onView(
                allOf(withId(R.id.saveSender), withText("Save Sender"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                1),
                        isDisplayed()));
        button10.perform(click());

        ViewInteraction editText10 = onView(
                allOf(withId(R.id.savedKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                4),
                        isDisplayed()));
        editText10.perform(replaceText("NoUser@usc.edu"), closeSoftKeyboard());


        ViewInteraction button12 = onView(
                allOf(withId(R.id.saveSender), withText("Save Sender"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                1),
                        isDisplayed()));
        button12.perform(click());

        ViewInteraction editText12 = onView(
                allOf(withId(R.id.spamKeywords),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                6),
                        isDisplayed()));
        editText12.perform(replaceText("NoUser@usc.edu"), closeSoftKeyboard());

        ViewInteraction button13 = onView(
                allOf(withId(R.id.saveSender), withText("Save Sender"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                1),
                        isDisplayed()));
        button13.perform(click());

        pressBack();

        mActivityTestRule.getActivity().sendEmail("Empty", "Empty without anything to flag this email");

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {

        }

        ViewInteraction button15 = onView(
                allOf(withId(R.id.notificationsButton), withText("Notifications"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        button15.perform(click());

        ViewInteraction button21 = onView(
                allOf(withId(R.id.savedButton), withText("Saved"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        button21.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("NoKeywordFound"),
                        childAtPosition(
                                allOf(withId(R.id.listView),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(doesNotExist());

        pressBack();

        ViewInteraction button22 = onView(
                allOf(withId(R.id.urgentButton), withText("Urgent"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        button22.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("NoKeywordFound"),
                        childAtPosition(
                                allOf(withId(R.id.listView),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(doesNotExist());

        pressBack();

        ViewInteraction button23 = onView(
                allOf(withId(R.id.spamButton), withText("Spam"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        button23.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.text1), withText("NoKeywordFound"),
                        childAtPosition(
                                allOf(withId(R.id.listView),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView3.check(doesNotExist());

       // pressBack();

        pressBack();

        pressBack();

        ViewInteraction button24 = onView(
                allOf(withId(R.id.account_button), withText("Account"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        button24.perform(click());

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
