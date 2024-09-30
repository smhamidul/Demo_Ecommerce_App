package com.hamidul.demoecommerceapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hamidul.demoecommerceapp.R;
import com.hamidul.demoecommerceapp.activities.ProductDetailActivity;
import com.hamidul.demoecommerceapp.databinding.ItemCartBinding;
import com.hamidul.demoecommerceapp.databinding.QuantityDialogBinding;
import com.hamidul.demoecommerceapp.model.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder>{

    Context context;
    ArrayList<Product> products;
    Toast toast;

    public CartAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new cartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);
        holder.binding.name.setText(product.getName());
        holder.binding.price.setText(String.format("BDT %.2f",product.getPrice()));
        holder.binding.quantity.setText(product.getQuantity() + " item(s)");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityDialogBinding.getRoot())
                        .create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                quantityDialogBinding.productName.setText(product.getName());
                quantityDialogBinding.productStock.setText("Stock : "+product.getStock());
                quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));

                int stock = product.getStock();

                quantityDialogBinding.negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int quantity = product.getQuantity();

                        if (quantity>1) quantity--;

                        product.setQuantity(quantity);
                        quantityDialogBinding.quantity.setText(String.valueOf(quantity));

                    }
                });

                quantityDialogBinding.positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int quantity = product.getQuantity();

                        quantity++;

                        if (quantity>product.getStock()){
                            setToast("Max stock available : "+product.getQuantity());
                            return;
                        }else {
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                        }

                    }
                });

                quantityDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class cartViewHolder extends RecyclerView.ViewHolder{


        ItemCartBinding binding;
        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }

    private void setToast (String message){
        if (toast!=null) toast.cancel();
        toast = Toast.makeText(context,message,Toast.LENGTH_LONG);
        toast.show();
    }


}
