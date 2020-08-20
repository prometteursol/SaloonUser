package com.prometteur.saloonuser.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.prometteur.saloonuser.Activities.ActivityLogin;
import com.prometteur.saloonuser.Model.SignoutBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.getLocationPermission;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Preferences.getClearPrefs;

public class Utils {

    static SignoutBean loginBean;

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }


    //validations

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "- Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 1, "+ More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static boolean isValidPassword(final String password) {


        if (password.length() >= 8) {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            //Pattern eight = Pattern.compile (".{8}");


            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        } else
            return false;


    }

    public static MultipartBody.Part[] getImageBody(String strPath) {
        if (strPath != null) {
            if (!strPath.isEmpty()) {
                String[] strPathArr = strPath.split(",");
                MultipartBody.Part[] body = new MultipartBody.Part[strPathArr.length];
                for (int i = 0; i < strPathArr.length; i++) {
                    File file = new File(strPathArr[i]);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    // MultipartBody.Part is used to send also the actual file name
                    body[i] = MultipartBody.Part.createFormData("user_img[]", file.getName(), requestFile);
                }
                return body;
            } else {
                return null;
            }

        }
        return null;
    }

    public static RequestBody getOtherFields(String data) {
        // add another part within the multipart request
        if (data == null) {
            data = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), data);
        return body;
    }

    public static Dialog showProgress(Context context, int txtType) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_progress);
        // set the custom dialog components - text, image and button
        //ImageView ivLogo = (ImageView) dialog.findViewById(R.id.ivLogo);
        TextView text = (TextView) dialog.findViewById(R.id.tvProgressMsg);
       /* if (txtType == 0) {
            text.setText(context.getResources().getString(R.string.please_wait));
        } else {
            text.setText(context.getResources().getString(R.string.loading));
        }*/
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        //image.setImageResource(R.drawable.iclau);

        // Glide.with(context).load(R.drawable.salon_gif_logo).into(ivLogo);

        return dialog;

    }
    public static Dialog dialog;
    public static void showNoInternetDialog(Context context) {
        // custom dialog
        if(dialog==null) {
            dialog = new Dialog(context);
        }
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);
        dialog.show();
    }
    public static void showNoInternetDialogReceiver(Context context) {
        // custom dialog
        if(dialog==null) {
            dialog = new Dialog(context);
        }
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void setNoInternetMsg(RecyclerView recyclerView, ImageView ivNoAppoint) {


        recyclerView.setVisibility(View.GONE);
        ivNoAppoint.setVisibility(View.VISIBLE);

    }


    /*public static void setEmptyMsg(List<OngoingBean.Result> mDataList, ViewPager viewPager, ImageView ivNoAppoint) {
        if (mDataList.size() > 0) {
            viewPager.setVisibility(View.VISIBLE);
            ivNoAppoint.setVisibility(View.GONE);

        } else {
            viewPager.setVisibility(View.GONE);
            ivNoAppoint.setVisibility(View.VISIBLE);
        }
    }
*/

    public static void setEmptyMsg(List mDataList, RecyclerView recyclerView, ImageView ivNoAppoint) {
        if (mDataList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            ivNoAppoint.setVisibility(View.GONE);

        } else {
            recyclerView.setVisibility(View.GONE);
            ivNoAppoint.setVisibility(View.VISIBLE);
        }
    }

    public static List<String> getTimeSlots(String strTimeSlots) {

        List<String> timeSlots = new ArrayList<>();
        String strTimeSlotsStart = strTimeSlots.split("-")[0];
        String strTimeSlotsEnd = strTimeSlots.split("-")[1];

        /*String timeValue = "2015-10-28T18:37:04.899+05:30";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");*/
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Calendar startCalendar = Calendar.getInstance();
            try {
                startCalendar.setTime(sdf.parse(strTimeSlotsStart));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (startCalendar.get(Calendar.MINUTE) == 0) {
                startCalendar.set(Calendar.MINUTE, 00);
            } else if (startCalendar.get(Calendar.MINUTE) < 15) {
                startCalendar.set(Calendar.MINUTE, 15);
            } else if (startCalendar.get(Calendar.MINUTE) >= 15 && startCalendar.get(Calendar.MINUTE) < 30) {
                startCalendar.set(Calendar.MINUTE, 30); // overstep hour and clear minutes
                // startCalendar.clear(Calendar.MINUTE);
            } else if (startCalendar.get(Calendar.MINUTE) >= 30 && startCalendar.get(Calendar.MINUTE) < 45) {
                startCalendar.set(Calendar.MINUTE, 45); // overstep hour and clear minutes
//                startCalendar.clear(Calendar.MINUTE);
            } else {
                startCalendar.clear(Calendar.MINUTE);
                startCalendar.add(Calendar.HOUR, 1);
            }

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.clear(Calendar.MINUTE);
            endCalendar.clear(Calendar.SECOND);
            endCalendar.clear(Calendar.MILLISECOND);
            endCalendar.setTime(sdf.parse(strTimeSlotsEnd));

            SimpleDateFormat slotTimeRes = new SimpleDateFormat("hh:mm a");

            String slotEndTime = slotTimeRes.format(startCalendar.getTime());

            slotEndTime = slotEndTime.replace("am", "AM");
            slotEndTime = slotEndTime.replace("pm", "PM");
            timeSlots.add(slotEndTime);
            while (endCalendar.after(startCalendar)) {

                startCalendar.add(Calendar.MINUTE, 15);
                slotEndTime = slotTimeRes.format(startCalendar.getTime());
                slotEndTime = slotEndTime.replace("am", "AM");
                slotEndTime = slotEndTime.replace("pm", "PM");
                timeSlots.add(slotEndTime);
                Log.d("DATE", slotEndTime);
            }

        } catch (Exception e) {
            // date in wrong format
        }
        if(timeSlots.size()>0)
        {
            timeSlots.remove(timeSlots.size()-1);
        }
        return timeSlots;
    }

    public static String getAppDetDate(String date) {
        SimpleDateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat destDate = new SimpleDateFormat("dd MMMM");

        try {
            Date date1 = sourceDate.parse(date);
            date = destDate.format(date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getReviewDate(Context context, String date) {
        SimpleDateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = "";
        try {
            Date date1 = sourceDate.parse(date);
            formattedDate = (String) DateUtils.getRelativeTimeSpanString(date1.getTime(), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String getPointsAddedDate(String date) {
        SimpleDateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat destDate = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = "";
        try {
            Date date1 = sourceDate.parse(date);
            formattedDate = destDate.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static void showFailToastForTimeCheck(Context context, String msg) { //for big time only
        // Toasty.error(context, msg, Toast.LENGTH_SHORT, true).show();
        Toasty.custom(context, msg, context.getDrawable(R.drawable.ic_cross_grey), context.getResources().getColor(R.color.red), context.getResources().getColor(R.color.white), Toast.LENGTH_LONG, true, true).show();
    }

    public static void showFailToast(Context context, String msg) {
        // Toasty.error(context, msg, Toast.LENGTH_SHORT, true).show();
        Toasty.custom(context, msg, context.getDrawable(R.drawable.ic_cross_grey), context.getResources().getColor(R.color.red), context.getResources().getColor(R.color.white), Toast.LENGTH_SHORT, true, true).show();
    }

    public static void showSuccessToast(Context context, String msg) {
        //  Toasty.success(context, msg, Toast.LENGTH_SHORT, true).show();
        Toasty.custom(context, msg, context.getDrawable(R.drawable.ic_tick_service), context.getResources().getColor(R.color.green), context.getResources().getColor(R.color.white), Toast.LENGTH_SHORT, true, true).show();
    }

    public static void showInfoToast(Activity context, String msg) {
        Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show();

    }

    public static void showFailToastLongTime(Context context, String msg) {
        // Toasty.error(context, msg, Toast.LENGTH_SHORT, true).show();
        Toasty.custom(context, msg, context.getDrawable(R.drawable.ic_cross_grey), context.getResources().getColor(R.color.red), context.getResources().getColor(R.color.white), Toast.LENGTH_LONG, true, true).show();
    }

    public static void logout(Context context) {

        getLogOut(context);

        getClearPrefs(context);
        context.startActivity(new Intent(context, ActivityLogin.class));
        ((Activity) context).finishAffinity();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean getValidDate(String date) {
        SimpleDateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = sourceDate.parse(date);
            if (date1.getTime() > System.currentTimeMillis()) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void enableLocation(final Context context) {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled && !network_enabled) {
                // notify user
                try {
                    new AlertDialog.Builder(context)
                            .setMessage("GPS Location not enabled")
                            .setPositiveButton("Open Location setting", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            //.setNegativeButton(R.string.Cancel,null)
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                getLocationPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getDateShowDDMMMYYYY(String date) {
        SimpleDateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat destDate = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date1 = sourceDate.parse(date);
            date = destDate.format(date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getDateShowDayDDMMMYYYY(String date) {
        SimpleDateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat destDate = new SimpleDateFormat("E, dd MMM yyyy");

        try {
            Date date1 = sourceDate.parse(date);
            date = destDate.format(date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getDateShowDayDDMMMHHMM(String date) {
        SimpleDateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat destDate = new SimpleDateFormat("E, dd MMM, hh:mm a");

        try {
            Date date1 = sourceDate.parse(date);
            date = destDate.format(date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date = date.replace("am", "AM").replace("pm", "PM");
        return date;
    }

    public static String getTimeShow24to12HR(String time) {
        time = time.trim();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat slotTimeRes = new SimpleDateFormat("hh:mm a");

        Date changeFTime = null;
        try {
            changeFTime = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIn12Hr = slotTimeRes.format(changeFTime.getTime());
        timeIn12Hr = timeIn12Hr.replace("am", "AM").replace("pm", "PM");
        return timeIn12Hr;
    }

    public static String getTimeShow12to24HR(String time) {
        time = time.trim();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat slotTimeRes = new SimpleDateFormat("hh:mm a");

        Date changeFTime = null;
        try {
            changeFTime = slotTimeRes.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIn24Hr = sdf.format(changeFTime.getTime());
        return timeIn24Hr;
    }

    private static void getLogOut(final Context context) {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);

        String userFcmKey = FirebaseInstanceId.getInstance().getToken();
        Log.i("FCM Key", userFcmKey);
        service.getLogout(USERIDVAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SignoutBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SignoutBean loginBeanObj) {
                        loginBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        showFailToast(context, context.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        if (loginBean.getStatus() == 1) {

                        } else {
                            showFailToast(context, "" + loginBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }

}
