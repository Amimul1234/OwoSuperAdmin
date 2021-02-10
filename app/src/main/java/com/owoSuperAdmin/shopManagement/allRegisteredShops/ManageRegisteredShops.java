package com.owoSuperAdmin.shopManagement.allRegisteredShops;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.owoSuperAdmin.shopManagement.PendingShopViewHolderForAdmin;
import com.owoSuperAdmin.shopManagement.RegisteredShop;
import com.owoSuperAdmin.shopManagement.approveShop.entities.PendingShop;
import com.owoSuperAdmin.owoshop.R;

public class ManageRegisteredShops extends AppCompatActivity {

    FirebaseRecyclerPagingAdapter<PendingShop, PendingShopViewHolderForAdmin> adapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        setTitle("Shops");

        recyclerView=findViewById(R.id.management_recyclerviewid);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Query baseQuery = databaseReference.child("approvedShops");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<PendingShop> options = new DatabasePagingOptions.Builder<PendingShop>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, PendingShop.class)
                .build();


        adapter = new FirebaseRecyclerPagingAdapter<PendingShop, PendingShopViewHolderForAdmin>(options) {
                    @NonNull
                    @Override
                    public PendingShopViewHolderForAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = getLayoutInflater().inflate(R.layout.shops_sample_view, parent, false);
                        return new PendingShopViewHolderForAdmin(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull PendingShopViewHolderForAdmin holder, int position, @NonNull PendingShop model) {
                        holder.bind(model, ManageRegisteredShops.this);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ManageRegisteredShops.this, RegisteredShop.class);
                                intent.putExtra("approvedShop", model);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull LoadingState state) {

                    }
                };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
