
package com.a.me.imid.swipebacklayout.lib.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a.me.imid.swipebacklayout.lib.SwipeBackLayout;
import com.a.me.imid.swipebacklayout.lib.Utils;


public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this, needAddParent());
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    Toolbar.LayoutParams toolBarTitleDefaultLayoutParams = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (titleCenter()) {
            ViewGroup decor = (ViewGroup) getWindow().getDecorView();
            ViewGroup contentParent = (ViewGroup) decor.findViewById(android.support.v7.appcompat.R.id.action_bar);
            if (contentParent != null) {
                Toolbar toolbar = (Toolbar) contentParent;
                if (menu.size() == 0) {
                    for (int i = 0; i < toolbar.getChildCount(); i++) {
                        if (toolbar.getChildAt(i) instanceof TextView) {
                            TextView title = (TextView) toolbar.getChildAt(i);
                            if (toolBarTitleDefaultLayoutParams == null) {
                                toolBarTitleDefaultLayoutParams = (Toolbar.LayoutParams) title.getLayoutParams();
                            }
                            Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) title.getLayoutParams();
                            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            layoutParams.rightMargin = getResources().getDimensionPixelSize(android.support.v7.appcompat.R.dimen.abc_action_bar_default_height_material);
                            title.setGravity(Gravity.CENTER);
                            title.setLayoutParams(layoutParams);
                        }
                    }
                } else {
                    if (toolBarTitleDefaultLayoutParams != null) {
                        for (int i = 0; i < toolbar.getChildCount(); i++) {
                            if (toolbar.getChildAt(i) instanceof TextView) {
                                TextView title = (TextView) toolbar.getChildAt(i);
                                title.setLayoutParams(toolBarTitleDefaultLayoutParams);
                                title.setGravity(Gravity.LEFT);
                            }
                        }
                    }
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public boolean needAddParent() {
        return true;
    }

    public boolean titleCenter() {
        return true;
    }
}
