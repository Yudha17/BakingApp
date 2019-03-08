package com.example.slametsudarsono.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;


import com.example.slametsudarsono.bakingapp.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityIntentTest {

    private static final int TEST_POSITION = 2;
    private static final int EXPECTED_ID = 3;
    private IdlingResource mIdlingResource;

    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class, true, true);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void intendHasCorrectRecipeId(){
        //Click on a specific recipe
        onView(withId(R.id.recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(TEST_POSITION, click()));

        //Check whether intent has the correct id as extras
        intended(hasExtras(hasEntry(equalTo(Constants.RECIPE_ID), equalTo(EXPECTED_ID))));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
