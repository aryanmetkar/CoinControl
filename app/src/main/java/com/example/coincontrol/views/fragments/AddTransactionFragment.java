package com.example.coincontrol.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.coincontrol.R;
import com.example.coincontrol.adapters.AccountsAdapter;
import com.example.coincontrol.adapters.CatergoryAdapter;
import com.example.coincontrol.databinding.FragmentAddTransactionBinding;
import com.example.coincontrol.databinding.ListDialogBinding;
import com.example.coincontrol.models.Account;
import com.example.coincontrol.models.Category;
import com.example.coincontrol.models.Transaction;
import com.example.coincontrol.utils.Constants;
import com.example.coincontrol.utils.Helper;
import com.example.coincontrol.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

 private String mParam2;

    public AddTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAddTransactionBinding.inflate(inflater);

        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

                transaction.setType(Constants.INCOME);
            }
        });

        binding.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));

                transaction.setType(Constants.EXPENSE);
            }
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());

                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                        calendar.set(Calendar.MONTH,datePicker.getMonth());
                        calendar.set(Calendar.YEAR,datePicker.getYear());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM ,yyyy");
                        String dateToShow = Helper.formatDate(calendar.getTime());

                        binding.date.setText(dateToShow);

                        transaction.setDate(calendar.getTime());
                        transaction.setId(calendar.getTime().getTime());

                    }
                });
                datePickerDialog.show();
            }
        });

        binding.category.setOnClickListener(c-> {

            //Repetative unchanging text
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            ArrayList<Category> categories = new ArrayList<>();
            categories.add(new Category("Salary",R.drawable.ic_salary,R.color.category1));
            categories.add(new Category("Bussiness",R.drawable.ic_business,R.color.category2));
            categories.add(new Category("Investment",R.drawable.ic_investment,R.color.category3));
            categories.add(new Category("Loan",R.drawable.ic_loan,R.color.category4));
            categories.add(new Category("Rent",R.drawable.ic_rent,R.color.category5));
            categories.add(new Category("Rent",R.drawable.ic_other,R.color.category6));

            CatergoryAdapter catergoryAdapter = new CatergoryAdapter(getContext(), Constants.categories, new CatergoryAdapter.CategoryClickListner() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());

                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(catergoryAdapter);

            categoryDialog.show();


        });

        binding.account.setOnClickListener(c->{

            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(0,"Cash"));
            accounts.add(new Account(0,"Savings"));
            accounts.add(new Account(0,"Credit"));
            accounts.add(new Account(0,"UPI"));
            accounts.add(new Account(0,"Others"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListner() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());

                    transaction.setAccount(account.getAccountName());
                    accountsDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//   now         dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);

            accountsDialog.show();

        });

        binding.saveTransactionBtn.setOnClickListener(c->{
            double amount = Double.parseDouble(binding.amount.getText().toString());

            String note = binding.note.getText().toString();

            if (transaction.getType().equals(Constants.EXPENSE)){
                transaction.setAmount(amount*-1);
            }
            else{
                transaction.setAmount(amount);
            }

            transaction.setNote(note);

            ((MainActivity)getActivity()).viewModel.addTransaction(transaction);
            ((MainActivity)getActivity()).getTransactions();
            dismiss();
        });


        return binding.getRoot();
    }
}