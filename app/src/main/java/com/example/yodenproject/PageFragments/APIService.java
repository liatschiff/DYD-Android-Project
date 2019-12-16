package com.example.yodenproject.PageFragments;

import com.example.yodenproject.Notifications.MyResponse;
import com.example.yodenproject.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAM25Bw0Y:APA91bESPlksb-kEQR3jqZqg7CM3N6Rcxx09pYnjY-6WFwgU3PVIE6Ni9PEWDOxDsL0YqLR0BJtXTyqmSl8hXT4VSx_1JBNLA4pCU3-oGFL0GGnuSCH4f7IzcPxHxYoaowrhh9nfCrkd"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
