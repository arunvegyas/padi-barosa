package com.hiddencoders.cattleinsurance.ui.utilis.sharedPreferences

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.hiddencoders.cattleinsurance.ui.login.SplashScreenActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserSession @Inject constructor(
    @ApplicationContext _context: Context,
) {

    // Shared Preferences reference
    private val pref: SharedPreferences

    // Editor reference for Shared preferences
    private val editor: SharedPreferences.Editor

    // Shared preferences mode
    private var PRIVATE_MODE = 0


    /*Save Token*/

    /*Get APP Token*/
    fun setAccessToken(value: String) {
        editor.putString(SET_TOKEN, value)
        editor.apply()
    }

    fun delete(key: String) {
        if (pref.contains(key)) {
            pref.edit().remove(key).apply()
        }
    }

    fun getAccessToken(): String {
        return pref.getString(SET_TOKEN, "")!!
    }

    /*Remove Fcm Token*/
    fun removeAppToken() {
        editor.remove(SET_TOKEN)
        editor.apply()
    }


    fun getUsername(): String {
        return pref.getString(USERNAME, "")!!
    }

    fun setUsername(result: String?) {
        editor.putString(USERNAME, result)
        editor.commit()
    }

    fun setLocation(result: String?) {
        editor.putString(LOCATION, result)
        editor.commit()
    }


    fun getLocation(): String? {
        return pref.getString(LOCATION, "")
    }

    /*Remove Fcm Token*/

    fun setQbUserId(id: String) {
        editor.putString(QB_USERID, id)
        editor.apply()
    }


    fun save(key: String, value: Any?) {
        val editor = pref.edit()
        when {
            value is Boolean -> editor.putBoolean(key, (value))
            value is Int -> editor.putInt(key, (value))
            value is Float -> editor.putFloat(key, (value))
            value is Long -> editor.putLong(key, (value))
            value is String -> editor.putString(key, value)
            value is Enum<*> -> editor.putString(key, value.toString())
            value != null -> throw RuntimeException("Attempting to save non-supported preference")
        }
        editor.apply()
    }

    fun logout(mContext: Context, activity: Activity) {
        editor.clear()
        editor.apply()
        val intent = Intent(
            mContext,
            SplashScreenActivity::class.java
        )
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK

        mContext.startActivity(intent)
        activity.finish()
    }

//    fun saveQbUser(qbUser: QBUser) {
//        save(QB_USER_ID, qbUser.id)
//        save(QB_USER_LOGIN, qbUser.login)
//        save(QB_USER_PASSWORD, qbUser.password)
//        save(QB_USER_FULL_NAME, qbUser.fullName)
//        save(QB_USER_TAGS, qbUser.tags.itemsAsString)
//    }

    fun getQbUserId(): String? {
        return pref.getString(QB_USERID, "")
    }

    fun setUserId(id: Int) {
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    fun getUserId(): Int {
        return pref.getInt(USER_ID, 0)!!
    }

    fun removeUserId() {
        editor.remove(USER_ID)
        editor.apply()
    }

    fun setDetailsHotSpot(value: String) {
        editor.putString(DETAIL_HOTSPOT, value)
        editor.apply()
    }

    fun getDetailsHotSpot(): String {
        return pref.getString(DETAIL_HOTSPOT, "")!!
    }

    /*Remove Fcm Token*/
    fun removeDetailsHotSpot() {
        editor.remove(DETAIL_HOTSPOT)
        editor.apply()
    }


    /*Genre Saving*/
    fun setAuthenticationType(value: String) {
        editor.putString(AUTH_TYPE, value)
        editor.apply()
    }

    fun getAuthenticationType(): String {
        return pref.getString(AUTH_TYPE, "")!!
    }

    fun removeAuthenticationType() {
        editor.remove(AUTH_TYPE)
        editor.apply()
    }


    init {
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }


    /*Save PhoneNumber*/
    fun setProfileReady(value: String) {
        editor.putString(PROFILE_READY, value)
        editor.apply()
    }

    fun getProfileReady(): String {
        return pref.getString(PROFILE_READY, "")!!
    }

    /*Vehicle ID*/
    fun removeProfileReady() {
        editor.remove(PROFILE_READY)
        editor.apply()
    }


    fun setGoogleLogin(value: String) {
        editor.putString(GOOGLE_LOGIN, value)
        editor.apply()
    }

    fun getGoogleLogin(): String {
        return pref.getString(GOOGLE_LOGIN, "")!!
    }

    fun clearAllData() {
        pref.edit().clear().apply()
    }

    fun hasQbUser(): Boolean {
        return has(QB_USER_LOGIN) && has(QB_USER_PASSWORD)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String): T {
        return pref.all[key] as T
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String, defValue: T): T {
        val returnValue = pref.all[key] as T
        return returnValue ?: defValue
    }

    private fun has(key: String): Boolean {
        return pref.contains(key)
    }


//    fun getQbUser(): QBUser {
//        val id = get<Int>(QB_USER_ID)
//        val login = get<String>(QB_USER_LOGIN)
//        val password = get<String>(QB_USER_PASSWORD)
//        val fullName = get<String>(QB_USER_FULL_NAME)
//        val tagsInString = get<String>(QB_USER_TAGS)
//
//        val tags: StringifyArrayList<String> = StringifyArrayList()
//
//        if (!TextUtils.isEmpty(tagsInString)) {
//            tags.add(*tagsInString.split(",".toRegex()).dropLastWhile { it.isEmpty() }
//                .toTypedArray())
//        }
//
//        val user = QBUser(login, password)
//        user.id = id
//        user.fullName = fullName
//        user.tags = tags
//        return user
//    }

    fun removeGoogleLogin() {
        editor.remove(GOOGLE_LOGIN)
        editor.apply()
    }

    /*Save Google Login*/
    fun setGoogleData(value: String) {
        editor.putString(GOOGLE_DATA, value)
        editor.apply()
    }

    fun getGoogleData(): String {
        return pref.getString(GOOGLE_DATA, "")!!
    }


    fun removeGoogleData() {
        editor.remove(GOOGLE_DATA)
        editor.apply()
    }


    /*Save Facebook Login*/
    fun setProfilePath(value: String) {
        editor.putString(PROFILE_PATH, value)
        editor.apply()
    }

    fun getProfilePath(): String {
        return pref.getString(PROFILE_PATH, "")!!
    }


    fun removeProfilePath() {
        editor.remove(PROFILE_PATH)
        editor.apply()
    }


    /*Open Task DMS*/
    fun setGetStarted(value: Boolean) {
        editor.putBoolean(GET_STARTED, value)
        editor.apply()
    }

    fun getGetStarted(): Boolean {
        return pref.getBoolean(GET_STARTED, true)
    }

    fun removeGetStarted() {
        editor.remove(GET_STARTED)
        editor.apply()
    }

    /*LOGIN Number*/
    fun setLifestylevalue(value: String) {
        editor.putString(LIFESTYLE_OPTION, value)
        editor.apply()
    }


    fun getLifestylevalue(): String {
        return pref.getString(LIFESTYLE_OPTION, "")!!
    }

    fun removesetLifestylevalue() {
        editor.remove(LIFESTYLE_OPTION)
        editor.apply()
    }

    /*IMDB_Search*/
    fun setimdbSearch(value: String) {
        editor.putString(IMDB_SEARCH, value)
        editor.apply()
    }


    fun getimdbSearch(): String {
        return pref.getString(IMDB_SEARCH, "")!!
    }

    fun removeimdbSearch() {
        editor.remove(IMDB_SEARCH)
        editor.apply()
    }

    /*Get SPotify Token*/
    fun setSpotifyToken(value: String) {
        editor.putString(SPOTIFY_TOKEN, value)
        editor.apply()
    }

    fun getSpotifyToken(): String {
        return pref.getString(SPOTIFY_TOKEN, "")!!
    }

    /*Remove Fcm Token*/
    fun removeSpotifyToken() {
        editor.remove(SPOTIFY_TOKEN)
        editor.apply()
    }

    fun setSpotifyLogout(value: String) {
        editor.putString(SPOTIFY_LOGOUT, value)
        editor.apply()
    }

    fun getSpotifyLogout(): String {
        return pref.getString(SPOTIFY_LOGOUT, "")!!
    }

    /*Remove Fcm Token*/
    fun removeSpotifyLogout() {
        editor.remove(SPOTIFY_LOGOUT)
        editor.apply()
    }

    /*Save List*/
    fun setArrayList(value: String) {
        editor.putString(SAVELIST, value)
        editor.apply()
    }

    fun getArrayList(): String {
        return pref.getString(SAVELIST, "")!!
    }

    fun removeArrayList() {
        editor.remove(SAVELIST)
        editor.apply()
    }

    /*Save search_CLick*/
    fun setSearchClick(value: String) {
        editor.putString(SEARCH_CLICK, value)
        editor.apply()
    }

    fun getSearchClick(): String {
        return pref.getString(SEARCH_CLICK, "")!!
    }

    fun removeSearchClick() {
        editor.remove(SEARCH_CLICK)
        editor.apply()
    }

    fun setDeviceId(value: String) {
        editor.putString(DEVICE_ID, value)
        editor.apply()
    }

    fun getDeviceId(): String {
        return pref.getString(DEVICE_ID, "")!!
    }

    /*Save Profile_Edit*/
    fun setOccupationClick(value: String) {
        editor.putString(OCCUPATION_CLICK, value)
        editor.apply()
    }

    fun getOccupationClick(): String {
        return pref.getString(OCCUPATION_CLICK, "")!!
    }

    fun removeOccupationClick() {
        editor.remove(OCCUPATION_CLICK)
        editor.apply()
    }

    fun setLifestyleClick(value: String) {
        editor.putString(LIFESTYLE_CLICK, value)
        editor.apply()
    }

    fun getLifestyleClick(): String {
        return pref.getString(LIFESTYLE_CLICK, "")!!
    }

    fun removeLifestyleClick() {
        editor.remove(LIFESTYLE_CLICK)
        editor.apply()
    }

    /*Get SPotify Token*/
    fun setSpotifyData(value: String) {
        editor.putString(SPOTIFY_DATA, value)
        editor.apply()
    }

    fun getSpotifyData(): String {
        return pref.getString(SPOTIFY_DATA, "")!!
    }

    /*Remove Fcm Token*/
    fun removeSpotifyData() {
        editor.remove(SPOTIFY_DATA)
        editor.apply()
    }

    /*Save Profile_Edit*/
    fun setProfileData(value: String) {
        editor.putString(PROFILE_DATA, value)
        editor.apply()
    }

    fun getProfileData(): String {
        return pref.getString(PROFILE_DATA, "")!!
    }

    fun removeProfileData() {
        editor.remove(PROFILE_DATA)
        editor.apply()
    }

    /*Save Profile_Edit*/
    fun setProfileDefault(value: String) {
        editor.putString(PROFILE_DEFAULT, value)
        editor.apply()
    }

    fun getProfileDefault(): String {
        return pref.getString(PROFILE_DEFAULT, "")!!
    }

    fun removeProfileDefault() {
        editor.remove(PROFILE_DEFAULT)
        editor.apply()
    }

    /*Save Scan Status*/
    fun isScanned(): Boolean {
        return pref.getBoolean(SCAN_STATUS, false)
    }

    fun setScanStatus(value: Boolean) {
        editor.putBoolean(SCAN_STATUS, value)
        editor.apply()
    }

    fun removeScanStatus() {
        editor.remove(SCAN_STATUS)
        editor.apply()
    }

    /*Save Profile_Photo TO redirect*/
    fun setProfilePicRedirect(value: String) {
        editor.putString(PROFILE_REDIRECT_PIC, value)
        editor.apply()
    }

    fun getProfilePicRedirect(): String {
        return pref.getString(PROFILE_REDIRECT_PIC, "")!!
    }

    fun removeProfilePicRedirect() {
        editor.remove(PROFILE_REDIRECT_PIC)
        editor.apply()
    }

    /*Save LifestyleProfile_Date TO redirect*/
    fun setLifestyleRedirect(value: String) {
        editor.putString(LIFESTYLE_REDIRECT, value)
        editor.apply()
    }

    fun getLifestyleRedirect(): String {
        return pref.getString(LIFESTYLE_REDIRECT, "")!!
    }

    fun removeLifestyleRedirect() {
        editor.remove(LIFESTYLE_REDIRECT)
        editor.apply()
    }

    /*Save LifestyleRedirectEdit */
    fun setprofile_lifestyle(value: String) {
        editor.putString(PROFILE_LIFESTYLE, value)
        editor.apply()
    }

    fun getprofile_lifestyle(): String {
        return pref.getString(PROFILE_LIFESTYLE, "")!!
    }

    fun removeprofile_lifestyle() {
        editor.remove(PROFILE_LIFESTYLE)
        editor.apply()
    }

    fun setCall_Log_ID(value: String) {
        editor.putString(CALL_LOG_ID, value)
        editor.apply()
    }

    fun getCallLogID(): String {
        return pref.getString(CALL_LOG_ID, "")!!
    }

    fun setCallTimer(value: String) {
        editor.putString(CALL_TIMER, value)
        editor.apply()
    }

    fun getCallTimer(): String {
        return pref.getString(CALL_TIMER, "")!!
    }


    /*Save LifestyleRedirectEdit */
    fun setFilterData(value: String) {
        editor.putString(FILTER_DATA, value)
        editor.apply()
    }

    fun getFilterData(): String {
        return pref.getString(FILTER_DATA, "")!!
    }

    fun removeFilterData() {
        editor.remove(FILTER_DATA)
        editor.apply()
    }

    /*Save FilterData */
    fun setSaveFilter(value: String) {
        editor.putString(FILTER_SAVE_DATA, value)
        editor.apply()
    }

    fun getSaveFilter(): String {
        return pref.getString(FILTER_SAVE_DATA, "")!!
    }

    fun removeSaveFilter() {
        editor.remove(FILTER_SAVE_DATA)
        editor.apply()
    }

    /*Save FilterData */
    fun setWhocanI(value: String) {
        editor.putString(WHOCANI_DATA, value)
        editor.apply()
    }

    fun getWhocanI(): String {
        return pref.getString(WHOCANI_DATA, "")!!
    }

    fun removeWhocanI() {
        editor.remove(WHOCANI_DATA)
        editor.apply()
    }


    /*Save GifData */
    fun setgifdata(value: String) {
        editor.putString(GIF_DATA, value)
        editor.apply()
    }

    fun getgifdata(): String {
        return pref.getString(GIF_DATA, "")!!
    }

    fun removegifdata() {
        editor.remove(GIF_DATA)
        editor.apply()
    }

    fun getProfanityData(): String {
        return pref.getString(PROFANITY_LIST, "")!!
    }

    fun saveProfanityList(value: String) {
        editor.putString(PROFANITY_LIST, value)
        editor.apply()
    }

    fun getQuickTemplates(): String {
        return pref.getString(QUICK_TEMPLATE, "")!!
    }

    fun saveQuickTemplates(value: String) {
        editor.putString(QUICK_TEMPLATE, value)
        editor.apply()
    }

    /*detail_filter data*/
    /*Save LifestyleRedirectEdit */
    fun setDetailsFilterData(value: String) {
        editor.putString(DETAILS_FILTER_DATA, value)
        editor.apply()
    }

    fun getDetailsFilterData(): String {
        return pref.getString(DETAILS_FILTER_DATA, "")!!
    }

    fun removeDetailsFilterData() {
        editor.remove(DETAILS_FILTER_DATA)
        editor.apply()
    }

    fun saveMap(inputMap: Map<String, String>) {

        val jsonObject = JSONObject(inputMap)
        val jsonString = jsonObject.toString()
        editor.remove(CURRENT_SESSION_DATA).commit()
        editor.putString(CURRENT_SESSION_DATA, jsonString)
        editor.commit()
    }

    fun loadMap(): Map<String, String> {
        val outputMap: MutableMap<String, String> = HashMap()

        try {
            if (pref != null) {
                val jsonString = pref.getString(CURRENT_SESSION_DATA, JSONObject().toString())
                val jsonObject = JSONObject(jsonString)
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    val value = jsonObject[key] as String
                    outputMap[key] = value
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outputMap
    }

    /*Grid Redirectt*/
    fun setGridRedirect(value: String) {
        editor.putString(GRID_REDIRECT, value)
        editor.apply()
    }

    fun getGridRedirect(): String {
        return pref.getString(GRID_REDIRECT, "")!!
    }

    fun removeGridRedirect() {
        editor.remove(GRID_REDIRECT)
        editor.apply()
    }

    /*add_deleteLifsty*/
    fun setClickLifestyleData(value: String) {
        editor.putString(LIFESTYLE_DATACLICK, value)
        editor.apply()
    }

    fun getClickLifestyleData(): String {
        return pref.getString(LIFESTYLE_DATACLICK, "")!!
    }

    /*Remove Fcm Token*/
    fun removeClickLifestyleData() {
        editor.remove(LIFESTYLE_DATACLICK)
        editor.apply()
    }

    fun setLifest_save(value: JSONObject) {
        editor.putString(LIFESTYLE_SAVE, value.toString())
        editor.apply()
    }

    fun getLifest_save(): String {
        return pref.getString(LIFESTYLE_SAVE, "")!!
    }

    fun removeLifest_save() {
        editor.remove(LIFESTYLE_SAVE)
        editor.apply()
    }

//    /*Save LifestyleRedirectEdit */
//    fun setIndustryItem(value: String) {
//        editor.putString(INDUSTRY_DATA, value)
//        editor.apply()
//    }
//
//    fun getIndustryItem(): String {
//        return pref.getString(INDUSTRY_DATA, "")!!
//    }
//
//    fun removeIndustryItem() {
//        editor.remove(INDUSTRY_DATA)
//        editor.apply()
//    }

    /*Save LifestyleRedirectEdit */
    fun setIndu_filterdata(value: String) {
        editor.putString(INFUS_FIl_DATA, value)
        editor.apply()
    }

    fun getIndu_filterdata(): String {
        return pref.getString(INFUS_FIl_DATA, "")!!
    }

    fun removeIndu_filterdata() {
        editor.remove(INFUS_FIl_DATA)
        editor.apply()
    }

    /*Save LifestyleRedirectEdit */
    fun setselectedID(value: String) {
        editor.putString(INDUS_SELCTED_ID, value)
        editor.apply()
    }

    fun getselectedID(): String {
        return pref.getString(INDUS_SELCTED_ID, "")!!
    }

    fun removeselectedID() {
        editor.remove(INDUS_SELCTED_ID)
        editor.apply()
    }


    /*Save FIlteredData*/
    fun setSaveFIlterSettings(value: String) {
        editor.putString(FILTER_SAVE, value)
        editor.apply()
    }

    fun getSaveFIlterSettings(): String {
        return pref.getString(FILTER_SAVE, "")!!
    }

    fun removeSaveFIlterSettings() {
        editor.remove(FILTER_SAVE)
        editor.apply()
    }


    fun saveEId(value: String) {
        editor.putString(EID, value)
        editor.apply()
    }

    fun getEid(): String {
        return pref.getString(EID, "")!!
    }

    /*User Profile_Type*/
    fun setUserProfileType(value: String) {
        editor.putString(USER_PROFILE_TYPE, value)
        editor.apply()
    }

    fun getUserProfileType(): String {
        return pref.getString(USER_PROFILE_TYPE, "")!!
    }

    fun removeUserProfileType() {
        editor.remove(USER_PROFILE_TYPE)
        editor.apply()
    }

    /*User Profile_Type*/
    fun setUniqueID(value: String) {
        editor.putString(UNIQUE_ID, value)
        editor.apply()
    }

    fun getUniqueID(): String {
        return pref.getString(UNIQUE_ID, "")!!
    }

    fun removeUniqueID() {
        editor.remove(UNIQUE_ID)
        editor.apply()
    }

    /*Industry Type*/
    fun setIndustry_type(value: String) {
        editor.putString(INDUSTRY_TYPE, value)
        editor.apply()
    }

    fun getIndustry_type(): String {
        return pref.getString(INDUSTRY_TYPE, "")!!
    }

    fun removeIndustry_type() {
        editor.remove(INDUSTRY_TYPE)
        editor.apply()
    }

    /*Entity Id*/
    fun setEntityId(value: String) {
        editor.putString(ENTITY_ID, value)
        editor.apply()
    }

    fun getEntityId(): String {
        return pref.getString(ENTITY_ID, "")!!
    }

    fun removeEntityId() {
        editor.remove(ENTITY_ID)
        editor.apply()
    }

    /*Spotify Click*/
    fun setGridClick(value: Boolean) {
        editor.putBoolean(SPOTIFY_CLICK, value)
        editor.apply()
    }

    fun getGridClick(): Boolean {
        return pref.getBoolean(SPOTIFY_CLICK, false)
    }

    /*Remove Fcm Token*/
    fun removeSpotifyClick() {
        editor.remove(SPOTIFY_CLICK)
        editor.apply()
    }


    /*Save filterIndustry */
    fun setFilterIndustry(value: List<String>) {
        editor.putString(FILTER_INDUSTRY, value.toString())
        editor.apply()
    }

    fun getFilterIndustry(): String {
        return pref.getString(FILTER_INDUSTRY, "")!!
    }

    fun removeFilterIndustry() {
        editor.remove(FILTER_INDUSTRY)
        editor.apply()
    }

    /*Save scan location*/
    fun setScanLocation(value: String) {
        editor.putString(SCAN_LOCATION, value)
        editor.apply()
    }

    fun getScanLocation(): String {
        return pref.getString(SCAN_LOCATION, "")!!
    }

    fun removeScanLocation() {
        editor.remove(SCAN_LOCATION)
        editor.apply()
    }

    /* Save scanned time*/
    fun setScannedTime(value: Long) {
        editor.putLong(SCANNED_TIME, value)
        editor.apply()
    }

    fun getScannedTime(): Long {
        return pref.getLong(SCANNED_TIME, 0)
    }

    fun removeScannedTime() {
        editor.remove(SCANNED_TIME)
        editor.apply()
    }

    /* Location CLick */
    fun setLocationClick(value: String) {
        editor.putString(SELECTED_LOCATION, value)
        editor.apply()
    }

    fun getLocationClick(): String {
        return pref.getString(SELECTED_LOCATION, "public")!!
    }

    fun removeLocationClick() {
        editor.remove(SELECTED_LOCATION)
        editor.apply()
    }

    /*Save HOme Events*/
    fun setHomeEvents(value: String) {
        editor.putString(HOME_EVENTS, value)
        editor.apply()
    }

    fun getHomeEvents(): String {
        return pref.getString(HOME_EVENTS, "")!!
    }

    fun removeHomeEvents() {
        editor.remove(HOME_EVENTS)
        editor.apply()
    }

    /*Save HOme Events*/
    fun setSIDValue(value: String) {
        editor.putString(SID_VALUE, value)
        editor.apply()
    }

    fun getSIDValue(): String {
        return pref.getString(SID_VALUE, "")!!
    }

    fun removeSIDValue() {
        editor.remove(SID_VALUE)
        editor.apply()
    }

    /*Save IndustrySelected Data */
    fun setIndustrySelectedData(value: String) {
        editor.putString(INDUSTRY_SELECTED, value)
        editor.apply()
    }

    fun getIndustrySelectedData(): String {
        return pref.getString(INDUSTRY_SELECTED, "")!!
    }

    fun removeIndustrySelectedData() {
        editor.remove(INDUSTRY_SELECTED)
        editor.apply()
    }

    /*Save IndustrySelected Data */
    fun setIndustrySave(value: String) {
        editor.putString(INDUSTRY_SAVE, value)
        editor.apply()
    }

    fun getIndustrySave(): String {
        return pref.getString(INDUSTRY_SAVE, "")!!
    }

    fun removeIndustrySave() {
        editor.remove(INDUSTRY_SAVE)
        editor.apply()
    }

    /* FIlter Back */
    fun setfilterBack(value: String) {
        editor.putString(FILTER_BACK, value)
        editor.apply()
    }

    fun getfilterBack(): String {
        return pref.getString(FILTER_BACK, "")!!
    }

    fun removefilterBack() {
        editor.remove(FILTER_BACK)
        editor.apply()
    }

    /*Industry Type*/
    fun setprofileID(value: String) {
        editor.putString(PROFILE_ID, value)
        editor.apply()
    }

    fun getprofileID(): String {
        return pref.getString(PROFILE_ID, "")!!
    }

    fun removeprofileID(): String {
        return pref.getString(PROFILE_ID, "")!!
    }

    /*Industry Type*/
    fun setSkilsSearch(value: String) {
        editor.putString(CITY_NAME, value)
        editor.apply()
    }

    fun getSkilsSearch(): String {
        return pref.getString(CITY_NAME, "")!!
    }

    fun removeSkilsSearch() {
        editor.remove(CITY_NAME)
        editor.apply()
    }

    /*Industry Type*/
    fun setIndustrySearch(value: String) {
        editor.putString(INDUSTRY_NAME, value)
        editor.apply()
    }

    fun getIndustrySearch(): String {
        return pref.getString(INDUSTRY_NAME, "")!!
    }

    fun removeIndustrySearch() {
        editor.remove(INDUSTRY_NAME)
        editor.apply()
    }

    /*Mgmnt Type*/
    fun setMgmntSearch(value: String) {
        editor.putString(MGMNT_NAME, value)
        editor.apply()
    }

    fun getMgmntSearch(): String {
        return pref.getString(MGMNT_NAME, "")!!
    }

    fun removeMgmntSearch() {
        editor.remove(MGMNT_NAME)
        editor.apply()
    }

    /*Mgmnt Type*/
    fun setQualificationSearch(value: String) {
        editor.putString(QUALIFICATION_NAME, value)
        editor.apply()
    }

    fun getQualificationSearch(): String {
        return pref.getString(QUALIFICATION_NAME, "")!!
    }

    fun removeQualificationSearch() {
        editor.remove(QUALIFICATION_NAME)
        editor.apply()
    }

    /*SKill Type*/
    fun setSkillSearch(value: String) {
        editor.putString(SKILL_NAME, value)
        editor.apply()
    }

    fun getSkillSearch(): String {
        return pref.getString(SKILL_NAME, "")!!
    }

    fun removeSkillSearch() {
        editor.remove(SKILL_NAME)
        editor.apply()
    }

    fun removeClearFIlter() {
        editor.remove(CLEAR_FILTER)
        editor.apply()
    }

    fun setNightMode(value: Boolean) {
        editor.putBoolean(IS_NIGHT_MODE_ON, value)
        editor.apply()
    }

    fun isNightMode(): Boolean {
        return pref.getBoolean(IS_NIGHT_MODE_ON, false)
    }

    fun setCityData(value: String) {
        editor.putString(REFER_DATA, value)
        editor.apply()
    }

    fun getCityData(): String {
        return pref.getString(REFER_DATA, "")!!
    }

    fun removeRefereceData() {
        editor.remove(REFER_DATA)
        editor.apply()
    }

    /*City Name*/
    fun setCityName(value: String) {
        editor.putString(CITY_NAME, value)
        editor.apply()
    }

    fun getCityName(): String {
        return pref.getString(CITY_NAME, "")!!
    }

    fun removeCityName() {
        editor.remove(CITY_NAME)
        editor.apply()
    }

    fun setBcityId(value: String) {
        editor.putString(BCITY_ID, value)
        editor.apply()
    }

    fun getBcityId(): String {
        return pref.getString(BCITY_ID, "")!!
    }

    fun removeBcityId() {
        editor.remove(BCITY_ID)
        editor.apply()
    }

    fun setGenderData(value: String) {
        editor.putString(GENDER_DATA, value)
        editor.apply()
    }

    fun getGenderData(): String {
        return pref.getString(GENDER_DATA, "")!!
    }

    fun removeGenderData() {
        editor.remove(GENDER_DATA)
        editor.apply()
    }

    fun setMgmtData(value: String) {
        editor.putString(MANAGEMENT_DATA, value)
        editor.apply()
    }

    fun getMgmtData(): String {
        return pref.getString(MANAGEMENT_DATA, "")!!
    }

    fun removeMgmtData() {
        editor.remove(MANAGEMENT_DATA)
        editor.apply()
    }

    fun setQualificationData(value: String) {
        editor.putString(QUALIFICATION_DATA, value)
        editor.apply()
    }

    fun getQualificationData(): String {
        return pref.getString(QUALIFICATION_DATA, "")!!
    }

    fun removeQualificationData() {
        editor.remove(QUALIFICATION_DATA)
        editor.apply()
    }

    fun setGridData(value: String) {
        editor.putString(GRID, value)
        editor.apply()
    }

    fun getGridData(): String {
        return pref.getString(GRID, "")!!
    }

    fun removeGridData() {
        editor.remove(GRID)
        editor.apply()
    }

    fun setRazorPayData(value: String) {
        editor.putString(RAZOR_DATA, value)
        editor.apply()
    }

    fun getRazorPayData(): String {
        return pref.getString(RAZOR_DATA, "")!!
    }

    fun removeRazorPayData() {
        editor.remove(RAZOR_DATA)
        editor.apply()
    }

    /*Save Pro*/
    fun setsaveProfile(value: String) {
        editor.putString(SAVE_PROFILE, value)
        editor.apply()
    }

    fun getsaveProfile(): String {
        return pref.getString(SAVE_PROFILE, "")!!
    }

    fun removesaveProfile() {
        editor.remove(SAVE_PROFILE)
        editor.apply()
    }

    fun setAccessData(value: Boolean) {
        editor.putBoolean(ACCESS_DATA, value)
        editor.apply()
    }

    fun getAccessData(): Boolean {
        return pref.getBoolean(ACCESS_DATA, false)
    }

    fun removeAccessData() {
        editor.remove(ACCESS_DATA)
        editor.apply()
    }

    fun setVstatusData(value: Boolean) {
        editor.putBoolean(VSTATUS, value)
        editor.apply()
    }

    fun getVstatusData(): Boolean {
        return pref.getBoolean(VSTATUS, false)
    }

    fun removeVstatusData() {
        editor.remove(VSTATUS)
        editor.apply()
    }

    fun setGhostModeData(value: Boolean) {
        editor.putBoolean(GHOSTMODETILL, value)
        editor.apply()
    }

    fun getGhostModeData(): Boolean {
        return pref.getBoolean(GHOSTMODETILL, false)
    }

    fun removeGhostModeData() {
        editor.remove(GHOSTMODETILL)
        editor.apply()
    }

    fun setRangeData(value: Int) {
        editor.putInt(RANGE_DATA, value)
        editor.apply()
    }

    fun getRangeData(): Int {
        return pref.getInt(RANGE_DATA, 2000)
    }

    fun removeRangeData() {
        editor.remove(RANGE_DATA)
        editor.apply()
    }

    fun setMinRangeData(value: Int) {
        editor.putInt(MIN_RANGE_DATA, value)
        editor.apply()
    }

    fun getMinRangeData(): Int {
        return pref.getInt(MIN_RANGE_DATA, 0)
    }

    fun removeMinRangeData() {
        editor.remove(MIN_RANGE_DATA)
        editor.apply()
    }

    fun setIndustryData(value: String) {
        editor.putString(INDUSTRYLIST, value)
        editor.apply()
    }

    fun getIndustryData(): String {
        return pref.getString(INDUSTRYLIST, "")!!
    }

    fun removeIndustryData() {
        editor.remove(INDUSTRYLIST)
        editor.apply()
    }

    fun setDOB(value: String) {
        editor.putString(DOB, value)
        editor.apply()
    }

    fun getDOB(): String {
        return pref.getString(DOB, "")!!
    }

    fun removeDOB() {
        editor.remove(DOB)
        editor.apply()
    }

    fun setAgeCount(value: String) {
        editor.putString(AGE_COUNT, value)
        editor.apply()
    }

    fun getAgeCount(): String {
        return pref.getString(AGE_COUNT, "")!!
    }

    fun removeAgeCount() {
        editor.remove(AGE_COUNT)
        editor.apply()
    }

    fun setIndustryCount(value: String) {
        editor.putString(INDUSTRY_COUNT, value)
        editor.apply()
    }

    fun getIndustryCount(): String {
        return pref.getString(INDUSTRY_COUNT, "")!!
    }

    fun removeIndustryCount() {
        editor.remove(INDUSTRY_COUNT)
        editor.apply()
    }

    /*Save Experience Data*/
    fun setExpData(value: String) {
        editor.putString(EXP_DATA, value)
        editor.apply()
    }

    fun getExpData(): String {
        return pref.getString(EXP_DATA, "")!!
    }

    fun removeExpData() {
        editor.remove(EXP_DATA)
        editor.apply()
    }

    /*Save Education Data*/
    fun setEduData(value: String) {
        editor.putString(EDU_DATA, value)
        editor.apply()
    }

    fun getEduData(): String {
        return pref.getString(EDU_DATA, "")!!
    }

    fun removeEduData() {
        editor.remove(EDU_DATA)
        editor.apply()
    }

    /*Save Scren Type*/
    fun setScreenType(value: String) {
        editor.putString(SCREEN_TYPE, value)
        editor.apply()
    }

    fun getScreenType(): String {
        return pref.getString(SCREEN_TYPE, "")!!
    }

    fun removeScreenType() {
        editor.remove(SCREEN_TYPE)
        editor.apply()
    }

    /*Save SSkilln Type*/
    fun setSKilltype(value: String) {
        editor.putString(SKILL_TYPE, value)
        editor.apply()
    }

    fun getSKilltype(): String {
        return pref.getString(SKILL_TYPE, "")!!
    }

    fun removeSKilltype() {
        editor.remove(SKILL_TYPE)
        editor.apply()
    }

    /*Set Landing Page*/
    fun setlandingPage(value: String) {
        editor.putString(LANDING_PAGE, value)
        editor.apply()
    }

    fun getlandingPage(): String {
        return pref.getString(LANDING_PAGE, "")!!
    }

    fun removelandingPage() {
        editor.remove(LANDING_PAGE)
        editor.apply()
    }

    /*FilterCount*/
    fun setFilterCount(value: String) {
        editor.putString(FILTER_COUNT, value)
        editor.apply()
    }

    fun getFilterCount(): String {
        return pref.getString(FILTER_COUNT, "")!!
    }

    fun removeFilterCount() {
        editor.remove(FILTER_COUNT)
        editor.apply()
    }

    /*FilterCount*/
    fun setEmailId(value: String) {
        editor.putString(EMAILID, value)
        editor.apply()
    }

    fun getEmailId(): String {
        return pref.getString(EMAILID, "")!!
    }

    fun removeEmailId() {
        editor.remove(EMAILID)
        editor.apply()
    }

    /*FilterCount*/
    fun setGetinTouch(value: String) {
        editor.putString(GETIN_TOUCH, value)
        editor.apply()
    }

    fun getGetinTouch(): String {
        return pref.getString(GETIN_TOUCH, "")!!
    }

    fun removeGetinTouch() {
        editor.remove(GETIN_TOUCH)
        editor.apply()
    }

    /*FilterCount*/
    fun setPhoneno(value: String) {
        editor.putString(PHONE_NO, value)
        editor.apply()
    }

    fun getPhoneno(): String {
        return pref.getString(PHONE_NO, "")!!
    }

    fun removePhoneno() {
        editor.remove(PHONE_NO)
        editor.apply()
    }

    /*Filter_skills*/
    /*SKill Type*/
    fun setFIlterSkillSearch(value: String) {
        editor.putString(FILTER_SKILL_SEARCH, value)
        editor.apply()
    }

    fun getFIlterSkillSearch(): String {
        return pref.getString(FILTER_SKILL_SEARCH, "")!!
    }

    fun removeFIlterSkillSearch() {
        editor.remove(FILTER_SKILL_SEARCH)
        editor.apply()
    }

    /*Filter skill name*/
    fun setFIlterSkillName(value: String) {
        editor.putString(FILTER_SKILL_NAME, value)
        editor.apply()
    }

    fun getFIlterSkillName(): String {
        return pref.getString(FILTER_SKILL_NAME, "")!!
    }

    fun removeFIlterSkillName() {
        editor.remove(FILTER_SKILL_NAME)
        editor.apply()
    }

    fun isAccepted(): Boolean {
        return pref.getBoolean(IS_ACCEPTED, false)
    }

    /*Business CityId*/

    fun setBusinessCityID(value: String) {
        editor.putString(BUSINESS_CITYID, value)
        editor.apply()
    }

    fun getBusinessCityID(): String {
        return pref.getString(BUSINESS_CITYID, "")!!
    }

    fun setcityID(value: String) {
        editor.putString(CITY_ID, value)
        editor.apply()
    }

    fun getcityID(): String {
        return pref.getString(CITY_ID, "")!!
    }

    fun removeBusinessCityID() {
        editor.remove(BUSINESS_CITYID)
        editor.apply()
    }

    /*get opp_user*/
    fun setOppUser(value: String) {
        editor.putString(OPP_USER, value)
        editor.apply()
    }

    fun getOppUser(): String {
        return pref.getString(OPP_USER, "")!!
    }

    fun removeOppUser() {
        editor.remove(OPP_USER)
        editor.apply()
    }

    /*Industry Type*/
    fun setproofID(value: String) {
        editor.putString(PROOF_ID, value)
        editor.apply()
    }

    fun getproofID(): String {
        return pref.getString(PROOF_ID, "")!!
    }

    fun removeproofID(): String {
        return pref.getString(PROOF_ID, "")!!
    }

    /*Industry Type*/
    fun setPayment(value: String) {
        editor.putString(PAYMENT, value)
        editor.apply()
    }

    fun getPayment(): String {
        return pref.getString(PAYMENT, "")!!
    }

    fun removePayment(): String {
        return pref.getString(PAYMENT, "")!!
    }

    /*Industry Type*/
    fun setPlanId(value: String) {
        editor.putString(PLAN_ID, value)
        editor.apply()
    }

    fun getPlanId(): String {
        return pref.getString(PLAN_ID, "")!!
    }

    fun removePlanId(): String {
        return pref.getString(PLAN_ID, "")!!
    }

    /*Industry Type*/
    fun setRazorPlanId(value: String) {
        editor.putString(RAZORPLAN_ID, value)
        editor.apply()
    }

    fun getRazorPlanId(): String {
        return pref.getString(RAZORPLAN_ID, "")!!
    }

    fun removeRazorPlanId(): String {
        return pref.getString(RAZORPLAN_ID, "")!!
    }

    /*Save Customer ID*/
    fun setCustomerID(value: String) {
        editor.putString(CUSTOMER_ID, value)
        editor.apply()
    }

    fun getCustomerID(): String {
        return pref.getString(CUSTOMER_ID, "")!!
    }

    fun removeCustomerID(): String {
        return pref.getString(CUSTOMER_ID, "")!!
    }

    /*Save CardsList*/
    fun setCardsList(value: String) {
        editor.putString(CARDS_LIST, value)
        editor.apply()
    }

    fun getCardsList(): String {
        return pref.getString(CARDS_LIST, "")!!
    }

    fun removeCardsList(): String {
        return pref.getString(CARDS_LIST, "")!!
    }


    fun setPaymentResponse(value: String) {
        editor.putString(PAYMENT_RESPONSE, value)
        editor.apply()
    }

    fun getPaymentResponse(): String {
        return pref.getString(PAYMENT_RESPONSE, "")!!
    }

    fun removePaymentResponse(): String {
        return pref.getString(PAYMENT_RESPONSE, "")!!
    }

    fun setTransactionID(value: String) {
        editor.putString(TRANS_ID, value)
        editor.apply()
    }

    fun getTransactionID(): String {
        return pref.getString(TRANS_ID, "")!!
    }

    fun removeTransactionID(): String {
        return pref.getString(TRANS_ID, "")!!
    }

    /*MRP id*/
    fun setRPaySubId(value: String) {
        editor.putString(RPAY_ID, value)
        editor.apply()
    }

    fun getRPaySubId(): String {
        return pref.getString(RPAY_ID, "")!!
    }

    fun removeRPaySubId(): String {
        return pref.getString(RPAY_ID, "")!!
    }

    /*Plan Type*/
    fun setplantype(value: String) {
        editor.putString(PLAN_TYPE, value)
        editor.apply()
    }

    fun getplantype(): String {
        return pref.getString(PLAN_TYPE, "")!!
    }

    fun removeplantype(): String {
        return pref.getString(PLAN_TYPE, "")!!
    }

    /*Face Detect*/
    fun setfacedetect(value: String) {
        editor.putString(FACE_DETECT, value)
        editor.apply()
    }

    fun getfacedetect(): String {
        return pref.getString(FACE_DETECT, "")!!
    }

    fun removefacedetect(): String {
        return pref.getString(FACE_DETECT, "")!!
    }

    /*Location Details*/
    fun setLocationDetails(value: String) {
        editor.putString(LOCATION_DETAILS, value.toString())
        editor.apply()
    }

    fun getLocationDetails(): String {
        return pref.getString(LOCATION_DETAILS, "")!!
    }

    fun removeLocationDetails() {
        editor.remove(LOCATION_DETAILS)
        editor.apply()
    }

    /*Location Details*/
    fun setCheckInDetails(value: Boolean) {
        editor.putBoolean(LOCATION_DETAILS, value)
        editor.apply()
    }

    fun getCheckInDetails(): Boolean {
        return pref.getBoolean(LOCATION_DETAILS, false)!!
    }

    /*Authenticaption*/
    fun setAthentication(value: Boolean) {
        editor.putBoolean(AUTHENTICATION, value)
        editor.apply()
    }

    fun getAthentication(): Boolean {
        return pref.getBoolean(AUTHENTICATION, false)
    }

    fun removeAthentication() {
        editor.remove(AUTHENTICATION)
        editor.apply()
    }

    /*Authenticaption*/
    fun setIsAthenticated(value: Boolean) {
        editor.putBoolean(AUTHENTICATED, value)
        editor.apply()
    }

    fun isAthenticated(): Boolean {
        return pref.getBoolean(AUTHENTICATED, false)
    }

    /*Profile Filling*/
    fun setIsProfileFilled(value: Boolean) {
        editor.putBoolean(PROFILE_FILLED, value)
        editor.apply()
    }

    fun isProfileFilled(): Boolean {
        return pref.getBoolean(PROFILE_FILLED, false)
    }


    companion object {

        // Shared preferences file location_name
        private val PREFER_NAME = "Outer"
        private val LOCATION = "Location"
        private val SCAN_LOCATION = "scan_location"
        private val USERNAME = "username"
        private val USER_ID = "user_id"
        private val QB_USERID = "qb_userid"
        private val SET_TOKEN = "token"
        private val SELECTED_MOVIES = "selected_movies"
        private val TITLE = "title"
        private val GENRE_TYPE = "genre_Type"
        private val DEVICE_ID = "device_id"
        private val GET_STARTED = "GET_STARTED"
        private val LIFESTYLE_OPTION = "lifestyle_option "
        private val IMDB_SEARCH = "imdb_search "
        private val PHONE_ID = "phone_ID"
        private val GOOGLE_LOGIN = "google_login"
        private val GOOGLE_DATA = "google_data"
        private val FB_DATA = "fb_data"
        private val SPOTIFY_TOKEN = "spotify_token"
        private val SPOTIFY_LOGOUT = "spotify_logout"
        private val SAVELIST = "save_list"
        private val SEARCH_CLICK = "search_click"
        private val OCCUPATION_CLICK = "occupation_click"
        private val LIFESTYLE_CLICK = "lifestyle_click"
        private val SPOTIFY_DATA = "spotify_data"
        private val PROFILE_DEFAULT = "profile_default"
        private val PROFILE_REDIRECT_PIC = "redirect_pic"
        private val LIFESTYLE_REDIRECT = "lifstyle_redirect"
        private val PROFILE_DATA = "profile_data"
        private val PROFILE_LIFESTYLE = "profile_lifestyle"
        private val EID = "eid"
        private val CHECK_IN = "check_in"
        private val SELECTED_LOCATION = "selectedLocation"
        private val SCANNED_TIME = "scanned_time"
        private val CALL_LOG_ID = "call_log_id"
        private val CALL_TIMER = "call_timer"
        private const val QB_USER_ID = "qb_user_id"
        private const val QB_USER_LOGIN = "qb_user_login"
        private const val QB_USER_PASSWORD = "qb_user_password"
        private const val QB_USER_FULL_NAME = "qb_user_full_name"
        private const val QB_USER_TAGS = "qb_user_tags"
        private val GIF_DATA = "gif_data"
        private val PROFANITY_LIST = "profanity_list"
        private val QUICK_TEMPLATE = "quick_templates"
        private val FILTER_DATA = "filter_data"
        private val DETAILS_FILTER_DATA = "detailds_filter_data"
        private val GRID_REDIRECT = "grid_redirect"
        private val LIFESTYLE_DATACLICK = "lifestyle_Dataclick"
        private val LIFESTYLE_SAVE = "lifestyle_save"
        private val INDUSTRY_DATA = "industry_data"
        private val INFUS_FIl_DATA = "industry_data_filter"
        private val FILTER_SAVE = "filter_Save"
        private val CURRENT_SESSION_DATA = "current_session_data"
        private val USER_PROFILE_TYPE = "user_profile_type"
        private val UNIQUE_ID = "unique_id"
        private val INDUSTRY_TYPE = "industry_type"
        private val ENTITY_ID = "entity_id"
        private val SPOTIFY_CLICK = "spotify_click"
        private val FILTER_INDUSTRY = "filter_industry"
        private val HOME_EVENTS = "Home_events"
        private val SID_VALUE = "sid_value"
        private val INDUSTRY_SELECTED = "industry_selected"
        private val SCAN_STATUS = "scan_status"
        private val INDUSTRY_SAVE = "industry_save"
        private val FILTER_SAVE_DATA = "filter_Save_data"
        private val FILTER_BACK = "filter_back"
        private val WHOCANI_DATA = "whocanIsee_data"
        private val CLEAR_FILTER = "clear_filter"
        private val INDUS_SELCTED_ID = "indus_selectedid"
        private val SAVE_GSON_Industry = "save_gson_industry"
        private val CITY_NAME = "cityname"
        private val IS_NIGHT_MODE_ON = "is_night_on"
        private val IS_ACCEPTED = "is_accepted"
        private val REFER_DATA = "reference_Data"
        private val INDUSTRYLIST = "industry_list"
        private val PROFILE_ID = "profile_id"
        private val GENDER_DATA = "gender_data"
        private val MANAGEMENT_DATA = "mgmt_data"
        private val INDUSTRY_NAME = "industry_name"
        private val MGMNT_NAME = "managment_name"
        private val QUALIFICATION_NAME = "qualification_name"
        private val SKILL_NAME = "skill_name"
        private val DOB = "dob"
        private val AGE_COUNT = "age_count"
        private val INDUSTRY_COUNT = "industry_count"
        private val EXP_DATA = "exp_data"
        private val SCREEN_TYPE = "screen_type"
        private val QUALIFICATION_DATA = "qualification_data"
        private val GRID = "grid"
        private val RANGE_DATA = "range_data"
        private val MIN_RANGE_DATA = "min_range_data"
        private val ACCESS_DATA = "access_data"
        private val VSTATUS = "vstatus"
        private val GHOSTMODETILL = "ghostmodetill"
        private val EDU_DATA = "education_data"
        private val SKILL_TYPE = "skill_type"
        private val LANDING_PAGE = "landing_page"
        private val FILTER_SKILL_SEARCH = "filter_skill_search"
        private val FILTER_SKILL_NAME = "filter_skill_name"
        private val CITY_ID = "city_id"
        private val BUSINESS_CITYID = "business_cityid"
        private val DETAIL_HOTSPOT = "detail_spot"
        private val OPP_USER = "opp_user"
        private val AUTH_TYPE = "auth_type"
        private val PROFILE_READY = "profile_ready"
        private val FILTER_COUNT = "filter_count"
        private val EMAILID = "emailid"
        private val BCITY_ID = "b_cityid"
        private val PHONE_NO = "phone_no"
        private val PROOF_ID = "proof_id"
        private val GETIN_TOUCH = "get_inTOuch"
        private val PAYMENT = "payment"
        private val PLAN_ID = "plan_id"
        private val PAYMENT_RESPONSE = "payment_response"
        private val TRANS_ID = "trans_id"
        private val PLAN_TYPE = "plan_type"
        private val PROFILE_PATH = "profile_path"
        private val FACE_DETECT = "face_detect"
        private val CUSTOMER_ID = "customer_id"
        private val CARDS_LIST = "cards_list"
        private val LOCATION_DETAILS = "location_details"
        private val RPAY_ID = "RPAY_ID"
        private val RAZORPLAN_ID = "RAZORPLAN_ID"
        private val RAZOR_DATA = "RazorDATA"
        private val SAVE_PROFILE = "save_pro"
        private val AUTHENTICATION = "authentication"
        private val AUTHENTICATED = "authenticated"
        private val PROFILE_FILLED ="is_profile_filled"
    }


}
