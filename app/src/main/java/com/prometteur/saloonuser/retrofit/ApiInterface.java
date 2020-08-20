package com.prometteur.saloonuser.retrofit;

import com.prometteur.saloonuser.Model.AcceptAppBean;
import com.prometteur.saloonuser.Model.AddCartBean;
import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.Model.BookAppointBean;
import com.prometteur.saloonuser.Model.CancelAppBean;
import com.prometteur.saloonuser.Model.CartDetailBean;
import com.prometteur.saloonuser.Model.CategoryBrandBean;
import com.prometteur.saloonuser.Model.CheckBean;
import com.prometteur.saloonuser.Model.CheckPenaltyBean;
import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.Model.CouponBean;
import com.prometteur.saloonuser.Model.ForgotPassBean;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.Model.LoginBean;
import com.prometteur.saloonuser.Model.NotificationBean;
import com.prometteur.saloonuser.Model.OrderIdBean;
import com.prometteur.saloonuser.Model.PointBalanceBean;
import com.prometteur.saloonuser.Model.PromoOfferBean;
import com.prometteur.saloonuser.Model.RegBean;
import com.prometteur.saloonuser.Model.RemoveCartBean;
import com.prometteur.saloonuser.Model.ReviewBean;
import com.prometteur.saloonuser.Model.ReviewDetailsBean;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.Model.SearchBean;
import com.prometteur.saloonuser.Model.ServicesBean;
import com.prometteur.saloonuser.Model.SignoutBean;
import com.prometteur.saloonuser.Model.UpdateLocationBean;
import com.prometteur.saloonuser.Model.UpodatePassBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("mLogin")
    Observable<LoginBean> getLogin(@Field("user_name") String userName,
                                   @Field("user_password") String userPassword,
                                   @Field("user_fcm_key") String userFcmKey);
    @FormUrlEncoded
    @POST("mLogout")
    Observable<SignoutBean> getLogout(@Field("user_id") String userId);
    @FormUrlEncoded
    @POST("mAdd/end_user")
    Observable<RegBean> getRegister(@Field("user_f_name") String userFName,
                                    @Field("user_l_name") String userLName,
                                    @Field("user_mb_no") String userMbNo,
                                    @Field("user_email") String userEmail,
                                    @Field("user_password") String userPassword,
                                    @Field("user_referral_code") String userReferralCode,
                                    @Field("user_gender") String userGender,
                                    @Field("user_fcm_key") String userFcmKey);

    @FormUrlEncoded
    @POST("mForgot")
    Observable<ForgotPassBean> getForgotPass(@Field("user_email") String userEmail,
                                             @Field("user_mb_no") String userMob);
@FormUrlEncoded
    @POST("sendOtp")
    Observable<CheckBean> getSendOtp(@Field("user_mb_no") String userMob);

