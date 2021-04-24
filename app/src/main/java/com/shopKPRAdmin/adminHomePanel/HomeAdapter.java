package com.shopKPRAdmin.adminHomePanel;

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
import com.shopKPRAdmin.adminManagement.AllAdmins;
import com.shopKPRAdmin.categoryManagement.brand.addBrand.AddABrand;
import com.shopKPRAdmin.categoryManagement.brand.updateBrand.AllCategories;
import com.shopKPRAdmin.categoryManagement.category.AddNewCategory;
import com.shopKPRAdmin.categoryManagement.category.DeleteExistentCategory;
import com.shopKPRAdmin.categoryManagement.category.UpdateExistentCategory;
import com.shopKPRAdmin.categoryManagement.subCategory.deleteSubCategory.DeleteSubCategoryAllCategories;
import com.shopKPRAdmin.categoryManagement.subCategory.updateSubCategory.UpdateSubCategoryAllCategories;
import com.shopKPRAdmin.giftAndDeals.deals.AllDealsActivity;
import com.shopKPRAdmin.giftAndDeals.deals.CreateDeal;
import com.shopKPRAdmin.giftAndDeals.gifts.AllGifts;
import com.shopKPRAdmin.giftAndDeals.gifts.CreateNewGift;
import com.shopKPRAdmin.login.AdminCredentials;
import com.shopKPRAdmin.offersManagement.QuponActivity;
import com.shopKPRAdmin.productsManagement.stockedOutProducts.StockedOutProducts;
import com.shopKPRAdmin.timeSlot.AddATimeSlot;
import com.shopKPRAdmin.pushNotification.CloudMessagingActivity;
import com.shopKPRAdmin.productsManagement.addProduct.AddProductActivity;
import com.shopKPRAdmin.offersManagement.avilableOffers.AvailableOffersActivity;
import com.shopKPRAdmin.adminManagement.CreateNewAdminActivity;
import com.shopKPRAdmin.offersManagement.addNewOffer.CreateOffersActivity;
import com.shopKPRAdmin.shopManagement.allRegisteredShops.ManageRegisteredShops;
import com.shopKPRAdmin.productsManagement.allProducts.AvailableProducts;
import com.shopKPRAdmin.owoshop.R;
import com.shopKPRAdmin.shopManagement.approveShop.ShopCreationRequestsActivity;
import com.shopKPRAdmin.categoryManagement.subCategory.addSubCategory.AddASubCategory;
import com.shopKPRAdmin.orderManagement.ShopOrderManagement;
import com.shopKPRAdmin.timeSlot.AllAvailableTimeSlots;
import com.shopKPRAdmin.userManagement.shopKeeperUser.allShopKeepers.AllEnabledShopKeepers;
import com.shopKPRAdmin.userManagement.shopKeeperUser.disabledShopKeepers.AllDisabledShopKeepers;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.xyz>{

    private final Context context;

    private final List<String> segment = new ArrayList<>();
    private final List<Integer> icons = new ArrayList<>();

    public HomeAdapter(Context context)
    {
        this.context = context;
        segment.addAll(AdminCredentials.adminPermissionList);

        for(int i=0; i<segment.size(); i++)
        {
            switch (segment.get(i)) {
                case "Admin Management":
                    icons.add(R.drawable.admin_management);
                    break;
                case "Shop Management":
                    icons.add(R.drawable.shop_management);
                    break;
                case "Product Management":
                    icons.add(R.drawable.product_management);
                    break;
                case "Banner Management":
                    icons.add(R.drawable.offer_management);
                    break;
                case "Category Management":
                    icons.add(R.drawable.category_management);
                    break;
                case "User Management":
                    icons.add(R.drawable.user_management);
                    break;
                case "Order Management":
                    icons.add(R.drawable.order_management);
                    break;
                case "Gift and Deal Management":
                    icons.add(R.drawable.gift);
                    break;
            }
        }
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
        holder.imageView.setImageResource(icons.get(position));
        holder.textView.setText(segment.get(position));
    }

    @Override
    public int getItemCount() {
        return segment.size();
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
        switch (segment.get(position))
        {
            case "Admin Management":
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
                break;
            }
            case "Shop Management":
            {
                CharSequence[] options=new CharSequence[] {"Manage Registered Shops","Approve A New Shop", "Category Extension Requests"};

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
                            Intent intent = new Intent(context, ShopOrderManagement.class);
                            context.startActivity(intent);
                            break;
                        }
                    }
                });

                builder.show();
                break;
            }

            case "Product Management":
            {

                CharSequence[] options=new CharSequence[] {"Add New Product", "All Products", "Stocked Out Products"};

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Products Management");

                builder.setItems(options, (dialog, i) -> {
                    switch (i)
                    {
                        case 0:
                        {
                            Intent intent=new Intent(context, AddProductActivity.class);
                            context.startActivity(intent);
                            break;
                        }
                        case 1:
                        {
                            Intent intent=new Intent(context, AvailableProducts.class);
                            context.startActivity(intent);
                            break;
                        }
                        case 2:
                        {
                            Intent intent = new Intent(context, StockedOutProducts.class);
                            context.startActivity(intent);
                            break;
                        }
                    }
                });

                builder.show();
                break;
            }

            case "Banner Management":
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
                break;
            }
            case "Category Management":
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
                break;
            }

            case "User Management":
            {
                CharSequence[] brandsOptions = new CharSequence[]{"All Enabled Shop Keepers",
                        "Disabled Shop Keepers", "Send Notification"};

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
                    else if(which == 2)
                    {
                        Intent intent=new Intent(context, CloudMessagingActivity.class);
                        context.startActivity(intent);
                    }
                }));

                brandsBuilder.show();

                break;
            }

            case "Order Management":
            {
                CharSequence[] brandsOptions = new CharSequence[]{"Manage Shop Order",
                        "Manage Time Slots", "Manage Qupon"};

                AlertDialog.Builder brandsBuilder = new AlertDialog.Builder(context);
                brandsBuilder.setTitle("Shop Order Management");

                brandsBuilder.setItems(brandsOptions, ((dialog1, which) -> {
                    if(which == 0)
                    {
                        Intent intent = new Intent(context, ShopOrderManagement.class);
                        context.startActivity(intent);
                    }
                    else if(which == 1)
                    {

                        CharSequence[] timSlotsOptions = new CharSequence[]{"Add New Time Slot",
                                "All Available Time Slots"};

                        AlertDialog.Builder timeSlotsOptionBuilder = new AlertDialog.Builder(context);
                        timeSlotsOptionBuilder.setTitle("Time Slots Management");

                        timeSlotsOptionBuilder.setItems(timSlotsOptions, ((dialog, which1) -> {
                            if(which1 == 0)
                            {
                                Intent intent = new Intent(context, AddATimeSlot.class);
                                context.startActivity(intent);
                            }
                            else if(which1 == 1)
                            {
                                Intent intent = new Intent(context, AllAvailableTimeSlots.class);
                                context.startActivity(intent);
                            }
                        }));

                        timeSlotsOptionBuilder.show();
                    }
                    else if(which == 2)
                    {
                        Intent intent=new Intent(context, QuponActivity.class);
                        context.startActivity(intent);
                    }
                }));

                brandsBuilder.show();
                break;
            }

            case "Gift and Deal Management": {

                CharSequence[] giftAndDealOptions = new CharSequence[]{"Manage gift cards",
                        "Manage Deals", "Manage Offers"};

                AlertDialog.Builder giftAndDealBuilder = new AlertDialog.Builder(context);
                giftAndDealBuilder.setTitle("Gift and Deal Management");

                giftAndDealBuilder.setItems(giftAndDealOptions, ((dialog1, which) ->
                {
                    if(which == 0)
                    {
                        CharSequence[] giftsOptions = new CharSequence[]{"Add new gift card",
                                "All Gift Cards"};

                        AlertDialog.Builder giftsBuilder = new AlertDialog.Builder(context);
                        giftsBuilder.setTitle("Gift Management");

                        giftsBuilder.setItems(giftsOptions, ((dialog, which1) -> {

                            Intent intent;

                            if(which1 == 0)
                            {
                                intent = new Intent(context, CreateNewGift.class);
                            }
                            else
                            {
                                intent = new Intent(context, AllGifts.class);
                            }

                            context.startActivity(intent);

                        }));

                        giftsBuilder.show();

                    }
                    else if(which == 1)
                    {

                        CharSequence[] dealsOption = new CharSequence[]{"Add New Deal",
                                "All Deals"};

                        AlertDialog.Builder dealsBuilder = new AlertDialog.Builder(context);
                        dealsBuilder.setTitle("Deal Management");

                        dealsBuilder.setItems(dealsOption, ((dialog, which1) -> {

                            Intent intent;

                            if(which1 == 0)
                            {
                                intent = new Intent(context, CreateDeal.class);
                            }
                            else
                            {
                                intent = new Intent(context, AllDealsActivity.class);
                            }

                            context.startActivity(intent);

                        }));

                        dealsBuilder.show();
                    }
                    else if(which == 2)
                    {
                        Intent intent = new Intent(context, AllCategories.class);
                        intent.putExtra("indicate", "delete");
                        context.startActivity(intent);
                    }
                }));

                giftAndDealBuilder.show();

                break;
            }
        }
    }
}

