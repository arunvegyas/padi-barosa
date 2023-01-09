package com.hiddencoders.cattleinsurance.data.remote

import com.google.gson.JsonObject
import com.hiddencoders.cattleinsurance.data.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface ApiServices {
    @GET()
    fun verifyUserByMobileNumber(@Url url: String): Observable<VerifyUserModel>

    @POST("login/")
    fun verifyLogin(@Body jsonObject: JsonObject): Observable<LoginUserModel>

    @GET("villages")
    fun getVillages(): Single<VillagesModel>

    @GET("farmers")
    fun getFarmersDetails(@Query("ccode") ccode: Int): Single<FarmersModel>

    @GET("banks")
    fun getBankDetails(): Single<BanksModel>

    @GET("tags")
    fun getTagData(): Single<TagsModel>

    @GET("bankbranches")
    fun getBranchDetails(@Query("bkname") bkname: String): Single<BranchModel>

    @GET("centers")
    fun getCenterDetails(@Query("villid") villid: Int): Single<CentersModel>

    @POST("newtag")
    fun newFormToServer(@Body jsonObject: JsonObject): Observable<CommonResponse>

    @POST("newfarmer")
    fun addFarmerToServer(@Body jsonObject: JsonObject): Single<CommonResponse>

    @GET("centerfarmers")
    fun getFarmersListDetails(
        @Query("ccode") ccode: Int,
        @Query("name") name: String
    ): Single<FarmersListModel>

}