package com.example.slametsudarsono.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.slametsudarsono.bakingapp.utils.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.slametsudarsono.bakingapp.utils.Constants.RECIPE_ID;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailsActivityDisplayTest {

    @Rule
    public final ActivityTestRule<RecipeDetailActivity> mActivityTestRule = new ActivityTestRule<>(RecipeDetailActivity.class, true, false);

    @Before
    public void launchDetailsActivity() {
        //First launch the details activity
        Intent intent = new Intent();
        intent.putExtra(RECIPE_ID, RECIPE_ID);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void checkIngredientListIsDisplayed(){
        Context context = InstrumentationRegistry.getTargetContext();
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);

        if(isTablet){
            onView(withId(R.id.list_fragment)).check(matches(isDisplayed()));
        } else{
            onView(withId(R.id.list_fragment_container)).check(matches(isDisplayed()));
        }

    }
}
