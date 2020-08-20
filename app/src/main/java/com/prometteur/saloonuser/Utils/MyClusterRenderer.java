package com.prometteur.saloonuser.Utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.prometteur.saloonuser.Model.MapClusterItem;
import com.prometteur.saloonuser.R;

public class MyClusterRenderer extends DefaultClusterRenderer<MapClusterItem> {

    public MyClusterRenderer(Context context, GoogleMap map,
                             ClusterManager<MapClusterItem> clusterManager) {
        super(context, map, clusterManager);

    }

    @Override
    protected void onBeforeClusterItemRendered(MapClusterItem item,
                                               MarkerOptions markerOptions) {
        markerOptions.title(item.getTitle());
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_salon_location_pin_icon);

        markerOptions.icon(icon);
    }


}