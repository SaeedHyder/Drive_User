package com.ingic.driveuser.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.ingic.driveuser.BaseApplication;
import com.ingic.driveuser.R;
import com.ingic.driveuser.fragments.HomeFragment;
import com.ingic.driveuser.fragments.SideMenuFragment;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.helpers.BasePreferenceHelper;
import com.ingic.driveuser.interfaces.LoadingListener;
import com.ingic.driveuser.residemenu.ResideMenu;
import com.ingic.driveuser.services.CurrentLocationFinder;
import com.ingic.driveuser.ui.dialogs.DialogFactory;
import com.ingic.driveuser.ui.views.TitleBar;

import java.util.Locale;


/**
 * This class is marked abstract so that it can pair with Dockable Fragments
 * only. All Classes extending this will inherit this functionality of
 * interaction with menus.
 */
public abstract class DockActivity extends AppCompatActivity implements
        LoadingListener {

    public static final String KEY_FRAG_FIRST = "firstFrag";
    public SideMenuFragment sideMenuFragment;
    protected BasePreferenceHelper prefHelper;
    private boolean restartApp=false;
    //For side menu
    protected DrawerLayout drawerLayout;
    BaseFragment baseFragment;


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    public abstract int getDockFrameLayoutId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new BasePreferenceHelper(this);

        String current = getResources().getConfiguration().locale.getLanguage();

        if (!current.equals("en")) {
            prefHelper.putLang(getDockActivity(), "en");
        }else {
         /*   TitleBar titleBar=((MainActivity)getDockActivity()).titleBar;
            titleBar.showMenuButton();
            titleBar.setSubHeading("Home");*/
        }

    }


    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void StartDriverLocationService() {
        startService(new Intent(getApplicationContext(), CurrentLocationFinder.class));
    }

    public void StopDriverLocationService() {
        stopService(new Intent(getApplicationContext(), CurrentLocationFinder.class));
    }

    public String getCountryCode() {
        String CountryID = "";
        String CountryZipCode = "";
        TelephonyManager tm = (TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        CountryID = tm.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);

        for (String aRl : rl) {
            String[] g = aRl.split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public DockActivity getDockActivity() {
        return (DockActivity) this;
    }

    public void replaceDockableFragment(BaseFragment frag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();


    }

    public void replaceDockableFragment(BaseFragment frag, String Tag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag, Tag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();


    }


    public void replaceSideDockableFragment(BaseFragment frag, String Tag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(null)
                .commit();
/* .addToBackStack(
                getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                        : null)*/

    }

    public void replaceDockableFragment(BaseFragment frag, boolean isAnimate) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // if ( isAnimate )
        // if ( !(frag instanceof DashboardFragment) ) {
        // // transaction.setCustomAnimations( R.anim.push_right_in,
        // // R.anim.push_right_out, R.anim.push_left_in,
        // // R.anim.push_left_out );
        // }

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    public void addDockableFragment(BaseFragment frag, String Tag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();


    }


    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();

    }

    public void lockDrawer() {
        try {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void addAndShowDialogFragment(
            android.support.v4.app.DialogFragment dialog) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        dialog.show(transaction, "tag");

    }

    public void prepareAndShowDialog(DialogFragment frag, String TAG,
                                     BaseFragment fragment) {
        FragmentTransaction transaction = fragment.getChildFragmentManager()
                .beginTransaction();
        Fragment prev = fragment.getChildFragmentManager().findFragmentByTag(
                TAG);

        if (prev != null)
            transaction.remove(prev);

        transaction.addToBackStack(null);
        frag.show(transaction, TAG);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            super.onBackPressed();
        else
            DialogFactory.createQuitDialog(this,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DockActivity.this.finish();

                        }
                    }, R.string.message_quit).show();
    }

    public BaseFragment getLastAddedFragment() {
        return baseFragment;
    }

    public void emptyBackStack() {
        //popBackStackTillEntry( 0 );
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
            if (entry != null && (!(entry instanceof HomeFragment)) && (!(entry instanceof SideMenuFragment))) {
                getSupportFragmentManager().popBackStack(entry.getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
    }

    /**
     * @param entryIndex is the index of fragment to be popped to, for example the
     *                   first fragment will have a index 0;
     */
    public void popBackStackTillEntry(int entryIndex) {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex)
            return;
        BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            getSupportFragmentManager().popBackStack(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void popFragment() {
        if (getSupportFragmentManager() == null)
            return;
        getSupportFragmentManager().popBackStack();
    }

    public abstract void onMenuItemActionCalled(int actionId, String data);

    public abstract void setSubHeading(String subHeadText);

    public abstract boolean isLoggedIn();

    public abstract void hideHeaderButtons(boolean leftBtn, boolean rightBtn);

    public BaseApplication getMainApplication() {
        return (BaseApplication) getApplication();
    }

    public ResideMenu.OnMenuListener getMenuListener() {
        return menuListener;
    }

}
