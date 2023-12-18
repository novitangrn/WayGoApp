

    @FormUrlEncoded
    @POST("users/signin")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("users/signup")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @GET("users/{id}")
    suspend fun getDetailUser(
        @Path("id") id: String
    ): Response<DetailUserResponse>

    @FormUrlEncoded
    @PATCH("users/{id}")
    suspend fun updateDetailUser(
        @Path("id") id: String,
        @Field("fullname") fullname: String,
        @Field("picture") picture: String,
        @Field("alamat") alamat: String,
        @Field("telephone") telephone: Int
    ): Response<DetailUserResponse>

    @GET("recipes")
    suspend fun getAllRecipes(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): RecipeResponses

    @GET("recipes/{id}")
    suspend fun getDetailRecipes(
        @Path("id") id: String
    ): Response<DetailRecipesResponses>

    @GET("donations/closest/{lon}/{lat}")
    suspend fun getAllClosestDonation(
        @Path("lon") lon: Double,
        @Path("lat") lat: Double,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): ClosestDonationsResponses

    @GET("donations")
    suspend fun getAllDonation(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): DonationsResponses