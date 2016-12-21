package edu.np.ece.beaconmonitor.Retrofit;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Tung on 5/31/2016.
 */
public interface StringClient {

    @POST("test/create")
    Call<ResponseBody> pushStudentArrayList(@Body JsonObject toUp);
}
