package com.clover.example.readcurrentorderexample;

import com.clover.example.readcurrentorderexample.pojo.Root;
import com.clover.example.readcurrentorderexample.pojo.Token;

import retrofit.http.GET;
import retrofit.Call;
import retrofit.http.Path;
import retrofit.http.Query;

import java.util.List;
public interface OnlineOrderService {
	//? op =getToken & UserName ={ userName }& Password ={password} &apikey=dgr2yc9wbq
	@GET("easyway.php")
	Call<Token> authenticateUser(@Query("op") String op, @Query("UserName") String userName, @Query("Password") String password,@Query("apikey") String apiKey);

	//http://ordrsrvr.net/easyway.php? op=getOrder& AuthToken=50e0582725ec975c874ced4c6d794a81 & slug=blue_charlotte & apikey=dgr2yc9wbq
	@GET("easyway.php")
	Call<Root> fetchOrders(@Query("op") String op,@Query("AuthToken") String authToken,@Query("slug") String slug, @Query("apikey") String apiKey);
}