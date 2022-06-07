package top.ntutn.zerohelper;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.PixelCopy;
import android.view.Window;
import androidx.appcompat.app.AppCompatDialog;
import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

public class Lancet {
    private static final String TAG = "Lancet";

    @Proxy("show")
    @TargetClass(value = "androidx.appcompat.app.AppCompatDialog", scope = Scope.ALL)
    public void hookDialogShow() {
        AppCompatDialog dialog = (AppCompatDialog) This.get();
        Log.i(TAG, "弹窗" + dialog);
        Origin.callVoid();
        try {
            afterDialogShow(dialog);
        } catch (Throwable tr) {
            Log.e(TAG, "", tr);
        }
    }

    public static void afterDialogShow(AppCompatDialog dialog) {
        Rect drawRect = new Rect();
        Window window = dialog.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(drawRect);
        drawRect.top = drawRect.bottom - 1;
        drawRect.left = drawRect.right / 2 - 1;
        drawRect.right = drawRect.right / 2;

        window.getDecorView().postDelayed(() -> {
            try {
                Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PixelCopy.request(window, drawRect, bitmap, (int copyResult) -> {
                        if (copyResult == PixelCopy.SUCCESS) {
                            int color = bitmap.getPixel(0, 0);
                            window.setNavigationBarColor(color);
                        }
                    }, new Handler(Looper.getMainLooper()));
                }
            } catch (Throwable tr) {
                Log.e(TAG, "", tr);
            }
        }, 100L);
    }
}
