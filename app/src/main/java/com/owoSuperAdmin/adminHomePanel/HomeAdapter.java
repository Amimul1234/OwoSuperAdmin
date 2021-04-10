package com.owoSuperAdmin.adminHomePanel;

import android.content.Context;
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

import com.owoSuperAdmin.adminManagement.AdminRegisterAdapter;
import com.owoSuperAdmin.adminManagement.AllAdmins;
import com.owoSuperAdmin.adminManagement.DeleteAdmin;
import com.owoSuperAdmin.adminManagement.UpdateAdminData;
import com.owoSuperAdmin.categoryManagement.brand.addBrand.AddABrand;
import com.owoSuperAdmin.categoryManagement.brand.updateBrand.AllCategories;
import com.owoSuperAdmin.categoryManagement.category.AddNewCategory;
import com.owoSuperAdmin.categoryManagement.category.DeleteExistentCategory;
import com.owoSuperAdmin.categoryManagement.category.UpdateExistentCategory;
import com.owoSuperAdmin.categoryManagement.subCategory.deleteSubCategory.DeleteSubCategoryAllCategories;
import com.owoSuperAdmin.categoryManagement.subCategory.updateSubCategory.UpdateSubCategoryAllCategories;
import com.owoSuperAdmin.owoshop.AddATimeSlot;
import com.owoSuperAdmin.productsManagement.addProduct.AddProductActivity;
import com.owoSuperAdmin.offersManagement.avilableOffers.AvailableOffersActivity;
import com.owoSuperAdmin.owoshop.CloudMessagingActivity;
import com.owoSuperAdmin.adminManagement.CreateNewAdminActivity;
import com.owoSuperAdmin.offersManagement.addNewOffer.CreateOffersActivity;
import com.owoSuperAdmin.shopManagement.allRegisteredShops.ManageRegisteredShops;
import com.owoSuperAdmin.productsManagement.allProducts.AvailableProducts;
import com.owoSuperAdmin.offersManagement.QuponActivity;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.shopManagement.approveShop.ShopCreationRequestsActivity;
import com.owoSuperAdmin.categoryManagement.subCategory.addSubCategory.AddASubCategory;
import com.owoSuperAdmin.ordersManagement.confirm_shop_orders;
import com.owoSuperAdmin.userManagement.shopKeeperUser.allShopKeepers.AllEnabledShopKeepers;
import com.owoSuperAdmin.userManagement.shopKeeperUser.disabledShopKeepers.AllDisabledShopKeepers;

