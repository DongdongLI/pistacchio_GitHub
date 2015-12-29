package com.clover.example.readcurrentorderexample;

import android.accounts.Account;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.example.readcurrentorderexample.asynctask.FetchOrdersTask;
import com.clover.example.readcurrentorderexample.event.OrdersAvailableEvent;
import com.clover.example.readcurrentorderexample.event.OrdersFetchCompleteEvent;
import com.clover.example.readcurrentorderexample.newgcm.MyGcmListenerService;
import com.clover.example.readcurrentorderexample.newgcm.QuickstartPreferences;
import com.clover.example.readcurrentorderexample.newgcm.RegistrationIntentService;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROJECT_NUMBER = "823465535209";
    public static final String REGISTER_ID = "register_id";
    public static final String APP_VERSION="app_version";
    private Account account;
    private OrderConnector orderConnector;
    private BroadcastReceiver mRegistrationBroadcastReceiver; // get the result of registration

    private EventBus bus;

    private TextView orderId;
    private TextView lineItemCount;
    private TextView total;
    private TextView createTime;
    private Button testBtn;

    GoogleCloudMessaging gcm;

    FetchOrdersTask task;
    private List<PistachioOrder> pistachioOrders;
    private List<String> orderIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // UI setup for testing purpose
        orderId = (TextView) findViewById(R.id.order_id);
        lineItemCount = (TextView) findViewById(R.id.line_item_count);
        total = (TextView) findViewById(R.id.total);
        createTime = (TextView) findViewById(R.id.create_time);
        testBtn=(Button)findViewById(R.id.conntBtn);
        testBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                bus.post(new OrdersAvailableEvent("order available."));
            }
        });
        // get gcm instance and eventBus object
        gcm=GoogleCloudMessaging.getInstance(this);
        bus=EventBus.getDefault();
       // check share preference to see if the app is already registered
        checkIfRegistered();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // token stored in sharedPreference
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);

                // this flag shows if the token has been sent to server
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(getApplicationContext(),"Token sent to server",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"error when fetching token",Toast.LENGTH_SHORT).show();
                }
            }
        };
        // subscribe this class to receive the EventBus Event
        bus.register(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {

        super.onResume();
        // Retrieve the Clover account
        if (account == null) {
            account = CloverAccount.getAccount(this);

            if (account == null) {
                Toast.makeText(this, getString(R.string.no_account), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        // Create and Connect to the OrderConnector
        connect();

    }

    @Override
    protected void onPause() {
        disconnect();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void connect() {

        disconnect();

        if (account == null)
            return;

        orderConnector = new OrderConnector(this, account, null);
        orderConnector.connect();
    }

    private void disconnect() {

        if (orderConnector == null)
            return;

        orderConnector.disconnect();
        orderConnector = null;
    }
    // this two need to stay in Main Activity
    private void loadLastOrder() {
        new OrderAsyncTask().execute();
    }

    private class OrderAsyncTask extends AsyncTask<Void, Void, Order> {

        @Override
        protected final Order doInBackground(Void... params) {

            String orderId = null;
            Cursor cursor = null;

            try {
                // sort the orders based on the the last modified time, then pick up one
                cursor = MainActivity.this.getContentResolver()
                        .query(OrderContract.Summaries.contentUriWithAccount(account)
                                , new String[]{OrderContract.Summaries.ID}, null, null,
                                OrderContract.Summaries.LAST_MODIFIED + " DESC LIMIT 1");

                if (cursor != null && cursor.moveToFirst()) {
                    orderId = cursor.getString(cursor.getColumnIndex(OrderContract.Summaries.ID));
                }

                if (isValidOrder(orderId)) {
                    return orderConnector.getOrder(orderId);
                }

                // no order create a new one
                PistachioOrder pistachioOrder = createMockOrders(orderId);
                return updateCloverWithOrders(pistachioOrder);

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (BindingException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return null;
        }

        private boolean isValidOrder(String orderId) {

            return (orderId == null) ? false : true;
        }
        @Deprecated
        private PistachioOrder createMockOrders(String orderId) throws
                RemoteException, ClientException, ServiceException, BindingException {

            PistachioOrder pistachioOrder = new PistachioOrder();
            pistachioOrder.setItems(new ArrayList<PistachioItem>());
            pistachioOrder.setOrderId("12345");

            PistachioItem item1 = createOrderItem(1.25, 2, "test Item 1", 2.50);
            pistachioOrder.getItems().add(item1);

            PistachioItem item2 = createOrderItem(3.98, 1, "test Item 2", 3.98);
            pistachioOrder.getItems().add(item2);

            pistachioOrder.setTotalWithTax(6.48);
            Log.d("demo1", pistachioOrder.toString());

            return pistachioOrder;

        }
        @Deprecated
        private Order updateCloverWithOrders(PistachioOrder pistachioOrder) throws
                RemoteException, ClientException, ServiceException, BindingException {

            Order order = orderConnector.createOrder(new Order());

      /* by D.L

      * never do that because all the modification done to the order
      * can only be finished with orderConnector*/
//          order.setLineItems(new ArrayList<LineItem>());
      /*
      * we need
      * tax rate
      * disaccount
      * or maybe we just add the tax into the total*/

            createCustomOrder(pistachioOrder, order);
            order.setTotal((long) (pistachioOrder.getTotalWithTax() * 100));
            Log.d("demo1", "order: " + order.toString());

            return order;
        }
        @Deprecated
        private void createCustomOrder(PistachioOrder pistachioOrder, Order order) throws
                RemoteException, ClientException, ServiceException, BindingException {

            for (PistachioItem item : pistachioOrder.getItems()) {

                Log.d("demo1", "in for");
                LineItem lineItem = new LineItem();
                lineItem.setName(item.getTitle());
                lineItem.setPrice((long) (item.getPrice() * 100));
                lineItem.setUnitQty(item.getQuantity());
//            lineItem.setDiscountAmount();
                //lineItem.setTaxRates(TaxRate)

                orderConnector.addCustomLineItem(order.getId(), lineItem, false);
            }

        }
        @Deprecated
        private PistachioItem createOrderItem(double price, int quantity, String title, double total) {

            PistachioItem item = new PistachioItem();
            item.setPrice(price);
            item.setQuantity(quantity);
            item.setTitle(title);
            item.setTotal(total);

            return item;
        }

        @Override
        protected final void onPostExecute(Order order) {

            if (order == null) {
                Log.d("demo1", "is null");
                return;
            }

            // Populate the UI
            orderId.setText(order.getId());

            int lineItemSize = 0;

            if (order.getLineItems() != null) {
                lineItemSize = order.getLineItems().size();
            }
            lineItemCount.setText(Integer.toString(lineItemSize));
            total.setText(BigDecimal.valueOf(order.getTotal()).
                    divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            createTime.setText(new Date(order.getCreatedTime()).toString());
        }
    }

    private void checkIfRegistered() {
        /*
        * Check the SharedPreference to see if there is "token"
        * to see if this device has been registered*/
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token=sharedPreferences.getString("token", "");
        if(token.length()==0){ // haven't registered yet
            Log.d("demo1","Not registered.");
            if(checkPlayServices()){ // check google play service, and start registration service
                Log.d("demo1","google service available");
                Intent intent=new Intent(getApplicationContext(), RegistrationIntentService.class);
                startService(intent);
            }
        }else{
            Log.d("demo1","Registered.");
        }

    }
    // check availability of google play service.
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("demo1", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    /*
    * Change the custom PistachioOrders to Clover orders*/
    public  List<Order> convert(List<PistachioOrder> pistachioOrders){
        List<Order> orders=new ArrayList<>();
        // keep all the OrderID
        orderIds=new ArrayList<String>();
        Log.d("demo1","pistachioOrders size: "+pistachioOrders.size()+"");
        if(pistachioOrders==null)
            pistachioOrders=task.getPistachioOrders();
        Order order=null;
        LineItem lineItem;
        try {
            for (PistachioOrder pOrder : pistachioOrders) {
                order = orderConnector.createOrder(new Order());
                // keep the ID since they will be needed to take out all the orders
                orderIds.add(order.getId());
                // IDs will be automatically generated

                for (PistachioItem pItem : pOrder.getItems()) {
                    // if there is multiple instances of one particular item, add it multiple times.
                    for(int i=0;i<pItem.getQuantity();i++)
                    {
                        lineItem = new LineItem();
                        //lineItem.setId(pItem.getTitle()); // this will cause problem !!!
                        lineItem.setName(pItem.getTitle());
                        lineItem.setPrice((long) (pItem.getPrice() * 100));
                        lineItem.setUnitQty(pItem.getQuantity());
                        orderConnector.addCustomLineItem(order.getId(), lineItem, false);

                    }
                }
                orders.add(order);
            }
        }
        catch (RemoteException e){
            Log.d("demo1",e.getMessage());
        }
        catch (ClientException e){
            Log.d("demo1",e.getMessage());
        }catch (ServiceException e){
            Log.d("demo1",e.getMessage());
        }catch (BindingException e){
            Log.d("demo1",e.getMessage());
        }

        return orders;
    }

    // this will be triggered when the event comes from the MyGcmListenerService notifying there are new orders in server.
    public void onEvent(OrdersAvailableEvent event){
        Log.d("demo1","on event 1");
        task=new FetchOrdersTask();
        task.execute();
    }
    // this will be triggered when the event comes from the FetchOrdersTask indicating that the fetch has been completed.
    public void onEventBackgroundThread(OrdersFetchCompleteEvent event) {
        Log.d("demo1","on event 2");
        pistachioOrders=task.getPistachioOrders();
        // convert the orders to the Clover style.
        convert(pistachioOrders);
        Log.d("demo1","after convert");
        List<Order> orders=null;
        try{
            orders=orderConnector.getOrders(orderIds);
            Log.d("demo1","order size "+orders.size());
        }catch(Exception e){
            Log.d("demo1","error: "+e.getMessage());
        }


        for(Order order:orders){
//            if(order==null)
//                Log.d("demo1","order is null");
            Log.d("demo1","order: "+order.getId());
            for(LineItem item: order.getLineItems()){

                Log.d("demo1",(item.getName()));
                Log.d("demo1", ""+(item.getPrice()));

            }
        }
    }

}
