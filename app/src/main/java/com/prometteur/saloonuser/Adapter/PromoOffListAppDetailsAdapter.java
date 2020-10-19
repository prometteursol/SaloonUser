package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.CartDetailBean;
import com.prometteur.saloonuser.Model.RemoveCartBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.dateTimeOneTime;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.globalCartCount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strAppDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strTime;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class PromoOffListAppDetailsAdapter extends RecyclerView.Adapter<PromoOffListAppDetailsAdapter.ViewHolder> {

    AppCompatActivity nActivity;

    boolean bottomlist;
    List<AppointDetailBean.PromotionalService> results;

    public PromoOffListAppDetailsAdapter(AppCompatActivity nActivity, List<AppointDetailBean.PromotionalService> results, boolean bottomlist) {
        this.nActivity = nActivity;
        this.results=results;
        this.bottomlist=bottomlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_details_combo_list,
                parent, false);

        return new PromoOffListAppDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            holder.tvComboName.setText(results.get(position).getProofferName());
            if(results.get(position).getProofferDiscountPrice()!=null || !results.get(position).getProofferDiscountPrice().isEmpty()) {
                holder.tvComboOfferPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(results.get(position).getProofferDiscountPrice())));
            }
            if(results.get(position).getProofferPrice()!=null || !results.get(position).getProofferPrice().isEmpty()) {
                holder.tvComboprice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(results.get(position).getProofferPrice())));
            }
        holder.tvComboOfferPrice.setVisibility(View.GONE);
        holder.tvComboprice.setVisibility(View.GONE);
            holder.recycleComboServicesOffers.setLayoutManager(new LinearLayoutManager(nActivity));
            holder.recycleComboServicesOffers.setAdapter(new PromoOffAppDetailsServicesAdapter(nActivity,results.get(position) ,results.get(position).getServices(), false));



            getStrikeString(holder.tvComboprice);



      //  getResizedDrawable(nActivity,R.drawable.ic_clock_blue,holder.tvTime,null,null,R.dimen._12sdp);


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextViewCustomFont tvComboName;
        TextViewCustomFont tvComboOfferPrice;
        TextViewCustomFont tvComboprice;


        RecyclerView recycleComboServicesOffers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvComboName = itemView.findViewById(R.id.tvComboName);
            tvComboOfferPrice = itemView.findViewById(R.id.tvComboOfferPrice);

            tvComboprice = itemView.findViewById(R.id.tvComboprice);
            recycleComboServicesOffers = itemView.findViewById(R.id.recycleComboServicesOffers);
            recycleComboServicesOffers = itemView.findViewById(R.id.recycleComboServicesOffers);
        }
    }

}
