package com.example.coincontrol.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coincontrol.R;
import com.example.coincontrol.adapters.TransactionsAdapter;
import com.example.coincontrol.databinding.FragmentTransactionsBinding;
import com.example.coincontrol.models.Transaction;
import com.example.coincontrol.utils.Constants;
import com.example.coincontrol.utils.Helper;
import com.example.coincontrol.viewmodels.MainViewModel;
import com.example.coincontrol.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;



public class TransactionsFragment extends Fragment {

    Calendar calendar;
    /*
    0=daily
    1=Monthly
    2=calender
    3=summary
    4=notes

     */
    public MainViewModel viewModel;


    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentTransactionsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c -> {
            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE, 1);
            }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c -> {
            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE, -1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddTransactionFragment().show(getParentFragmentManager(), null);
            }
        });

        // ArrayList<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,2));
//        transactions.add(new Transaction(Constants.EXPENSE,"Salary","Bussiness","Some note here",new Date(),-900,4));
//        transactions.add(new Transaction(Constants.INCOME,"Loan","Cash","Some note here",new Date(),500,5));
//        transactions.add(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,6));


//        realm.beginTransaction();
//        //Enter here comde to start and end realm
//
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Loan","Cash","Some note here",new Date(),500,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Bussiness","Cash","Some note here",new Date(),500,new Date().getTime()));
//
//        realm.commitTransaction();

//        RealmResults<Transaction> transactions = realm.where(Transaction.class).findAll();

        binding.transactionsList.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")) {
                    Toast.makeText(getContext(), tab.getText().toString(), Toast.LENGTH_SHORT).show();
                    Constants.SELECTED_TAB=1;
                    updateDate();
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB=0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewModel.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getContext(), transactions);
                binding.transactionsList.setAdapter(transactionsAdapter);
                if (transactions.size() > 0){
                    binding.emptyState.setVisibility(View.GONE);
                }else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);

        return binding.getRoot();
    }

    void updateDate(){
       // SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM,yyyy");

        if (Constants.SELECTED_TAB == Constants.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        }
        else if (Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar);
    }
}