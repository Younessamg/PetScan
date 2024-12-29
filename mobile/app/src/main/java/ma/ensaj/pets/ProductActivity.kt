package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensaj.pets.adapter.ProductAdapter
import ma.ensaj.pets.api.ProductApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val products = mutableListOf<Product>() // Liste mutable pour ajouter les produits récupérés

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Initialisation de l'adaptateur avec une fonction pour gérer le clic sur un produit
        adapter = ProductAdapter { product ->
            // Naviguer vers ProductDetailsActivity avec l'ID du produit
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id) // Passer l'ID du produit
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        loadProducts()
    }

    private fun loadProducts() {
        val productApi = RetrofitClient.instance.create(ProductApi::class.java)

        productApi.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    Log.d("product", "response.body()")
                    val productList = response.body()
                    if (productList != null) {
                        products.clear()
                        products.addAll(productList)
                        adapter.submitList(products.toList())
                    } else {
                        Toast.makeText(this@ProductActivity, "Aucun produit trouvé", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProductActivity, "Échec de la récupération des produits", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductActivity, "Erreur réseau : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
