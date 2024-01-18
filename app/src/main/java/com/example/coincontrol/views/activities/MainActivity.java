package com.example.coincontrol.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.coincontrol.adapters.TransactionsAdapter;
import com.example.coincontrol.models.Transaction;
import com.example.coincontrol.utils.Constants;
import com.example.coincontrol.utils.Helper;
import com.example.coincontrol.viewmodels.MainViewModel;
import com.example.coincontrol.views.fragments.AddTransactionFragment;
import com.example.coincontrol.R;
import com.example.coincontrol.databinding.ActivityMainBinding;
import com.example.coincontrol.views.fragments.StatsFragment;
import com.example.coincontrol.views.fragments.TransactionsFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calendar;
    /*
    0=daily
    1=Monthly
    2=calender
    3=summary
    4=notes
    
     */
//    int selectedTab =0;
    Realm realm;
    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        setupDatabase();

        setSupportActionBar(binding.materialToolbar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        //updateDate();


//        binding.nextDateBtn.setOnClickListener(c -> {
//            if (Constants.SELECTED_TAB == Constants.DAILY){
//                calendar.add(Calendar.DATE, 1);
//            }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
//                calendar.add(Calendar.MONTH, 1);
//            }
//            updateDate();
//        });
//
//        binding.previousDateBtn.setOnClickListener(c -> {
//            if (Constants.SELECTED_TAB == Constants.DAILY){
//                calendar.add(Calendar.DATE, -1);
//            } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
//                calendar.add(Calendar.MONTH, -1);
//            }
//            updateDate();
//        });
//
//        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AddTransactionFragment().show(getSupportFragmentManager(), null);
//            }
//        });
//
//        // ArrayList<Transaction> transactions = new ArrayList<>();
////        transactions.add(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,2));
////        transactions.add(new Transaction(Constants.EXPENSE,"Salary","Bussiness","Some note here",new Date(),-900,4));
////        transactions.add(new Transaction(Constants.INCOME,"Loan","Cash","Some note here",new Date(),500,5));
////        transactions.add(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,6));
//
//
////        realm.beginTransaction();
////        //Enter here comde to start and end realm
////
////        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,new Date().getTime()));
////        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,new Date().getTime()));
////        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Loan","Cash","Some note here",new Date(),500,new Date().getTime()));
////        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,new Date().getTime()));
////
////        realm.commitTransaction();
//
////        RealmResults<Transaction> transactions = realm.where(Transaction.class).findAll();
//
//        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
//
//        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getText().equals("Monthly")) {
//                    Toast.makeText(MainActivity.this, tab.getText().toString(), Toast.LENGTH_SHORT).show();
//                    Constants.SELECTED_TAB=1;
//                    updateDate();
//                } else if (tab.getText().equals("Daily")) {
//                    Constants.SELECTED_TAB=0;
//                    updateDate();
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
//            @Override
//            public void onChanged(RealmResults<Transaction> transactions) {
//
//                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);
//                binding.transactionsList.setAdapter(transactionsAdapter);
//                if (transactions.size() > 0){
//                    binding.emptyState.setVisibility(View.GONE);
//                }else {
//                    binding.emptyState.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        viewModel.totalIncome.observe(this, new Observer<Double>() {
//            @Override
//            public void onChanged(Double aDouble) {
//                binding.incomeLbl.setText(String.valueOf(aDouble));
//            }
//        });
//        viewModel.totalExpense.observe(this, new Observer<Double>() {
//            @Override
//            public void onChanged(Double aDouble) {
//                binding.expenseLbl.setText(String.valueOf(aDouble));
//            }
//        });
//        viewModel.totalAmount.observe(this, new Observer<Double>() {
//            @Override
//            public void onChanged(Double aDouble) {
//                binding.totalLbl.setText(String.valueOf(aDouble));
//            }
//        });
//        viewModel.getTransactions(calendar);
//    }

//        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(this,transactions);
//        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
//        binding.transactionsList.setAdapter(transactionsAdapter);


//    void setupDatabase() {
//        Realm.init(this);
//        realm = Realm.getDefaultInstance();
//    }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionsFragment());
        transaction.commit();


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.transactions){
                    getSupportFragmentManager().popBackStack();
                }
                else if (item.getItemId() == R.id.stats) {
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });

//    void updateDate(){
////        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM,yyyy");
//
//        if (Constants.SELECTED_TAB == Constants.DAILY){
//            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
//        }
//        else if (Constants.SELECTED_TAB == Constants.MONTHLY){
//            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
//        }
//        viewModel.getTransactions(calendar);
//    }
    }
    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}

