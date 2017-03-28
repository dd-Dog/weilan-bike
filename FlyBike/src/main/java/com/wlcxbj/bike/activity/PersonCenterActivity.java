package com.wlcxbj.bike.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.view.CompatibaleScrollView;

/**
 * Created by Administrator on 2016/11/15.
 */
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.fl_front)
    FrameLayout flFront;
    @Bind(R.id.ib_back)
    ImageView titleBack;
    @Bind(R.id.usr_icon)
    ImageView usrIcon;
    @Bind(R.id.rl_mywallet)
    RelativeLayout myWallet;
    @Bind(R.id.rl_mymsg)
    RelativeLayout myMsg;
    @Bind(R.id.rl_myroute)
    RelativeLayout myRoute;
    @Bind(R.id.rl_invite_friends)
    RelativeLayout inviteFriends;
    @Bind(R.id.rl_guide)
    RelativeLayout guide;
    @Bind(R.id.rl_settings)
    RelativeLayout settings;
    @Bind(R.id.ride_distance)
    TextView tvDistance;
    @Bind(R.id.save_carbon)
    TextView tvCarbon;
    @Bind(R.id.exercise_acheivement)
    TextView tvExercise;
    @Bind(R.id.scrollView)
    CompatibaleScrollView scrollView;
    @Bind(R.id.remaining)
    TextView remaining;


    private static final String TAG = "PersonCenterActivity";
    private int mScrollY = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        mScrollY = 0;
        setTitleBar();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void setTitleBar() {
        scrollView.setScrollViewListener(new CompatibaleScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(CompatibaleScrollView scrollView, int x, int y, int oldx, int oldy) {
                mScrollY = oldy;
                float offset = (809 - (oldy + mScrollY))* 255 / 809;
//                Log.e(TAG, "mscrollY=" + mScrollY + ",scrollY=" + scrollY + ", offset=" + offset);
//                routeTitle.getBackground().setAlpha((int) offset);
//                if (offset <= 50.0) {
//                    routeTitle.setVisibility(View.INVISIBLE);
//                } else {
//                    routeTitle.setVisibility(View.VISIBLE);
//                }
            }

//            @Override
//            public void onScrollChanged(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                mScrollY = scrollY;
//                float offset = (809 - (scrollY + mScrollY))* 255 / 809;
////                Log.e(TAG, "mscrollY=" + mScrollY + ",scrollY=" + scrollY + ", offset=" + offset);
////                routeTitle.getBackground().setAlpha((int) offset);
////                if (offset <= 50.0) {
////                    routeTitle.setVisibility(View.INVISIBLE);
////                } else {
////                    routeTitle.setVisibility(View.VISIBLE);
////                }
//            }
        });
    }


    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_person_center);
    }


    @OnClick({R.id.tv_score, R.id.ib_back, R.id.usr_icon, R.id.rl_guide, R.id.rl_invite_friends, R.id.rl_mymsg, R.id.rl_myroute, R.id.rl_mywallet, R.id.rl_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_mywallet:
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.rl_myroute:
                startActivity(new Intent(this, RouteActivity.class));
                break;
            case R.id.rl_mymsg:
                startActivity(new Intent(this, MyMsgActivity.class));
                break;
            case R.id.rl_guide:
                startActivity(new Intent(this, UserGuideActivity.class));
                break;
            case R.id.rl_invite_friends:
                startActivity(new Intent(this, ShareActivity.class));
                break;
            case R.id.rl_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.usr_icon:
                startActivity(new Intent(PersonCenterActivity.this, UserInfoActivity.class));
                break;
            case R.id.tv_score:
                startActivity(new Intent(PersonCenterActivity.this, ScoreActivity.class));
                break;
        }
    }
}
