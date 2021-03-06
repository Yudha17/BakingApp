package com.example.slametsudarsono.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.slametsudarsono.bakingapp.MainActivity;
import com.example.slametsudarsono.bakingapp.R;
import com.example.slametsudarsono.bakingapp.RecipeDetailActivity;
import com.example.slametsudarsono.bakingapp.utils.Constants;

public class BakingWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        //Get the id and name of the last chosen recipe from the preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.WIDGET_PREFERENCES, Context.MODE_PRIVATE);
        int lastChosenRecipeId = sharedPreferences.getInt(Constants.LAST_CHOSEN_RECIPE_ID, 0);
        String recipeName = sharedPreferences.getString(Constants.LAST_CHOSEN_RECIPE_NAME, "");
        views.setTextViewText(R.id.appwidget_recipe_name, recipeName);

        //Set adapter
        Intent intent = new Intent(context, IngredientListWidgetService.class);
        views.setRemoteAdapter(R.id.appwidget_ingredient_list, intent);
        views.setEmptyView(R.id.appwidget_ingredient_list, R.id.empty_tv_widget);

        //When clicked, if there is a chosen recipe, open details activity, otherwise open main activity
        if(lastChosenRecipeId > 0){
            Intent detailsIntent = new Intent(context, RecipeDetailActivity.class);
            detailsIntent.putExtra(Constants.RECIPE_ID, lastChosenRecipeId);
            PendingIntent appPendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(detailsIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_root, appPendingIntent);
        } else {
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.putExtra(Constants.RECIPE_ID, lastChosenRecipeId);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_root, appPendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_ingredient_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /*This is a helper method in order to call for updating widget
    without a need to all the parameters updateAppWidget method uses*/

    public static void triggerUpdate(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_ingredient_list);
        //Now update all widgets
        for(int appWidgetId : appWidgetIds){
            BakingWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) { }

    @Override
    public void onDisabled(Context context) { }

    //When widget is removed from the home screen, delete widget related info from shared preferences
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.WIDGET_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.LAST_CHOSEN_RECIPE_ID);
        editor.remove(Constants.LAST_CHOSEN_RECIPE_NAME);
        editor.apply();
    }
}
