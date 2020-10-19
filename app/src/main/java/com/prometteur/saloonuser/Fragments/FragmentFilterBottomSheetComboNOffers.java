package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Adapter.FilterShowProductsAdapter;
import com.prometteur.saloonuser.Model.CategoryBrandBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.FragmentFilterBottomSheetServicesBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.comboAndOffersBinding;
import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.filterBottomSheetComboNOffers;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brands;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brandsArr;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.category;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.discount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.gender;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.highToLow;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.isFilter;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.lowToHigh;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.rating;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCatsText;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.sortBy;
import static com.prometteur.saloonuser.Activities.ActivitySalonServices.filterBottomSheetServices;
import static com.prometteur.saloonuser.Activities.ActivitySalonServices.mainServicesAdapter;
import static com.prometteur.saloonuser.Fragments.FragmentComboPage.getComboListDetails;
import static com.prometteur.saloonuser.Fragments.FragmentOfferPage.getPromoOffListDetails;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;


public class FragmentFilterBottomSheetComboNOffers extends BottomSheetDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    FragmentFilterBottomSheetServicesBinding filterBinding;
    Activity nActivity;

    public FragmentFilterBottomSheetComboNOffers(Activity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        filterBinding = FragmentFilterBottomSheetServicesBinding.inflate(inflater, container, false);
        View view = filterBinding.getRoot();

        filterBinding.cbCatList.setChecked(false);
        filterBinding.cbBrandList.setChecked(false);

        filterBinding.cbCostLowToHigh.setChecked(false);
        filterBinding.cbCostHighToLow.setChecked(false);

        filterBinding.cbHair.setChecked(false);
        filterBinding.cbSkin.setChecked(false);
        filterBinding.cbSpa.setChecked(false);
        filterBinding.cbNails.setChecked(false);
        filterBinding.cbMakeup.setChecked(false);

        filterBinding.cbCostLowToHigh.setOnCheckedChangeListener(this);
        filterBinding.cbCostHighToLow.setOnCheckedChangeListener(this);

        filterBinding.cbHair.setOnCheckedChangeListener(this);
        filterBinding.cbSkin.setOnCheckedChangeListener(this);
        filterBinding.cbSpa.setOnCheckedChangeListener(this);
        filterBinding.cbNails.setOnCheckedChangeListener(this);
        filterBinding.cbMakeup.setOnCheckedChangeListener(this);

        filterBinding.cbCatList.setOnCheckedChangeListener(this);
        filterBinding.cbBrandList.setOnCheckedChangeListener(this);

        setDrawableTextView(R.drawable.ic_keyboard_arrow_down_black_24dp, filterBinding.cbCatList);
        filterBinding.linCatSection.setVisibility(View.GONE);
        setDrawableTextView(R.drawable.ic_keyboard_arrow_down_black_24dp, filterBinding.cbBrandList);
        filterBinding.recycleShowProducts.setVisibility(View.GONE);

        filterBinding.ivCrossGrey.setOnClickListener(this);
        filterBinding.btnSubmit.setOnClickListener(this);
        filterBinding.btnReset.setOnClickListener(this);
        gender= Preferences.getPreferenceValue(nActivity, "genderFilter" );
        category= Preferences.getPreferenceValue(nActivity, "categoryFilter");
        brands= Preferences.getPreferenceValue(nActivity, "brandsFilter");
        rating= Preferences.getPreferenceValue(nActivity, "ratingFilter" );
        discount= Preferences.getPreferenceValue(nActivity, "discountFilter");
        sortBy= Preferences.getPreferenceValue(nActivity, "sortByFilter");
        lowToHigh= Preferences.getPreferenceValue(nActivity, "sortByLowToHigh");
        highToLow= Preferences.getPreferenceValue(nActivity, "sortByHighToLow");
        if(gender.equalsIgnoreCase("NA"))
        {
            gender="";
        }if(category.equalsIgnoreCase("NA"))
        {
            category="";
        }if(brands.equalsIgnoreCase("NA"))
        {
            brands="";
        }if(rating.equalsIgnoreCase("NA"))
        {
            rating="";
        }if(discount.equalsIgnoreCase("NA"))
        {
            discount="";
        }if(lowToHigh.equalsIgnoreCase("NA"))
        {
            lowToHigh="";
        }if(highToLow.equalsIgnoreCase("NA"))
        {
            highToLow="";
        }if(sortBy.equalsIgnoreCase("NA"))
        {
            sortBy="";
        }

        removeAllTicks();
        getBrandCategoryWise();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivCrossGrey) {
            filterBottomSheetComboNOffers.dismiss();

        }
        if (view.getId() == R.id.btnReset) {
            isFilter=false;
            if(isFilter)
            {
                comboAndOffersBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_filled);
            }else
            {
                comboAndOffersBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_icon);
            }
            category="";brands="";rating="";discount="";sortBy="";gender="";lowToHigh="";highToLow="";
            brandsArr=new ArrayList<>();
            selectedCats=new ArrayList<>();
            selectedCatsText=new ArrayList<>();
            Preferences.setPreferenceValue(nActivity, "genderFilter","");
            Preferences.setPreferenceValue(nActivity, "categoryFilter","");
            Preferences.setPreferenceValue(nActivity, "brandsFilter","");
            Preferences.setPreferenceValue(nActivity, "ratingFilter","");
            Preferences.setPreferenceValue(nActivity, "discountFilter","");
            Preferences.setPreferenceValue(nActivity, "sortByFilter","");
            Preferences.setPreferenceValue(nActivity, "sortByLowToHigh","");
            Preferences.setPreferenceValue(nActivity, "sortByHighToLow","");
            filterBottomSheetComboNOffers.dismiss();
            mainServicesAdapter.notifyDataSetChanged();
            /*filterBottomSheetComboNOffers = new FragmentFilterBottomSheetComboNOffers(nActivity);
            filterBottomSheetComboNOffers.show(getActivity().getSupportFragmentManager(), filterBottomSheetComboNOffers.getTag());*/
            if (isNetworkAvailable(nActivity)) {
                getComboListDetails();
                getPromoOffListDetails();
            } else {
                showNoInternetDialog(nActivity);
            }

        }
        if (view.getId() == R.id.btnSubmit) {
            category=TextUtils.join(",",selectedCats);
            brands=TextUtils.join(",",brandsArr);
            if(!category.equalsIgnoreCase("") || !brands.equalsIgnoreCase("") || !rating.equalsIgnoreCase("") || !discount.equalsIgnoreCase("") ||!sortBy.equalsIgnoreCase("") || !gender.equalsIgnoreCase("") ||!lowToHigh.equalsIgnoreCase("")||!highToLow.equalsIgnoreCase(""))
            {
                isFilter=true;
            }else
            {
                isFilter=false;
            }
            if(isFilter)
            {
                comboAndOffersBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_filled);
            }else
            {
                comboAndOffersBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_icon);
            }
            Preferences.setPreferenceValue(nActivity, "genderFilter",gender);
            Preferences.setPreferenceValue(nActivity, "categoryFilter",TextUtils.join(",",selectedCats));
            Preferences.setPreferenceValue(nActivity, "brandsFilter",TextUtils.join(",",brandsArr));
            Preferences.setPreferenceValue(nActivity, "ratingFilter",rating);
            Preferences.setPreferenceValue(nActivity, "discountFilter",discount);
            Preferences.setPreferenceValue(nActivity, "sortByFilter",sortBy);
            Preferences.setPreferenceValue(nActivity, "sortByLowToHigh",lowToHigh);
            Preferences.setPreferenceValue(nActivity, "sortByHighToLow",highToLow);
            mainServicesAdapter.notifyDataSetChanged();
            if (isNetworkAvailable(nActivity)) {
                getComboListDetails();
                getPromoOffListDetails();
            } else {
                showNoInternetDialog(nActivity);
            }

            filterBottomSheetComboNOffers.dismiss();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cbCatList:
                if (b) {
                    setDrawableTextView(R.drawable.ic_keyboard_arrow_up_black_24dp, filterBinding.cbCatList);
                    filterBinding.linCatSection.setVisibility(View.VISIBLE);
                } else {
                    setDrawableTextView(R.drawable.ic_keyboard_arrow_down_black_24dp, filterBinding.cbCatList);
                    filterBinding.linCatSection.setVisibility(View.GONE);
                }
                break;
            case R.id.cbBrandList:
                if (b) {
                    setDrawableTextView(R.drawable.ic_keyboard_arrow_up_black_24dp, filterBinding.cbBrandList);
                    filterBinding.recycleShowProducts.setVisibility(View.VISIBLE);
                } else {
                    setDrawableTextView(R.drawable.ic_keyboard_arrow_down_black_24dp, filterBinding.cbBrandList);
                    filterBinding.recycleShowProducts.setVisibility(View.GONE);
                }
                break;