import org.jetbrains.annotations.NotNull;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.xyz>{

    private final Context context;
    private final String[] segment;
    private final int[] icons;

    public HomeAdapter(Context context) {
        this.context = context;
        segment = context.getResources().getStringArray(R.array.adminPanelItems);

        icons = new int[] {R.drawable.admin_management, R.drawable.shop_management, R.drawable.product_management,
            R.drawable.offer_management, R.drawable.category_management, R.drawable.user_management,
            R.drawable.order_management};
    }

    @NotNull
    @Override
    public xyz onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_sample,viewGroup,false);
        return new  xyz(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final xyz holder, final int position) {
        holder.imageView.setImageResource(icons[position]);
        holder.textView.setText(segment[position]);
    }

    @Override
    public int getItemCount() {
        return segment.length;
    }


    public class xyz extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public xyz(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.homeElementImageView);
            textView = itemView.findViewById(R.id.homeElementTextView);

            itemView.setOnClickListener(v -> takeNecessaryActions(getAdapterPosition()));
        }
    }

    private void takeNecessaryActions(int position) {
        if (position==0) //Admin management
        {
            CharSequence[] adminOptions = new CharSequence[] {"Register a new admin","All Admins"};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Admin Management");

            builder.setItems(adminOptions, (dialog, i) -> {
                switch (i)
                {
                    case 0:
                    {
                        Intent intent = new Intent(context, CreateNewAdminActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        Intent intent = new Intent(context, AllAdmins.class);
                        context.startActivity(intent);
                        break;
                    }
                }
            });

            builder.show();
        }
        else if (position == 1) //Shops management
        {
            CharSequence[] options=new CharSequence[] {"Manage Registered Shops","Approve A New Shop",
                    "Manage Blocked Shops", "Category Extension Requests"};

            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Shop Management");

            builder.setItems(options, (dialog, i) -> {
                switch (i)
                {
                    case 0:
                    {
                        Intent intent=new Intent(context, ManageRegisteredShops.class);
                        context.startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        Intent intent = new Intent(context, ShopCreationRequestsActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case 2:
                    {
                        Intent intent = new Intent(context, confirm_shop_orders.class);
                        context.startActivity(intent);
                        break;
                    }
                }
            });
            builder.show();

        }
        else if (position==2)//Products management
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            View view = LayoutInflater.from(context).inflate(R.layout.custom_products_alert_dialog, null);

            Button add_a_new_product = view.findViewById(R.id.add_a_new_product);
            Button available_products = view.findViewById(R.id.available_products);

            builder.setView(view);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            add_a_new_product.setOnClickListener(v -> {
                Intent intent=new Intent(context, AddProductActivity.class);
                context.startActivity(intent);
                alertDialog.cancel();
            });

            available_products.setOnClickListener(v -> {
                Intent intent=new Intent(context, AvailableProducts.class);
                context.startActivity(intent);
                alertDialog.cancel();
            });
        }

        else if (position==3) //Offers management
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            View view = LayoutInflater.from(context).inflate(R.layout.custom_offers_alert_dialog, null);

            Button create_a_new_offer = view.findViewById(R.id.create_a_new_offer);
            Button available_offers = view.findViewById(R.id.available_offers);

            builder.setView(view);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            create_a_new_offer.setOnClickListener(v -> {
                Intent intent=new Intent(context, CreateOffersActivity.class);
                context.startActivity(intent);
                alertDialog.cancel();
            });

            available_offers.setOnClickListener(v -> {
                Intent intent=new Intent(context, AvailableOffersActivity.class);
                context.startActivity(intent);
                alertDialog.cancel();
            });

        }
        else if (position==4)//Category and sub category management
        {
            CharSequence[] options = new CharSequence[]{"Manage Category", "Manage Sub Categories", "Manage Brands"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Category Management");

            builder.setItems(options, (dialog, i) -> {
                if (i==0)
                {
                    CharSequence[] categoryOptions = new CharSequence[]{"Create a new category", "Update existent category",
                        "Delete existent category"};

                    AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(context);
                    categoryBuilder.setTitle("Category Management");

                    categoryBuilder.setItems(categoryOptions, ((dialog1, which) -> {
                        if(which == 0)
                        {
                            Intent intent=new Intent(context, AddNewCategory.class);
                            context.startActivity(intent);
                        }
                        else if(which == 1)
                        {
                            Intent intent = new Intent(context, UpdateExistentCategory.class);
                            context.startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(context, DeleteExistentCategory.class);
                            context.startActivity(intent);
                        }
                    }));

                    categoryBuilder.show();

                }
                else if(i==1)
                {
                    CharSequence[] subCategoryOptions = new CharSequence[]{"Create a new sub-category",
                            "Update existent sub-category", "Delete existent sub-category"};

                    AlertDialog.Builder subCategoryBuilder = new AlertDialog.Builder(context);
                    subCategoryBuilder.setTitle("Sub Category Management");

                    subCategoryBuilder.setItems(subCategoryOptions, ((dialog1, which) -> {
                        if(which == 0)
                        {
                            Intent intent=new Intent(context, AddASubCategory.class);
                            context.startActivity(intent);
                        }
                        else if(which == 1)
                        {
                            Intent intent = new Intent(context, UpdateSubCategoryAllCategories.class);
                            context.startActivity(intent);
                        }
                        else if(which == 2)
                        {
                            Intent intent = new Intent(context, DeleteSubCategoryAllCategories.class);
                            context.startActivity(intent);
                        }

                    }));

                    subCategoryBuilder.show();
                }
                else if(i==2)
                {
                    CharSequence[] brandsOptions = new CharSequence[]{"Create a new brand",
                            "Update existent brand", "Delete existent brand"};

                    AlertDialog.Builder brandsBuilder = new AlertDialog.Builder(context);
                    brandsBuilder.setTitle("Brands Management");

                    brandsBuilder.setItems(brandsOptions, ((dialog1, which) -> {
                        if(which == 0)
                        {
                            Intent intent=new Intent(context, AddABrand.class);
                            context.startActivity(intent);
                        }
                        else if(which == 1)
                        {
                            Intent intent = new Intent(context, AllCategories.class);
                            intent.putExtra("indicate", "update");
                            context.startActivity(intent);
                        }
                        else if(which == 2)
                        {
                            Intent intent = new Intent(context, AllCategories.class);
                            intent.putExtra("indicate", "delete");
                            context.startActivity(intent);
                        }
                    }));

                    brandsBuilder.show();
                }
            });

            builder.show();
        }

        else if(position == 5)//Users management
        {
            CharSequence[] brandsOptions = new CharSequence[]{"All Enabled Shop Keepers",
                    "Disabled Shop Keepers"};

            AlertDialog.Builder brandsBuilder = new AlertDialog.Builder(context);
            brandsBuilder.setTitle("Shop Keeper Management");

            brandsBuilder.setItems(brandsOptions, ((dialog1, which) -> {
                if(which == 0)
                {
                    Intent intent = new Intent(context, AllEnabledShopKeepers.class);
                    context.startActivity(intent);
                }
                else if(which == 1)
                {
                    Intent intent = new Intent(context, AllDisabledShopKeepers.class);
                    context.startActivity(intent);
                }
            }));

            brandsBuilder.show();
        }

        else if (position==6)//Shop Orders management
        {
            Intent intent = new Intent(context, confirm_shop_orders.class);
            context.startActivity(intent);
        }
        else if (position == 8)
        {
            Intent intent=new Intent(context, CloudMessagingActivity.class);
            context.startActivity(intent);
        }
        else if (position == 9)
        {
            Intent intent=new Intent(context, QuponActivity.class);
            context.startActivity(intent);
        }
        else if(position == 12)
        {
            Intent intent = new Intent(context, AddATimeSlot.class);
            context.startActivity(intent);
        }
    }
}

