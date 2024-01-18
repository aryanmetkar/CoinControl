package com.example.coincontrol.views.fragments;

import static com.example.coincontrol.utils.Constants.SELECTED_STATS_TYPE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.coincontrol.R;
import com.example.coincontrol.databinding.FragmentStatsBinding;
import com.example.coincontrol.models.Transaction;
import com.example.coincontrol.utils.Constants;
import com.example.coincontrol.utils.Helper;
import com.example.coincontrol.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

public class StatsFragment extends Fragment {


    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentStatsBinding binding;
    Calendar calendar;
    /*     0=daily     1=Monthly  */
    public MainViewModel viewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatsBinding.inflate(inflater);

        calendar = Calendar.getInstance();
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        updateDate();


        binding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

                SELECTED_STATS_TYPE = Constants.INCOME;
                updateDate();
            }
        });

        binding.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));

                SELECTED_STATS_TYPE = Constants.EXPENSE;
                updateDate();
            }
        });

        binding.nextDateBtn.setOnClickListener(c -> {
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY){
                calendar.add(Calendar.DATE, 1);
            }else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c -> {
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY){
                calendar.add(Calendar.DATE, -1);
            } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")) {
                    Toast.makeText(getContext(), tab.getText().toString(), Toast.LENGTH_SHORT).show();
                    Constants.SELECTED_TAB_STATS=1;
                    updateDate();
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB_STATS=0;
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
        //viewModel.getTransactions(calendar,SELECTED_STATS_TYPE);



        Pie pie = AnyChart.pie();

        viewModel.categoriesTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                if (transactions.size()>0){
                    binding.emptyState.setVisibility(View.GONE);
                    binding.anyChart.setVisibility(View.VISIBLE);

                    List<DataEntry> data = new ArrayList<>();

                    Map<String,Double> categoryMap = new HashMap<>();

                    for (Transaction transaction:transactions){
                        String category = transaction.getCategory();
                        double amount = transaction.getAmount();

                        if (categoryMap.containsKey(category)){
                            double currentTotal = categoryMap.get(category).doubleValue();
                            currentTotal += Math.abs(amount);

                            categoryMap.put(category,currentTotal);

                        } else {
                            categoryMap.put(category,Math.abs(amount));
                        }
                    }

                    for (Map.Entry<String , Double> entry : categoryMap.entrySet()){
                        data.add(new ValueDataEntry(entry.getKey(),entry.getValue()));
                    }
                    pie.data(data);
                } else  {
                    binding.emptyState.setVisibility(View.VISIBLE);
                    binding.anyChart.setVisibility(View.GONE);
                }

            }
        });

        viewModel.getTransactions(calendar,SELECTED_STATS_TYPE);


//        data.add(new ValueDataEntry("Apples", 6371664));
//        data.add(new ValueDataEntry("Pears", 789622));
//        data.add(new ValueDataEntry("Bananas", 7216301));
//        data.add(new ValueDataEntry("Grapes", 1486621));
//        data.add(new ValueDataEntry("Oranges", 1200000));



//        pie.title("Fruits imported in 2015 (in kg)");
//
//        pie.labels().position("outside");
//
//        pie.legend().title().enabled(true);
//        pie.legend().title()
//                .text("Retail channels")
//                .padding(0d, 0d, 10d, 0d);
//
//        pie.legend()
//                .position("center-bottom")
//                .itemsLayout(LegendLayout.HORIZONTAL)
//                .align(Align.CENTER);

        binding.anyChart.setChart(pie);

        return binding.getRoot();
    }

    void updateDate(){
        // SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM,yyyy");

        if (Constants.SELECTED_TAB_STATS == Constants.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        }
        else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar,SELECTED_STATS_TYPE);
    }
}
