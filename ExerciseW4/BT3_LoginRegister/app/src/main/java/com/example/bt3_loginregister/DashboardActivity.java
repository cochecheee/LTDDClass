package com.example.bt3_loginregister;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt3_loginregister.adapter.CategoryAdapter;
import com.example.bt3_loginregister.adapter.ProductAdapter;
import com.example.bt3_loginregister.api.ApiService;
import com.example.bt3_loginregister.api.RetrofitClient;
import com.example.bt3_loginregister.model.Category;
import com.example.bt3_loginregister.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView productsRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Category> categoryList;
    private List<Product> productList;
    private TextView userName;
    private TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);

        // Set user information
        userName.setText("John Doe");
        userEmail.setText("john.doe@example.com");

        // Initialize data
        categoryList = new ArrayList<>();
        productList = new ArrayList<>();

        // Setup RecyclerViews
        setupCategoriesRecyclerView();
        setupProductsRecyclerView();


        // Load data from API
        loadCategories();
        loadLatestProducts();
    }



    private void setupCategoriesRecyclerView() {
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupProductsRecyclerView() {
        productAdapter = new ProductAdapter(this, productList);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productsRecyclerView.setAdapter(productAdapter);
    }

    private void loadCategories() {
        // Sử dụng try-catch để xử lý lỗi
        try {
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<List<Category>> call = apiService.getAllCategories();

            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        categoryList.clear();
                        categoryList.addAll(response.body());
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        // Hiển thị dữ liệu mẫu nếu API không thành công
                        loadDummyCategories();
                        Toast.makeText(DashboardActivity.this, "Cannot load categories from server. Using sample data.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    // Hiển thị dữ liệu mẫu trong trường hợp lỗi
                    loadDummyCategories();
                    Toast.makeText(DashboardActivity.this, "Failed to load categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            // Hiển thị dữ liệu mẫu nếu có lỗi
            loadDummyCategories();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLatestProducts() {
        try {
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<List<Product>> call = apiService.getLatestProducts();

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        productList.clear();
                        productList.addAll(response.body());
                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Hiển thị dữ liệu mẫu nếu API không thành công
                        loadDummyProducts();
                        Toast.makeText(DashboardActivity.this, "Cannot load products from server. Using sample data.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    // Hiển thị dữ liệu mẫu trong trường hợp lỗi
                    loadDummyProducts();
                    Toast.makeText(DashboardActivity.this, "Failed to load products: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            // Hiển thị dữ liệu mẫu nếu có lỗi
            loadDummyProducts();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức để nạp dữ liệu mẫu Category khi API không hoạt động
    private void loadDummyCategories() {
        categoryList.clear();
        categoryList.add(new Category(1, "Pizza", "https://cdn-icons-png.flaticon.com/128/3132/3132693.png"));
        categoryList.add(new Category(2, "Burger", "https://cdn-icons-png.flaticon.com/128/3075/3075977.png"));
        categoryList.add(new Category(3, "Hotdog", "https://cdn-icons-png.flaticon.com/128/3075/3075929.png"));
        categoryList.add(new Category(4, "Drink", "https://cdn-icons-png.flaticon.com/128/3081/3081065.png"));
        categoryList.add(new Category(5, "Donut", "https://cdn-icons-png.flaticon.com/128/3075/3075850.png"));
        categoryAdapter.notifyDataSetChanged();
    }

    // Phương thức để nạp dữ liệu mẫu Product khi API không hoạt động
    private void loadDummyProducts() {
        productList.clear();
        productList.add(new Product(1, "Pepperoni Pizza", "Delicious pizza with pepperoni", 8.99, "https://www.freepnglogos.com/uploads/pizza-png/pizza-slice-icon-download-icons-14.png", 1));
        productList.add(new Product(2, "Cheese Burger", "Tasty burger with cheese", 5.99, "https://www.freepnglogos.com/uploads/burger-png/burger-png-transparent-images-png-only-27.png", 2));
        productList.add(new Product(3, "Classic Hotdog", "Classic hotdog with ketchup", 4.49, "https://www.freepnglogos.com/uploads/hotdog-png/hotdog-transparent-images-download-clip-art-19.png", 3));
        productList.add(new Product(4, "Coca Cola", "Refreshing soft drink", 1.99, "https://www.freepnglogos.com/uploads/coca-cola-png/coca-cola-soda-soft-drink-34.png", 4));
        productList.add(new Product(5, "Glazed Donut", "Sweet glazed donut", 2.49, "https://www.freepnglogos.com/uploads/donut-png/donut-png-images-transparent-background-12.png", 5));
        productList.add(new Product(6, "Veggie Pizza", "Healthy pizza with vegetables", 9.99, "https://www.freepnglogos.com/uploads/pizza-png/pizza-images-download-pizza-17.png", 1));
        productAdapter.notifyDataSetChanged();
    }
}