//Main category// "1": "Hair", "2": "Skin","3": "Nails","4": "Spa", "5": "Makeup"
            case R.id.cbHair:
                if (selectedCats.contains("0"))
                {
                    selectedCats.remove("0");
                    selectedCatsText=new ArrayList<>();
                }
                if (!selectedCats.contains("1")) {
                    setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbHair);
                    selectedCatsText.add("Hair");
                    selectedCats.add("1");
                } else {
                    removeDrawableCheckbox(filterBinding.cbHair);
                    selectedCatsText.remove("Hair");
                    selectedCats.remove("1");
                }
                getBrandCategoryWise();
                break;
            case R.id.cbSkin:
                if (selectedCats.contains("0"))
                {
                    selectedCats.remove("0");
                    selectedCatsText=new ArrayList<>();
                }
                if (!selectedCats.contains("2")) {
                    setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbSkin);
                    selectedCatsText.add("Skin");
                    selectedCats.add("2");
                } else {
                    removeDrawableCheckbox(filterBinding.cbSkin);
                    selectedCats.remove("2");
                    selectedCatsText.remove("Skin");
                }
                getBrandCategoryWise();
                break;
            case R.id.cbNails:
                if (selectedCats.contains("0"))
                {
                    selectedCats.remove("0");
                    selectedCatsText=new ArrayList<>();
                }
                if (!selectedCats.contains("3")) {
                    setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbNails);
                    selectedCats.add("3");
                    selectedCatsText.add("Nails");
                } else {
                    removeDrawableCheckbox(filterBinding.cbNails);
                    selectedCats.remove("3");
                    selectedCatsText.remove("Nails");
                }
                getBrandCategoryWise();
                break;
            case R.id.cbSpa:
                if (selectedCats.contains("0"))
                {
                    selectedCats.remove("0");
                    selectedCatsText=new ArrayList<>();
                }
                if (!selectedCats.contains("4")) {
                    setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbSpa);
                    selectedCats.add("4");
                    selectedCatsText.add("Spa");
                } else {
                    removeDrawableCheckbox(filterBinding.cbSpa);
                    selectedCats.remove("4");
                    selectedCatsText.remove("Spa");
                }
                getBrandCategoryWise();
                break;
            case R.id.cbMakeup:
                if (selectedCats.contains("0"))
                {
                    selectedCats.remove("0");
                    selectedCatsText=new ArrayList<>();
                }
                if (!selectedCats.contains("5")) {
                    setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbMakeup);
                    selectedCats.add("5");
                    selectedCatsText.add("Makeup");
                } else {
                    removeDrawableCheckbox(filterBinding.cbMakeup);
                    selectedCats.remove("5");
                    selectedCatsText.remove("Makeup");
                }
                getBrandCategoryWise();
                break;

            //popularity
            case R.id.cbCostLowToHigh:
                removeAllTicksForSortBy();
                if (!lowToHigh.equalsIgnoreCase("1")) {
                    setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbCostLowToHigh);
                    lowToHigh="1";//filterBinding.cbCostLowToHigh.getText().toString();
                    highToLow="";
                } else {
                    removeDrawableCheckbox(filterBinding.cbCostLowToHigh);
                    lowToHigh="";
                }
                break;
            case R.id.cbCostHighToLow:
                removeAllTicksForSortBy();
                if (!highToLow.equalsIgnoreCase("1")) {
                    setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbCostHighToLow);
                    highToLow="1";//filterBinding.cbCostHighToLow.getText().toString();
                    lowToHigh="";
                } else {
                    removeDrawableCheckbox(filterBinding.cbCostHighToLow);
                    highToLow="";
                }
                break;

        }
    }

    public void removeAllTicksForSortBy() {

        removeDrawableCheckbox(filterBinding.cbCostLowToHigh);//3
        removeDrawableCheckbox(filterBinding.cbCostHighToLow);//4
    }
        public void removeAllTicks() {
            removeDrawableCheckbox(filterBinding.cbCostLowToHigh);
            removeDrawableCheckbox(filterBinding.cbCostHighToLow);
        if(lowToHigh.equalsIgnoreCase("1"))
        {
            setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbCostLowToHigh);
        }else if(highToLow.equalsIgnoreCase("1"))
        {
            setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbCostHighToLow);
        }

        removeDrawableCheckbox(filterBinding.cbHair);
        removeDrawableCheckbox(filterBinding.cbSkin);
        removeDrawableCheckbox(filterBinding.cbSpa);
        removeDrawableCheckbox(filterBinding.cbNails);
        removeDrawableCheckbox(filterBinding.cbMakeup);
            if(category.isEmpty()) {
                selectedCatsText=new ArrayList<>();
                selectedCats=new ArrayList<>();
            }
            if(selectedCatsText.contains("Hair")){
                setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbHair);
            }
            if(selectedCatsText.contains("Skin")){
                setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbSkin);
            }
            if(selectedCatsText.contains("Nails")){
                setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbNails);
            }
            if(selectedCatsText.contains("Spa")){
                setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbSpa);
            }
            if(selectedCatsText.contains("Makeup")){
                setDrawableTextView(R.drawable.ic_check_black_24dp, filterBinding.cbMakeup);
            }

            if(brands.isEmpty()) {
                brandsArr=new ArrayList<>();
            }
        getBrandCategoryWise();
    }

    public void removeDrawableCheckbox(CheckBox checkBox) {
        /*Drawable img = nContext.getResources().getDrawable(id);
        img.setBounds(0, 0, 70, 70);*/
        checkBox.setChecked(false);
        checkBox.setCompoundDrawables(null, null, null, null);
        checkBox.setTextColor(getResources().getColor(R.color.black));
    }

    private void setDrawableTextView(int ic_keyboard_arrow_up_black_24dp, CheckBox tvSalonDetails) {
        Drawable img = getResources().getDrawable(ic_keyboard_arrow_up_black_24dp);
        img.setBounds(0, 0, 60, 50);
        tvSalonDetails.setCompoundDrawables(null, null, img, null);
        tvSalonDetails.setTextColor(getResources().getColor(R.color.skyBlueDark));
    }


    CategoryBrandBean categoryBrandBean;
    private void getBrandCategoryWise() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(nActivity,0);
        progressDialog.show();
        if(selectedCats.contains("0"))
        {
            selectedCats.remove("0");
        }
        service.getBrandCategoryWise(TextUtils.join(",",selectedCats))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryBrandBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CategoryBrandBean loginBeanObj) {
                        progressDialog.dismiss();
                        categoryBrandBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                      //  showFailToast(nActivity, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (categoryBrandBean.getStatus() == 1) {

                            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(nActivity);
                            filterBinding.recycleShowProducts.setLayoutManager(gridLayoutManager);
                            filterBinding.recycleShowProducts.setAdapter(new FilterShowProductsAdapter(nActivity,categoryBrandBean.getResult(), new FilterShowProductsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    brands = categoryBrandBean.getResult().get(position).getBrndId();
                                }
                            }));
                        }else if(categoryBrandBean.getStatus() == 3){
                            logout(nActivity);
                        }else
                        {
                            // showFailToast(nActivity,  "" + updateLocationBean.getMsg());
                        }
                        //setEmptyMsg(notificationsArrayList, notificationsBinding.recycleNotifications, notificationsBinding.emptyMsg.ivNoData);
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }
}