@FormUrlEncoded
    @POST("fields/verify_user")
    Observable<ForgotPassBean> getVerifyUser(@Field("user_email") String userEmail,
                                             @Field("user_mb_no") String userMob,
                                             @Field("user_referral_code") String userReferralCode);


    @FormUrlEncoded
    @POST("mChangePassword")
    Observable<LoginBean> getChangePass(@Field("user_email") String userEmail,
                                        @Field("user_mb_no") String userMbNo,
                                        @Field("user_password") String userPassword);

    @Multipart
    @POST("mAdd/end_user")//completed survey adding
    Observable<LoginBean> updateUser(@Part("user_id") RequestBody userId,
                                                       @Part("user_f_name") RequestBody userFName,
                                                       @Part("user_l_name") RequestBody userLName,
                                                       @Part("user_mb_no") RequestBody userMbNo,
                                                       @Part("user_email") RequestBody userEmail,
                                                       @Part("user_gender") RequestBody userGender,
                                                       @Part MultipartBody.Part[] otherFields54
    );

    @FormUrlEncoded
    @POST("fields/home")
    Observable<HomeBean> getHomeData(@Field("user_lat") String userLat,
                                     @Field("user_long") String userLong,
                                     @Field("category") String category,
                                     @Field("brands") String brands,
                                     @Field("rating") String rating,
                                     @Field("discount") String discount,
                                     @Field("sort_by") String sortBy,
                                     @Field("pincode") String pinCode,
                                     @Field("gender") String gender,
                                     @Field("low_to_high") String lowToHigh,
                                     @Field("high_to_low") String highToLow
                                     );

    @FormUrlEncoded
    @POST("fields/combo_offer")
    Observable<ComboOffBean> getComboListDetails(@Field("branch_id") String branchId,
                                                 @Field("category_id") String categoryId,
                                                 @Field("low_to_high") String lowToHigh,
                                                 @Field("high_to_low") String highToLow,
                                                 @Field("brand_id") String brandId);

    @FormUrlEncoded
    @POST("fields/promotional_offer")
    Observable<PromoOfferBean> getPromoOfferDetails(@Field("branch_id") String branchId,
                                                    @Field("category_id") String categoryId,
                                                    @Field("low_to_high") String lowToHigh,
                                                    @Field("high_to_low") String highToLow,
                                                    @Field("brand_id") String brandId);

    @FormUrlEncoded
    @POST("fields/salon_details")
    Observable<SalonDetailBean> getSalonDetails(@Field("branch_id") String branchId);

    @FormUrlEncoded
    @POST("fields/get_coupons")
    Observable<CouponBean> getCoupons(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("mAdd/apply_coupon")
    Observable<CouponBean> getApplyCoupon(
            @Field("uscop_user_id") String userId,
            @Field("uscop_coupon_id") String couponId
                                          );

    @FormUrlEncoded
    @POST("fields/check_coupon")
    Observable<CouponBean> getValidateCoupon(
            @Field("user_id") String userId,
            @Field("coupon_code") String couponCode,
            @Field("amount") String aptAmount
    );

    @FormUrlEncoded
    @POST("fields/search")
    Observable<SearchBean> getSearchResult(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("mAdd/cart")
    Observable<AddCartBean> getAddCart(
            @Field("cart_user_id") String cartUserId,
            @Field("cart_type") String cartType,
            @Field("cart_branch_id") String cartBranchId,
            @Field("cart_service_id") String cartServiceId,
            @Field("cart_services") String cartServices

    );

    @FormUrlEncoded
    @POST("fields/cart_details")
    Observable<CartDetailBean> getCartDetails(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("fields/appointments")
    Observable<AppointmentBean> getAppointments(
            @Field("user_id") String userId,
            @Field("user_lat") String userLat,
            @Field("user_long") String userLong
    );

    @FormUrlEncoded
    @POST("fields/appointments")
    Observable<AppointmentBean> getAppointments(
            @Field("user_id") String userId,
            @Field("apt_status") String aptStatus,
            @Field("user_lat") String userLat,
            @Field("user_long") String userLong
    );

    @FormUrlEncoded
    @POST("fields/appointment_details")
    Observable<AppointDetailBean> getAppointmentDetail(
            @Field("apt_id") String aptId,
            @Field("user_lat") String userLat,
            @Field("user_long") String userLong,
            @Field("user_noti_id") String userNotiId
    );

    @FormUrlEncoded
    @POST("mAdd/cancel_appointment")
    Observable<CancelAppBean> getAppointmentCancel(
            @Field("apt_id") String aptId,
            @Field("apt_status") String aptStatus,
            @Field("apt_reject_reason") String aptRejectReason,
            @Field("penality") String penalityAmt
    );

    @FormUrlEncoded
    @POST("fields/get_review_details")
    Observable<ReviewDetailsBean> getReviewDetails(
            @Field("apt_id") String aptId
    );

    @FormUrlEncoded
    @POST("mAdd/add_review")
    Observable<ReviewBean> setReviews(@Field("rev_branch_id") String revBranchId,
                                      @Field("rev_rating") String rating,
                                      @Field("rev_review") String review,
                                      @Field("rev_user_id") String userId,
                                      @Field("rev_apt_id") String aptId,
                                      @Field("operator_review") String operatorReview//{"rev_branch_id":"2","rev_operator_id":"4","rev_user_id":"6","rev_service_id":"6","rev_rating":"3.5"}
    );

    @FormUrlEncoded
    @POST("mAdd/update_password")
    Observable<UpodatePassBean> getUpdatePassword(@Field("user_id") String userId,
                                                  @Field("user_current_password") String userCurrentPassword,
                                                  @Field("user_password") String userPassword
    );

    @FormUrlEncoded
    @POST("mAdd/update_user_location")
    Observable<UpdateLocationBean> getUpdateLocation(@Field("user_id") String userId,
                                                     @Field("user_lat") String userLat,
                                                     @Field("user_lon") String userLong,
                                                     @Field("user_address") String userAddress
    );

    @FormUrlEncoded
    @POST("fields/get_services")
    Observable<ServicesBean> getServices(@Field("branch_id") String branchId,
                                         @Field("category_id") String categoryId,
                                         @Field("low_to_high") String lowToHigh,
                                         @Field("high_to_low") String highToLow,
                                         @Field("brand_id") String brandId
    );

    @FormUrlEncoded
    @POST("del/cart")
    Observable<RemoveCartBean> getRemoveCart(@Field("delete") String cartId);


    @FormUrlEncoded
    @POST("mAdd/book_appointment")
    Observable<BookAppointBean> getBookAppoint(
            @Field("apt_cp_branch") String aptCpBranch,
            @Field("apt_user_id") String aptUserId,
            @Field("apt_date") String aptDate,
            @Field("apt_time") String aptTime,
            @Field("apt_gst") String aptGst,
            @Field("apt_subtotal") String aptSubtotal,
            @Field("apt_amount") String aptAmount,
            @Field("apt_payment_type") String aptPaymentType,
            @Field("apt_coupon_id") String aptCouponId,
            @Field("apt_services") String aptServices,
            @Field("apt_penality") String aptPenality,
            @Field("apt_redeem_point") String aptRedeemPoint,
            @Field("apt_service_charges") String aptServiceCharges,
            @Field("apt_coupon_price") String apt_coupon_price
    );

    @FormUrlEncoded
    @POST("razorpay/transfer_order.php")
    Observable<OrderIdBean> getOrderId(
            @Field("currency") String currency,
            @Field("amount") int amount,
            @Field("payment_capture") int paymentCapture,


            @Field("taccount") String taccount,
            @Field("tcurrency") String tcurrency,
            @Field("tamount") int tamount

           /* @Field("t1account") String t1account,
            @Field("t1currency") String t1currency,
            @Field("t1amount") int t1amount*/
    );

    @FormUrlEncoded
    @POST("fields/notification")
    Observable<NotificationBean> getNotifications(@Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("fields/category_brand")
    Observable<CategoryBrandBean> getBrandCategoryWise(@Field("brnd_category") String brndCategory);

    @FormUrlEncoded
    @POST("mAdd/appointments")
    Observable<AcceptAppBean> getAcceptAppointment(
            @Field("apt_id") String aptId,
            @Field("apt_status") String aptStatus
    );

    @FormUrlEncoded
    @POST("del/clear_cart")
    Observable<RemoveCartBean> getRemoveAllCart(@Field("delete") String userId);

    @FormUrlEncoded
    @POST("fields/check_penality")
    Observable<CheckPenaltyBean> getCheckPenalty(@Field("apt_id") String aptId
    );

    @FormUrlEncoded
    @POST("mAdd/appointments")
    Observable<AcceptAppBean> getUnpaidToPaid(
            @Field("apt_id") String aptId,
            @Field("apt_payment_status") String aptStatus,
            @Field("apt_transaction_id") String aptTransactionId,
            @Field("apt_order_id") String aptOrderId,
            @Field("reward_point") String rewardPoint,
            @Field("apt_payment_type") String aptPaymentType
    );

    @POST("fields/point_balance")
    Observable<PointBalanceBean> getPointBalance();

    @FormUrlEncoded
    @POST("fields/branch_working_details")
    Observable<SalonDetailBean> getBranchWorkingDetails(@Field("branch_id") String branchId);
}