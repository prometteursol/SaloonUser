package com.prometteur.saloonuser.Utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.HeaderViewBinding;


public class HeaderView extends LinearLayout {

   HeaderViewBinding headerViewBinding;
 Context nContext;
    TextView title;
    TextView subTitle;
    TextView header_view_status;
    TextView tvReviewCount;
    TextView tvDiscount;
    RelativeLayout rlRatingSection;
    RatingBar ratingBar;
LinearLayout linBadge;
    public HeaderView(Context context) {
        super(context);
        this.nContext=context;
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
      title=findViewById(R.id.header_view_title);
        subTitle=findViewById(R.id.header_view_sub_title);
        ratingBar=findViewById(R.id.ratingBar);
        linBadge=findViewById(R.id.linBadge);
        header_view_status=findViewById(R.id.header_view_status);
        tvReviewCount=findViewById(R.id.tvReviewCount);
        tvDiscount=findViewById(R.id.tvDiscount);
        rlRatingSection=findViewById(R.id.rlRatingSection);


    }

   /* public void bindTo(String title) {
        bindTo(title, "",0,"0","OPEN","0");
    }*/

    public void bindTo(final String title, String subTitle, float ratingBarVal, String reviewCount, String status, String discount, final String lat, final String lon) {
        hideOrSetText(this.title, title);
        hideOrSetText(this.subTitle, subTitle);
        hideOrSetTextDis(this.tvDiscount, discount);
        final TextView tit=this.title;
        this.title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.getContext().startActivity(new Intent(view.getContext(),ActivityHomepage.class).putExtra("branchId",branchId));
                showMap(tit.getContext(),lat,lon);
            }
        });this.subTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.getContext().startActivity(new Intent(view.getContext(), ActivityHomepage.class).putExtra("branchId",branchId));
                showMap(tit.getContext(),lat,lon);
            }
        });
        if(!status.equalsIgnoreCase("OPEN"))
        {
            this.header_view_status.setBackground(getResources().getDrawable(R.drawable.status_rounded_red_background));
        }
        hideOrSetText(this.header_view_status, status);
        hideOrSetText(this.tvReviewCount, "("+reviewCount+" Reviews)");
        if(ratingBarVal==0.0)
        {
            ratingBar.setVisibility(View.GONE);
            //header_view_status.setVisibility(VISIBLE);
        }else {
           // header_view_status.setVisibility(GONE);
            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setRating(ratingBarVal);
        }
        if(discount==null || discount.equalsIgnoreCase("0"))
        {
            header_view_status.setVisibility(View.GONE);
            linBadge.setVisibility(View.INVISIBLE);
        }else
        {
            hideShowBadge(false);
        }

    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(View.GONE);
        else
            tv.setText(text);
    }
    private void hideOrSetTextDis(TextView tv, String text) {
        if (text == null || text.equals(""))
            linBadge.setVisibility(View.INVISIBLE);
        else
            tv.setText(text+"% *");
    }

public void hideShowRating(boolean val)
{
    if(val) {
        ratingBar.setVisibility(View.INVISIBLE);
    }else
    {
        ratingBar.setVisibility(View.VISIBLE);
    }
}
public void hideShowBadge(boolean badgeFlag)
{
    if(badgeFlag)
    {
        header_view_status.setVisibility(View.VISIBLE);
        linBadge.setVisibility(View.GONE);
        ratingBar.setVisibility(View.INVISIBLE);
        rlRatingSection.setVisibility(View.GONE);
    }
    else
    {
        header_view_status.setVisibility(View.GONE);
        linBadge.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        rlRatingSection.setVisibility(View.VISIBLE);
    }
}
/*public void hideShowBadgeNull(boolean badgeFlag)
{
    if(badgeFlag)
    {
        header_view_status.setVisibility(GONE);
        linBadge.setVisibility(GONE);
    }
}*/
private void showMap(Context context,String lat,String lon)
{
    try {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon + "");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }catch (Exception e)
    {
        e.printStackTrace();
    }
}

}
