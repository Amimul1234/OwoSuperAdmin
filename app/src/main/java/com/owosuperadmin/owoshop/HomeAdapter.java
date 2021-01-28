package com.owosuperadmin.owoshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.owosuperadmin.shop_related.confirm_shop_orders;

import static com.owosuperadmin.owoshop.HomeActivity.p;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.xyz>{

    private Context context;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public xyz onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.home_sample,viewGroup,false);
        return new  xyz(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final xyz holder, final int position) {

        holder.imageView.setImageResource(HomeActivity.icons[position]);
        holder.textView.setText(HomeActivity.segment[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position==0)
                {
                    Intent intent=new Intent(holder.itemView.getContext(), CreateNewAdminActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }

                else if (position==1)
                {
                    Intent intent=new Intent(holder.itemView.getContext(), SemiAdminActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
                else if (position==2)
                {
                    Intent intent=new Intent(holder.itemView.getContext(), ShopApprovalActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
                else if (position==3)
                {
                    CharSequence options[]=new CharSequence[]{"Shop Management","Shop Orders"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("SHOPS");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            if (i==0)
                            {
                                Intent intent=new Intent(holder.itemView.getContext(), ManagementActivity.class);
                                holder.itemView.getContext().startActivity(intent);

                            }
                            else if(i==1)
                            {
                                Intent intent = new Intent(holder.itemView.getContext(), confirm_shop_orders.class);
                                holder.itemView.getContext().startActivity(intent);
                            }
                        }
                    });
                    builder.show();

                }
                else if (position==4)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    View view = LayoutInflater.from(context).inflate(R.layout.custom_products_alert_dialog, null);

                    Button add_a_new_product = view.findViewById(R.id.add_a_new_product);
                    Button available_products = view.findViewById(R.id.available_products);

                    builder.setView(view);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    add_a_new_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(holder.itemView.getContext(), AddProductActivity.class);
                            holder.itemView.getContext().startActivity(intent);
                            alertDialog.cancel();
                        }
                    });

                    available_products.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(holder.itemView.getContext(), ProductAvailabilityActivity.class);
                            holder.itemView.getContext().startActivity(intent);
                            alertDialog.cancel();
                        }
                    });

                }

                else if (position==5)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    View view = LayoutInflater.from(context).inflate(R.layout.custom_offers_alert_dialog, null);

                    Button create_a_new_offer = view.findViewById(R.id.create_a_new_offer);
                    Button available_offers = view.findViewById(R.id.available_offers);

                    builder.setView(view);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    create_a_new_offer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(holder.itemView.getContext(), CreateOffersActivity.class);
                            holder.itemView.getContext().startActivity(intent);
                            alertDialog.cancel();
                        }
                    });

                    available_offers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(holder.itemView.getContext(), AvailableOffersActivity.class);
                            holder.itemView.getContext().startActivity(intent);
                            alertDialog.cancel();
                        }
                    });

                }
                else if (position==6)
                {

                }
                else if (position==7)
                {

                }
                else if (position==8)
                {
                    Intent intent=new Intent(holder.itemView.getContext(), CloudMessagingActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
                else if (position==9)
                {
                    Intent intent=new Intent(holder.itemView.getContext(), QuponActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
                else if(position == 10)
                {
                    Intent intent=new Intent(holder.itemView.getContext(), AddASubCategory.class);
                    holder.itemView.getContext().startActivity(intent);
                }
                else if(position == 11)
                {
                    Intent intent = new Intent(holder.itemView.getContext(), AddABrand.class);
                    holder.itemView.getContext().startActivity(intent);
                }
                else if(position == 12)
                {
                    Intent intent = new Intent(holder.itemView.getContext(), AddATimeSlot.class);
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return HomeActivity.segment.length;
    }


    public class xyz extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public xyz(@NonNull View itemView) {
            super(itemView);

            ViewGroup.LayoutParams abcd =  itemView.getLayoutParams();// Handling different screen sizes in home
            abcd.width = p/2-20;
            itemView.setLayoutParams(abcd);

            imageView = itemView.findViewById(R.id.imageviewid);
            imageView.setMinimumHeight(p/2-40);
            textView = itemView.findViewById(R.id.textviewid);
            textView.setMinimumHeight(40);
        }

    }

}

