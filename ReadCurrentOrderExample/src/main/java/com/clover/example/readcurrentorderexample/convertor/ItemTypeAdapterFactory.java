package com.clover.example.readcurrentorderexample.convertor;


import android.util.Log;

import com.clover.example.readcurrentorderexample.PistachioItem;
import com.clover.example.readcurrentorderexample.PistachioOrder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lidongdong on 10/26/15.
 */
public class ItemTypeAdapterFactory implements TypeAdapterFactory {
    final static List<PistachioOrder> orders=new ArrayList<PistachioOrder>();

    public static List<PistachioOrder> getOrders() {
        Log.d("demo1","size="+orders.size()+" ,getOrder called");
        return orders;
    }

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        orders.clear();

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }
            // for each json object
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    // get the converted json object
                    // check the existence of both "orders" and "total" to make sure it is a order jobject
                    if (jsonObject.has("ORDERS") && jsonObject.get("ORDERS").isJsonObject() && jsonObject.has("TOTAL") && jsonObject.get("TOTAL").isJsonObject())
                    {
                        //Log.d("demo1","find ORDERS object");
                        JsonElement jsonORDERSElement = jsonObject.get("ORDERS");// get ORDERS object

                        PistachioOrder pistachioOrder=new PistachioOrder();
                        pistachioOrder.setOrderId(jsonORDERSElement.getAsJsonObject().get("order_id").getAsString());

                        JsonElement items=jsonORDERSElement.getAsJsonObject().get("items");
                        //Log.d("demo1","items raw: "+items.toString());
                        //Log.d("demo1",items.getAsJsonObject().entrySet().toString());
                        Set<Map.Entry<String,JsonElement>> set=items.getAsJsonObject().entrySet();
                        Iterator<Map.Entry<String,JsonElement>> iterator=set.iterator();
                        // could be more than one item in the "items"
                        while(iterator.hasNext()){
                            PistachioItem pistachioItem=new PistachioItem();

                            Map.Entry<String,JsonElement> entry=iterator.next();
                            JsonObject itemDetail=entry.getValue().getAsJsonObject();
//                            Log.d("demo1","title: "+itemDetail.get("item_title")+"");
//                            Log.d("demo1","total price: "+itemDetail.get("item_total_price")+"");
//                            Log.d("demo1","");
                            pistachioItem.setPrice(itemDetail.get("item_price").getAsDouble());
                            pistachioItem.setQuantity(itemDetail.get("item_qty").getAsInt());
                            pistachioItem.setTitle(itemDetail.get("item_title") + "");
                            pistachioItem.setTotal(itemDetail.get("item_total_price").getAsDouble());
                            pistachioOrder.getItems().add(pistachioItem);
                        }
                        // get "TOTAL" object
                        JsonElement jsonTOTALElement = jsonObject.get("TOTAL");
                        double totalWithTax=jsonTOTALElement.getAsJsonObject().get("total").getAsDouble();
                        pistachioOrder.setTotalWithTax(totalWithTax);
                        // finish with 1 object and save it
                        orders.add(pistachioOrder);
                    }

//                    if(jsonObject.has("TOTAL") && jsonObject.get("TOTAL").isJsonObject()){
//                        jsonElement = jsonObject.get("TOTAL");
//                        Log.d("demo1","total: "+jsonElement.toString());
////                        double totalWithTax=jsonElement.getAsJsonObject().get("total").getAsDouble();
////                        pistachioOrder.setTotalWithTax(totalWithTax);
//                    }

//                    orders.add(pistachioOrder);
                }
                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}

