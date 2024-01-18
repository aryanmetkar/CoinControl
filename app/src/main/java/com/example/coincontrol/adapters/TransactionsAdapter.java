package com.example.coincontrol.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coincontrol.R;
import com.example.coincontrol.databinding.RowTransactionBinding;
import com.example.coincontrol.models.Category;
import com.example.coincontrol.models.Transaction;
import com.example.coincontrol.utils.Constants;
import com.example.coincontrol.utils.Helper;
import com.example.coincontrol.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{

    Context context;
//    ArrayList<Transaction> transactions;
    RealmResults<Transaction> transactions;

    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.binding.trasnsactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.accountLbl.setText(transaction.getAccount());

        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

        holder.binding.categoryicon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.categoryicon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));

        if (transaction.getType().equals(Constants.INCOME)) {
            holder.binding.trasnsactionAmount.setTextColor(context.getColor(R.color.greenColor));
        }
        else if (transaction.getType().equals(Constants.EXPENSE)){
            holder.binding.trasnsactionAmount.setTextColor(context.getColor(R.color.redColor));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are you sure to delete this Transaction");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity)context).viewModel.deleteTransaction(transaction);
                    }
                });

                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDialog.dismiss();
                    }
                });
                deleteDialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        RowTransactionBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
