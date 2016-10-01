package kr.edcan.neologism.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.net.MalformedURLException;

import kr.edcan.neologism.R;

/**
 * Created by JunseokOh on 2016. 9. 24..
 */
public class SupportHelper {
    Context context;

    public SupportHelper(Context context) {

        this.context = context;
    }

    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 3.5f;

    public static String convertTwitterImgSize(String url, int type) throws MalformedURLException {
        if (type > 3) return "";
        else {
        /*
        * mini 0
        * normal 1
        * bigger 2
         * origin 3*/
            if (!url.contains("pbs.twimg.com/profile_images/"))
                throw new MalformedURLException("Not Well-formed Twitter Image Url");
            else {
                String[] originUrl = url.split("_");
                String[] typeReplace = {"_mini", "_normal", "_bigger", ""};
                String result = "";
                for (int i = 0; i < originUrl.length - 1; i++) {
                    if (i != 0 && i != originUrl.length - 1) result += "_";
                    result += originUrl[i];
                }
                result += typeReplace[type] + ".jpg";
                return result;
            }
        }
    }

    public static String convertFacebookImgSize(String userId, int type) {
        if (type > 2) return "";
        else {
            String[] typeReplace = {"small", "normal", "large"};
            return "https://graph.facebook.com/" + userId + "/picture?type=" + typeReplace[type];
        }
    }

    public static Bitmap blur(Bitmap image, Context context) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public void showAlertDialog(String contentString, MaterialDialog.SingleButtonCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_view, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        TextView subContent = (TextView) view.findViewById(R.id.dialog_subContent);
        content.setVisibility(View.GONE);
        subContent.setText(contentString);
        subContent.setTextColor(context.getResources().getColor(R.color.textColor));
        title.setText("CumChuck!");
        new MaterialDialog.Builder(context)
                .customView(view, false)
                .positiveColor(context.getResources().getColor(R.color.colorPrimary))
                .positiveText("확인")
                .negativeText("취소")
                .onPositive(callback)
                .show();
    }

    public void showAlertDialog(String titleString, String contentString, MaterialDialog.SingleButtonCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_view, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        TextView subConent = (TextView) view.findViewById(R.id.dialog_subContent);
        content.setVisibility(View.GONE);
        subConent.setText(contentString);
        subConent.setTextColor(context.getResources().getColor(R.color.textColor));
        title.setText(titleString);
        new MaterialDialog.Builder(context)
                .customView(view, false)
                .positiveColor(context.getResources().getColor(R.color.colorPrimary))
                .positiveText("확인")
                .negativeText("취소")
                .onPositive(callback)
                .show();
    }

    public void showAlertDialog(String titleString, String contentString, String subContentString, MaterialDialog.SingleButtonCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_view, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        TextView subConent = (TextView) view.findViewById(R.id.dialog_subContent);
        content.setText(contentString);
        title.setText(titleString);
        subConent.setText(subContentString);

        new MaterialDialog.Builder(context)
                .customView(view, false)
                .positiveColor(context.getResources().getColor(R.color.colorPrimary))
                .positiveText("확인")
                .negativeText("취소")
                .onPositive(callback)
                .show();
    }

}
