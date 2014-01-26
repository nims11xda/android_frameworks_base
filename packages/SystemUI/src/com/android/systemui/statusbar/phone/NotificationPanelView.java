/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
<<<<<<< HEAD
import android.os.UserHandle;
=======
>>>>>>> PA/kitkat
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.SettingConfirmationHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.android.systemui.EventLogTags;
import com.android.systemui.R;
import com.android.systemui.statusbar.GestureRecorder;

public class NotificationPanelView extends PanelView {
    public static final boolean DEBUG_GESTURES = true;

    private static final float STATUS_BAR_LEFT_PERCENTAGE = 0.7f;
    private static final float STATUS_BAR_RIGHT_PERCENTAGE = 0.3f;

    private Drawable mHandleBar;
    private int mHandleBarHeight;
    private View mHandleView;
    private int mFingers;
    private PhoneStatusBar mStatusBar;
    private boolean mOkToFlip;
<<<<<<< HEAD
=======
    private static final float QUICK_PULL_DOWN_PERCENTAGE = 0.8f;
>>>>>>> PA/kitkat

    public NotificationPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStatusBar(PhoneStatusBar bar) {
        mStatusBar = bar;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Resources resources = getContext().getResources();
        mHandleBar = resources.getDrawable(R.drawable.status_bar_close);
        mHandleBarHeight = resources.getDimensionPixelSize(R.dimen.close_handle_height);
        mHandleView = findViewById(R.id.handle);
    }

    @Override
    public void fling(float vel, boolean always) {
        GestureRecorder gr = ((PhoneStatusBarView) mBar).mBar.getGestureRecorder();
        if (gr != null) {
            gr.tag(
                "fling " + ((vel > 0) ? "open" : "closed"),
                "notifications,v=" + vel);
        }
        super.fling(vel, always);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            event.getText()
                    .add(getContext().getString(R.string.accessibility_desc_notification_shade));
            return true;
        }

        return super.dispatchPopulateAccessibilityEvent(event);
    }

    // We draw the handle ourselves so that it's always glued to the bottom of the window.
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            final int pl = getPaddingLeft();
            final int pr = getPaddingRight();
            mHandleBar.setBounds(pl, 0, getWidth() - pr, (int) mHandleBarHeight);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final int off = (int) (getHeight() - mHandleBarHeight - getPaddingBottom());
        canvas.translate(0, off);
        mHandleBar.setState(mHandleView.getDrawableState());
        mHandleBar.draw(canvas);
        canvas.translate(0, -off);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (DEBUG_GESTURES) {
            if (event.getActionMasked() != MotionEvent.ACTION_MOVE) {
                EventLog.writeEvent(EventLogTags.SYSUI_NOTIFICATIONPANEL_TOUCH,
                       event.getActionMasked(), (int) event.getX(), (int) event.getY());
            }
        }
        if (PhoneStatusBar.SETTINGS_DRAG_SHORTCUT && mStatusBar.mHasFlipSettings) {

            boolean flip = false;
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mOkToFlip = getExpandedHeight() == 0;
<<<<<<< HEAD
                    if (event.getX(0) > getWidth() * (1.0f - STATUS_BAR_RIGHT_PERCENTAGE) &&
                            Settings.System.getIntForUser(getContext().getContentResolver(),
                            Settings.System.QS_QUICK_PULLDOWN, 1, UserHandle.USER_CURRENT) == 1) {
                        flip = true;
                    } else if (event.getX(0) < getWidth() * (1.0f - STATUS_BAR_LEFT_PERCENTAGE) &&
                            Settings.System.getIntForUser(getContext().getContentResolver(),
                            Settings.System.QS_QUICK_PULLDOWN, 1, UserHandle.USER_CURRENT) == 2) {
=======
                    if (event.getX(0) > getWidth() * QUICK_PULL_DOWN_PERCENTAGE) {
>>>>>>> PA/kitkat
                        flip = true;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    flip = true;
                    break;
            }
            if (mOkToFlip && flip) {
                float miny = event.getY(0);
                float maxy = miny;
                for (int i=1; i<event.getPointerCount(); i++) {
                    final float y = event.getY(i);
                    if (y < miny) miny = y;
                    if (y > maxy) maxy = y;
                }
                if (maxy - miny < mHandleBarHeight) {
                    if (mJustPeeked || getExpandedHeight() < mHandleBarHeight) {
                        SettingConfirmationHelper helper = new SettingConfirmationHelper(mContext);
                        helper.showConfirmationDialogForSetting(
                                mContext.getString(R.string.quick_settings_quick_pull_down_title),
                                mContext.getString(R.string.quick_settings_quick_pull_down_message),
                                mContext.getResources().getDrawable(R.drawable.quick_pull_down),
                                Settings.System.QUICK_SETTINGS_QUICK_PULL_DOWN);
                        if(Settings.System.getInt(mContext.getContentResolver(),
                                    Settings.System.QUICK_SETTINGS_QUICK_PULL_DOWN, 0) != 2) {
                            mStatusBar.switchToSettings();
                        }
                    } else {
                        mStatusBar.flipToSettings();
                    }
                    mOkToFlip = false;
                }
            }
        }
        return mHandleView.dispatchTouchEvent(event);
    }
}
