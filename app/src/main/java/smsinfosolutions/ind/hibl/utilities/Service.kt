package smsinfosolutions.ind.hibl.utilities

/**
 * Created by Manish on 27-Jul-20.
 */

import okhttp3.ResponseBody
import org.healthymantra.piousvision.utilities.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface Service {

    @GET("?act=GET_AREA_LIST")
    fun getArea(): Call<LovDataList>

    @GET("?act=GET_DURATION_LIST")
    fun getDuration(): Call<LovDataList>

    @GET("?act=GET_CATEGORY_LIST")
    fun getCategories(): Call<LovDataList>

    @GET("?act=GET_ANIMAL_LIST")
    fun getAnimalType(): Call<AnimalTypeLovList>

    @GET("?act=GET_CITY_LIST")
    fun getCities(): Call<LovDataList>

    @FormUrlEncoded
    @POST("?act=GET_HOSPITAL_LIST")
    fun getHospitals(@Field("city_id") id: String): Call<LovDataList>


    @POST("?act=GET_BANKTYPE_LIST")
    fun getBankType(): Call<LovDataList>

    @FormUrlEncoded
    @POST("?act=GET_PROPOSAL_LIST")
    fun getProposalNo(
        @Field("id") id: String,
        @Field("user_type") user_type: String
    ): Call<ProposalNoList>


    @FormUrlEncoded
    @POST("?act=CHK_MOBILE")
    fun checkMobile(@Field("mobile_no") mobile_no: String): Call<ResponseBody>


    @FormUrlEncoded
    @POST("?act=VALIDATE_LOGIN")
    fun validateLogin(
        @Field("id") id: String,
        @Field("user_type") user_type: String,
        @Field("mobile_no") mobile_no: String,
        @Field("login_type") login_type: String,
        @Field("password") password: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("?act=UPD_PWD")
    fun updatePassword(
        @Field("id") id: String,
        @Field("user_type") user_type: String,
        @Field("password") password: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("?act=GET_PREMIUM_BREAKUP")
    fun getPremium(
        @Field("proposal_no") proposal_no: String,
        @Field("sa") sa: String,
        @Field("term_id") term_id: String,
        @Field("wptd") wptd: String,
        @Field("area_id") area_id: String,
        @Field("category_id") category_id: String
    ): Call<PremiumDetail>

    @FormUrlEncoded
    @POST("?act=GET_PAYMENT_LIST")
    fun getPaymentList(
        @Field("id") id: String,
        @Field("user_type") user_type: String
    ): Call<PaymentDetailList>

    @FormUrlEncoded
    @POST("?act=SAVE_RECORDS")
    fun saveRecord(
        @Field("user_id") id: String,
        @Field("user_type") user_type: String,
        @Field("records") records: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("?act=UPDATE_BANK_DETAILS")
    fun updatePayment(
        @Field("bank_type") bank_type: String,
        @Field("bank_date") bank_date: String,
        @Field("bank_amt") bank_amt: String,
        @Field("proposal_no") proposal_no: String,
        @Field("bank_img") bank_img: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("?act=GET_COUNT")
    fun getCount(@Field("id") id: String, @Field("user_type") user_type: String): Call<ResponseBody>


    @FormUrlEncoded
    @POST("?act=GET_FORMLIST")
    fun getCompletedList(
        @Field("id") id: String,
        @Field("user_type") user_type: String
    ): Call<CompletedFormList>

    @FormUrlEncoded
    @POST("?act=GET_FORMLISTDETAILS")
    fun getCompletedListDetail(
        @Field("id") id: String,
        @Field("user_type") user_type: String,
        @Field("proposal_no") proposal_no: String
    ): Call<ShowAnimalDetailList>




    @FormUrlEncoded
    @POST("?act=SAVE_CLAIM")
    fun saveClaimRecord(
        @Field("id") id: String,
        @Field("user_type") user_type: String,
        @Field("mobile_no") mobile_no: String,
        @Field("proposal_no") proposal_no: String,
        @Field("tag_no") tag_no: String,
        @Field("death_dt") death_dt: String,
        @Field("city_id") city_id: String,
        @Field("hospital_id") hospital_id: String,
        @Field("claim_for") claim_for: String,
        @Field("customer_mobile") customer_mobile: String,
        @Field("proposal_img") proposal_img: String,
        @Field("bank_img1") bank_img1: String?,
        @Field("bank_img2") bank_img2: String?,
        @Field("animal_img") animal_img: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("?act=GET_CLAIMLIST")
    fun getClaimList(
        @Field("id") id: String,
        @Field("user_type") user_type: String,
        @Field("mobile_no") mobile_no: String): Call<ClaimList>


@FormUrlEncoded
@POST("?act=GET_CLAIMDETAILS")
fun getClaimDetails(
    @Field("id") id: String,
    @Field("user_type") user_type: String,
    @Field("mobile_no") mobile_no: String,
    @Field("tag_no") tag_no: String): Call<ClaimDetailList>
}