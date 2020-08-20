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

public class ComboListAppDetailsAdapter extends RecyclerView.Adapter<ComboListAppDetailsAdapter.ViewHolder> {

    AppCompatActivity nActivity;

    boolean bottomlist;
    List<AppointDetailBean.ComboService> results;

    public ComboListAppDetailsAdapter(AppCompatActivity nActivity, List<AppointDetailBean.ComboService> results, boolean bottomlist) {
        this.nActivity = nActivity;
        this.results=results;
        this.bottomlist=bottomlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_details_combo_list,
                parent, false);

        return new ComboListAppDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            holder.tvComboName.setText(results.get(position).getOfferName());
            if(results.get(position).getOfferDiscountPrice()!=null || !results.get(position).getOfferDiscountPrice().isEmpty()) {
                holder.tvComboOfferPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(results.get(position).getOfferDiscountPrice())));
            }
            if(results.get(position).getOfferPrice()!=null || !results.get(position).getOfferPrice().isEmpty()) {
                holder.tvComboprice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(results.get(position).getOfferPrice())));
            }

            holder.recycleComboServicesOffers.setLayoutManager(new LinearLayoutManager(nActivity));
            holder.recycleComboServicesOffers.setAdapter(new ComboAppDetailsServicesAdapter(nActivity, results.get(position).getServices(), false));


/*holder.ivRemoveCombo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        getRemoveCart(position,results.get(position).getCartId());   //remove from cart
    }
});*/

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
