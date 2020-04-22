package com.rooio.repairs;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

@RunWith(AndroidJUnit4.class)
public final class LandingTest {
    private Landing activity;

    @Before
    public final void setUp() throws Exception {
        this.activity = (Landing)Robolectric.buildActivity(Landing.class).create().resume().get();
    }

    @Test
    public final void testConnectAccount() {
        Landing var10000 = this.activity;
        if (var10000 == null) {
            Intrinsics.throwNpe();
        }

        View var4 = var10000.findViewById(-1000532);
        if (var4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.widget.Button");
        } else {
            Button button = (Button)var4;
            button.performClick();
            Intent expectedIntent = new Intent((Context)this.activity, Login.class);
            ShadowApplication var5 = Shadows.shadowOf(new Application());
            Intrinsics.checkExpressionValueIsNotNull(var5, "shadowOf(Application())");
            Intent var6 = var5.getNextStartedActivity();
            Intrinsics.checkExpressionValueIsNotNull(var6, "shadowOf(Application()).nextStartedActivity");
            Intent actual = var6;
            Assert.assertEquals(expectedIntent.getComponent(), actual.getComponent());
        }
    }

    @Test
    public final void testCreateAccount() {
        Landing var10000 = this.activity;
        if (var10000 == null) {
            Intrinsics.throwNpe();
        }

        View var4 = var10000.findViewById(-1000361);
        if (var4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.widget.Button");
        } else {
            Button button = (Button)var4;
            button.performClick();
            Intent expectedIntent = new Intent((Context)this.activity, Registration.class);
            ShadowApplication var5 = Shadows.shadowOf(new Application());
            Intrinsics.checkExpressionValueIsNotNull(var5, "shadowOf(Application())");
            Intent var6 = var5.getNextStartedActivity();
            Intrinsics.checkExpressionValueIsNotNull(var6, "shadowOf(Application()).nextStartedActivity");
            Intent actual = var6;
            Assert.assertEquals(expectedIntent.getComponent(), actual.getComponent());
        }
    }
}