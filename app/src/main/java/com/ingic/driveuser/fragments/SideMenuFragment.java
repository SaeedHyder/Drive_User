package com.ingic.driveuser.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.NavigationEnt;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.DialogHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.adapters.ArrayListAdapter;
import com.ingic.driveuser.ui.viewbinders.abstracts.NavigationItemBinder;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SideMenuFragment extends BaseFragment {

    @BindView(R.id.img_driver)
    CircleImageView imgDriver;
    @BindView(R.id.txt_drivername)
    AnyTextView txtDrivername;
    @BindView(R.id.ll_driver)
    LinearLayout llDriver;

    @BindView(R.id.sideoptions)
    ListView sideoptions;

    @BindView(R.id.txt_driveoption)
    AnyTextView txtDriveoption;
    private ArrayList<NavigationEnt> navigationEnts;
    private ArrayListAdapter<NavigationEnt> adapter;

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();

    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<NavigationEnt>(getDockActivity(), new NavigationItemBinder(getDockActivity()));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sidemenu, container, false);

        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BindData();
        if (prefHelper.getUser() != null) {
            Glide.with(this).load(prefHelper.getUser().getProfileImage() + "").into(imgDriver);
            txtDrivername.setText(prefHelper.getUser().getFullName() + "");
        }
        setGooglePlayShortcut(getResources().getString(R.string.drive_with_fastcab),
                getResources().getString(R.string.fastcab), txtDriveoption);


    }

    public void refreshSideMenuData() {
        if (prefHelper != null && prefHelper.getUser() != null) {
            Glide.with(this).load(prefHelper.getUser().getProfileImage() + "").into(imgDriver);
            txtDrivername.setText(prefHelper.getUser().getFullName() + "");
        }
    }


    private void setGooglePlayShortcut(String text, String spanText, AnyTextView txtview) {

        txtview.setText(text);

        txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppORPlaystore(getResources().getString(R.string.driver_app_package_name));
            }
        });

        /*SpannableStringBuilder stringBuilder = ClickableSpanHelper.initSpan(text);
        ClickableSpanHelper.setSpan(stringBuilder, text, spanText, new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.white));    // you can use custom color
                ds.setTypeface(Typeface.DEFAULT_BOLD);
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(View widget) {

            }
        });

        ClickableSpanHelper.setClickableSpan(txtview, stringBuilder);*/
    }

    private void bindview() {
        adapter.clearList();
        sideoptions.setAdapter(adapter);
        adapter.addAll(navigationEnts);
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(sideoptions);
        sideoptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (navigationEnts.get(position).getTitle().equals(getString(R.string.home))) {
                    // getMainActivity().getResideMenu().closeMenu();
                    getMainActivity().closeDrawer();
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(), "HomeMapFragment");
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.notification))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    if (currentFragment instanceof NotificationFragment) {
                        getMainActivity().closeDrawer();
                    } else {
                        // getMainActivity().getResideMenu().closeMenu();
                        getMainActivity().closeDrawer();
                        getDockActivity().popBackStackTillEntry(0);
                        //getDockActivity().replaceDockableFragment(NotificationFragment.newInstance(), "NotificationFragment");
                        getDockActivity().addDockableFragment(NotificationFragment.newInstance(), "NotificationFragment");
                    }
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.profile))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    if (currentFragment instanceof ProfileFragment) {
                        getMainActivity().closeDrawer();
                    } else {
                        // getMainActivity().getResideMenu().closeMenu();
                        getMainActivity().closeDrawer();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().addDockableFragment(ProfileFragment.newInstance(), "ProfileFragment");
                    }
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.your_trips))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    if (currentFragment instanceof TripsFragment) {
                        getMainActivity().closeDrawer();
                    } else {
                        // getMainActivity().getResideMenu().closeMenu();
                        getMainActivity().closeDrawer();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().addDockableFragment(TripsFragment.newInstance(), "TripsFragment");
                    }
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.wallet))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    // getMainActivity().getResideMenu().closeMenu();
                    getMainActivity().closeDrawer();
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().addDockableFragment(WalletFragment.newInstance(), "WalletFragment");
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.contact))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    if (currentFragment instanceof ContactUsFragment) {
                        getMainActivity().closeDrawer();
                    } else {
                        // getMainActivity().getResideMenu().closeMenu();
                        getMainActivity().closeDrawer();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().addDockableFragment(ContactUsFragment.newInstance(), "ContactUsFragment");
                    }
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.about_us))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    if (currentFragment instanceof AboutUsFragment) {
                        getMainActivity().closeDrawer();
                    } else {
                        // getMainActivity().getResideMenu().closeMenu();
                        getMainActivity().closeDrawer();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().addDockableFragment(AboutUsFragment.newInstance(), "AboutUsFragment");
                    }
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.invite_earn))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    if (currentFragment instanceof InviteAndEarnFragment) {
                        getMainActivity().closeDrawer();
                    } else {
                        // getMainActivity().getResideMenu().closeMenu();
                        getMainActivity().closeDrawer();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().addDockableFragment(InviteAndEarnFragment.newInstance(), "InviteAndEarnFragment");
                    }
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.arabic_english))) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Will be Implemented in Beta Version");
                    // getMainActivity().getResideMenu().closeMenu();
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.settings))) {
                    android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                    if (currentFragment instanceof SettingFragment) {
                        getMainActivity().closeDrawer();
                    } else {
                        getMainActivity().closeDrawer();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().addDockableFragment(SettingFragment.newInstance(), "SettingFragment");
                    }
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.logoout))) {

                    final DialogHelper logoutdialog = new DialogHelper(getDockActivity());
                    logoutdialog.logout(R.layout.logout_dialog, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMainActivity().closeDrawer();
                            logoutdialog.hideDialog();
                            logoutUser();

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMainActivity().closeDrawer();
                            logoutdialog.hideDialog();
                        }
                    });
                    logoutdialog.setCancelable(false);
                    logoutdialog.showDialog();
                }
            }
        });
    }

    private void logoutUser() {

        loadingStarted();
        Call<ResponseWrapper> call = headerWebService.LogoutUser();

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                    token.updateTokenService();
                } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    getMainActivity().closeDrawer();
                    prefHelper.setLoginStatus(false);
                    prefHelper.setRideInSession(false);
                    prefHelper.removeRideSessionPreferences();
                    getDockActivity().StopDriverLocationService();
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), LoginFragment.class.getSimpleName());

                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                Log.e(SideMenuFragment.class.getSimpleName(), t.toString());
            }
        });


    }

    private void BindData() {
        navigationEnts = new ArrayList<>();
        navigationEnts.add(new NavigationEnt(R.drawable.drive_home, getResources().getString(R.string.home)));
        navigationEnts.add(new NavigationEnt(R.drawable.drive_notifications, getResources().getString(R.string.notification)));
        navigationEnts.add(new NavigationEnt(R.drawable.drive_profile, getResources().getString(R.string.profile)));
        navigationEnts.add(new NavigationEnt(R.drawable.yourtrips, getResources().getString(R.string.your_trips)));
        navigationEnts.add(new NavigationEnt(R.drawable.payments, getResources().getString(R.string.wallet)));
        navigationEnts.add(new NavigationEnt(R.drawable.contact, getResources().getString(R.string.contact)));
        navigationEnts.add(new NavigationEnt(R.drawable.aboutus, getResources().getString(R.string.about_us)));
        navigationEnts.add(new NavigationEnt(R.drawable.drive_invite, getResources().getString(R.string.invite_earn)));
      /*  navigationEnts.add(new NavigationEnt(R.drawable.language, getResources().getString(R.string.arabic_english)));*/
        navigationEnts.add(new NavigationEnt(R.drawable.setting, getResources().getString(R.string.settings)));
        navigationEnts.add(new NavigationEnt(R.drawable.drive_logout, getResources().getString(R.string.logoout)));
        bindview();

    }

    private void openAppORPlaystore(String packageName) {
        Intent i;
        PackageManager manager = getDockActivity().getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage(packageName);
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

//if not found in device then will come here
            // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


}
