package ma.ensaj.pets

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ma.ensaj.pets.api.ProductApi
import ma.ensaj.pets.dto.Product
import ma.ensaj.pets.config.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productDescription: TextView
    private lateinit var productPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productImage = findViewById(R.id.productImage)
        productName = findViewById(R.id.productName)
        productDescription = findViewById(R.id.productDescription)
        productPrice = findViewById(R.id.productPrice)

        val productId = intent.getLongExtra("PRODUCT_ID", -1)
        fetchProductDetails(productId)
    }

    private fun fetchProductDetails(productId: Long) {
        val productApi = RetrofitClient.instance.create(ProductApi::class.java)

        productApi.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        productName.text = it.name
                        productDescription.text = it.description
                        productPrice.text = "Price: \$${it.price}"

                        Glide.with(this@ProductDetailsActivity)
                            .load(it.imageUrl) // URL complète de l'image
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(productImage)
                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                // Gérer l'échec
            }
        })
    }
}
