package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.databinding.FragmentAboutSalonBinding;

import static com.prometteur.saloonuser.Activities.ActivitySalonProfile.salonDetailBean;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow24to12HR;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAboutSalon extends Fragment {

    FragmentAboutSalonBinding aboutSalonBinding;

    Activity nActivity;

    public FragmentAboutSalon() {
        // Required empty public constructor
    }

    public FragmentAboutSalon(Activity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aboutSalonBinding=FragmentAboutSalonBinding.inflate(inflater,container,false);
        View view=aboutSalonBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(salonDetailBean!=null) {
            if(salonDetailBean.getResult().size()>0) {
                final SalonDetailBean.Result result = salonDetailBean.getResult().get(0);
                aboutSalonBinding.tvAbout.setText(result.getBranDiscription());
                //aboutSalonBinding.tvAbout.setText("klgkldfgkldfkjgdfkgjkdfg df gkldfjklg dfklgj dfklgj dfklg dfklg kldfgjkldf jgkldfj gkldfk gk dfg df gdsfgkljdfkg dfklgj dfkgj kldf jgkldf jgkldsf gkldf gkldsf gsdf gl sdfglkdsf klg dfkgldf kgljdfklg dfklgj dfklgj dfkgjkdf gkldf gkldf gkldf gkdflgklsdfj gkldfjklgjsdfkg df gkld fg dfklgjkdfljgkldf gklsdfg klsdf gkldfjg dfkljg kldfjgkldfjgkldfj gkldfg kdflgj kldfg lkdfg kldf glkdf gdfjg ldfj gkldfjgkdf gkldfgklsdfjg dflkg dflg ldf gdflgdsfljgldsf gd fg dfgkldfkgljdfgkldflgdsf gl dfl gdsf g df lgjdflgjdfkg sdf g ldfjglkdfjkgjdfklgjdfklgjldfk glsdfg dfklgjdflk gdfklgjlkdfj gljdfgj dflg dflgjkldfgjdflkg dflg kldfgkdflg dfklgjdfl jgkldgldfgkljriogfjgoierjgioreotierodfg  ogierjoitjieroteri godf goerijvfdklmvklcgofk gdfg dfogjdfiog og fdogjdfijg iodfgiodfjgorklfdgnkdfgnkfdnglkdfngkdfngkdfngkdfngkdfgkldflkgdfngkldfg fgfdgfdgf fdgfdg klgkldfgkldfkjgdfkgjkdfg df gkldfjklg dfklgj dfklgj dfklg dfklg kldfgjkldf jgkldfj gkldfk gk dfg df gdsfgkljdfkg dfklgj dfkgj kldf jgkldf jgkldsf gkldf gkldsf gsdf gl sdfglkdsf klg dfkgldf kgljdfklg dfklgj dfklgj dfkgjkdf gkldf gkldf gkldf gkdflgklsdfj gkldfjklgjsdfkg df gkld fg dfklgjkdfljgkldf gklsdfg klsdf gkldfjg dfkljg kldfjgkldfjgkldfj gkldfg kdflgj kldfg lkdfg kldf glkdf gdfjg ldfj gkldfjgkdf gkldfgklsdfjg dflkg dflg ldf gdflgdsfljgldsf gd fg dfgkldfkgljdfgkldflgdsf gl dfl gdsf g df lgjdflgjdfkg sdf g ldfjglkdfjkgjdfklgjdfklgjldfk glsdfg dfklgjdflk gdfklgjlkdfj gljdfgj dflg dflgjkldfgjdflkg dflg kldfgkdflg dfklgjdfl jgkldgldfgkljriogfjgoierjgioreotierodfg  ogierjoitjieroteri godf goerijvfdklmvklcgofk gdfg dfogjdfiog og fdogjdfijg iodfgiodfjgorklfdgnkdfgnkfdnglkdfngkdfngkdfngkdfngkdfgkldflkgdfngkldfg fgfdgfdgf fdgfdg");
                String strAddress = "";
                if (!result.getBranAddr().isEmpty()) {
                    strAddress = result.getBranAddr();
                }
                if (!result.getBranCity().isEmpty()) {
                    strAddress = strAddress + ", " + result.getBranCity();
                }
                if (!result.getBranState().isEmpty()) {
                    strAddress = strAddress + "\n" + result.getBranState() + "-" + result.getBranPinCode() + ".";
                }
                aboutSalonBinding.tvAddress.setText(strAddress);
aboutSalonBinding.tvAddress.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //startActivity(new Intent(nActivity, ActivityHomepage.class).putExtra("branchId",result.getBranId()));
        showMap(nActivity,result.getBranLat(),result.getBranLon());
    }
});
                if (!result.getBranWorkingHrs().isEmpty()) {
                    aboutSalonBinding.tvHoursMtoF.setText(getTimeShow24to12HR(result.getBranWorkingHrs().split("-")[0])+" - "+getTimeShow24to12HR(result.getBranWorkingHrs().split("-")[1]));
                }

                if (!result.getBranOffDay().isEmpty()) {
                    aboutSalonBinding.tvHoursStoS.setText(result.getBranOffDay());
                }else
                {
                    aboutSalonBinding.tvHoursStoS.setText("-");
                }

                if (!result.getBranHolidayFrom().equalsIgnoreCase("0000-00-00") && !result.getBranHolidayTo().equalsIgnoreCase("0000-00-00")) {
                    aboutSalonBinding.tvHoliday.setText(getDateShowDDMMMYYYY(result.getBranHolidayFrom()) + " To " + getDateShowDDMMMYYYY(result.getBranHolidayTo()));
                }else
                {
                    aboutSalonBinding.tvHoliday.setText("-");
                }
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        nActivity= (Activity) context;
    }

    private void showMap(Context context, String lat, String lon)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lon+"");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }
}
