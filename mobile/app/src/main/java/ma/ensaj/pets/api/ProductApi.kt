package ma.ensaj.pets.api

import ma.ensaj.pets.dto.Product
import ma.ensaj.pets.dto.ProductCategory
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.http.Part
import retrofit2.http.Multipart
import retrofit2.http.GET
import okhttp3.RequestBody

interface ProductApi {

    // Créer un produit avec une image
    @Multipart
    @POST("products")
    fun createProduct(
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stockQuantity") stockQuantity: RequestBody,
        @Part("category") category: ProductCategory,
        @Part("brand") brand: RequestBody
    ): Call<Product>

    // Récupérer tous les produits
    @GET("products")
    fun getAllProducts(): Call<List<Product>>

    // Récupérer un produit par son ID
    @GET("products/{id}")
    fun getProductById(
        @Path("id") id: Long
    ): Call<Product>

    // Récupérer les produits par catégorie
    @GET("products/category/{category}")
    fun getProductsByCategory(
        @Path("category") category: ProductCategory
    ): Call<List<Product>>

    // Rechercher des produits par nom
    @GET("products/search")
    fun searchProducts(
        @Query("name") name: String
    ): Call<List<Product>>

    // Récupérer l'image d'un produit par le nom de fichier
    @GET("products/image/{filename}")
    fun getImage(
        @Path("filename") filename: String
    ): Call<ResponseBody>

    // Mettre à jour un produit
    @Multipart
    @PUT("products/{id}")
    fun updateProduct(
        @Path("id") id: Long,
        @Part file: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stockQuantity") stockQuantity: RequestBody,
        @Part("category") category: ProductCategory,
        @Part("brand") brand: RequestBody
    ): Call<Product>

    // Supprimer un produit
    @DELETE("products/{id}")
    fun deleteProduct(
        @Path("id") id: Long
    ): Call<Void>
}
