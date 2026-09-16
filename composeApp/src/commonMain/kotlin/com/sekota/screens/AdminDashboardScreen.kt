package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.usecase.*

val InkNavy = Color(0xFF0D1F2D)
val BrandTeal = Color(0xFF00B5C8)

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getBooksUseCase = remember { GetAdminBooksUseCase(repository) }
    val saveBookUseCase = remember { SaveAdminBookUseCase(repository) }
    val deleteBookUseCase = remember { DeleteAdminBookUseCase(repository) }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    val saveProductUseCase = remember { SaveAdminProductUseCase(repository) }
    val deleteProductUseCase = remember { DeleteAdminProductUseCase(repository) }
    val getMerchUseCase = remember { GetAdminMerchUseCase(repository) }
    val saveMerchUseCase = remember { SaveAdminMerchUseCase(repository) }
    val deleteMerchUseCase = remember { DeleteAdminMerchUseCase(repository) }
    val getLiveMetricsUseCase = remember { GetAdminLiveMetricsUseCase(repository) }
    val saveLiveMetricsUseCase = remember { SaveAdminLiveMetricsUseCase(repository) }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Books Registry", "Intelligence Suite", "Merchandise", "Live Metrics")
    val icons = listOf("📝", "🛠", "🛒", "ℹ️")

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        val isCompact = maxWidth < 700.dp

        if (isCompact) {
            // Mobile (Android Phone) Layout: Top AppBar + Content + Bottom Navigation Bar
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = InkNavy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SEKOTA CMS",
                            color = BrandTeal,
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onLogout) {
                            Text(text = "🚪", fontSize = 18.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    when (selectedTab) {
                        0 -> BooksRegistryTab(getBooksUseCase, saveBookUseCase, deleteBookUseCase)
                        1 -> IntelligenceSuiteTab(getProductsUseCase, saveProductUseCase, deleteProductUseCase)
                        2 -> MerchandiseTab(getMerchUseCase, saveMerchUseCase, deleteMerchUseCase)
                        3 -> LiveMetricsTab(getLiveMetricsUseCase, saveLiveMetricsUseCase)
                    }
                }

                NavigationBar(
                    containerColor = InkNavy,
                    contentColor = Color.White
                ) {
                    tabs.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Text(text = icons[index], fontSize = 20.sp) },
                            label = {
                                Text(
                                    text = title.split(" ").first(),
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 11.sp,
                                    color = if (selectedTab == index) BrandTeal else Color.LightGray
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrandTeal,
                                selectedTextColor = BrandTeal,
                                indicatorColor = InkNavy.copy(alpha = 0.6f),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        } else {
            // Desktop / Tablet Layout: Left Navigation Rail + Right Content Workbench
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    modifier = Modifier.width(250.dp).fillMaxHeight(),
                    containerColor = InkNavy,
                    contentColor = Color.White
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "ADMIN PANEL",
                        color = BrandTeal,
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    tabs.forEachIndexed { index, title ->
                        NavigationRailItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Text(text = icons[index], fontSize = 24.sp) },
                            label = { 
                                Text(
                                    text = title, 
                                    fontFamily = getDmSansFontFamily(), 
                                    color = if (selectedTab == index) BrandTeal else Color.LightGray
                                ) 
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = BrandTeal,
                                selectedTextColor = BrandTeal,
                                indicatorColor = InkNavy.copy(alpha = 0.5f),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = onLogout,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = "🚪 Sign Out",
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 13.sp,
                            color = Color(0xFFFF8A80)
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(32.dp)) {
                    when (selectedTab) {
                        0 -> BooksRegistryTab(getBooksUseCase, saveBookUseCase, deleteBookUseCase)
                        1 -> IntelligenceSuiteTab(getProductsUseCase, saveProductUseCase, deleteProductUseCase)
                        2 -> MerchandiseTab(getMerchUseCase, saveMerchUseCase, deleteMerchUseCase)
                        3 -> LiveMetricsTab(getLiveMetricsUseCase, saveLiveMetricsUseCase)
                    }
                }
            }
        }
    }
}

@Composable
fun BooksRegistryTab(
    getBooksUseCase: GetAdminBooksUseCase,
    saveBookUseCase: SaveAdminBookUseCase,
    deleteBookUseCase: DeleteAdminBookUseCase
) {
    var books by remember { mutableStateOf<List<AdminBook>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingBookId by remember { mutableStateOf<String?>(null) }
    var bookTitle by remember { mutableStateOf("") }
    var bookAuthor by remember { mutableStateOf("") }
    var bookIsbn by remember { mutableStateOf("") }
    var bookCoverImage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        books = getBooksUseCase()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = if (editingBookId == null) "Tambah Naskah Buku Baru" else "Edit Data Buku",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = InkNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    errorMessage?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp)
                    }
                    OutlinedTextField(
                        value = bookTitle,
                        onValueChange = { bookTitle = it; errorMessage = null },
                        label = { Text("Judul Buku", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = bookAuthor,
                        onValueChange = { bookAuthor = it; errorMessage = null },
                        label = { Text("Penulis", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = bookIsbn,
                        onValueChange = { bookIsbn = it; errorMessage = null },
                        label = { Text("ISBN", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = bookCoverImage,
                        onValueChange = { bookCoverImage = it; errorMessage = null },
                        label = { Text("Cover Image (URL atau nama file)", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. /images/cover.jpg atau https://...") }
                    )

                    // Preview thumbnail box for cover
                    if (bookCoverImage.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp, 52.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = BrandTeal.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🖼️", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Cover Preview Ready", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = InkNavy)
                                Text(bookCoverImage, fontFamily = getDmSansFontFamily(), fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val id = editingBookId ?: bookTitle.lowercase().replace(" ", "-").replace(Regex("[^a-z0-9-]"), "").ifBlank { "b-${kotlin.random.Random.nextInt(1000, 9999)}" }
                            val cover = bookCoverImage.trim().ifBlank { null }
                            val result = saveBookUseCase(AdminBook(id, bookTitle.trim(), bookAuthor.trim(), bookIsbn.trim(), cover))
                            if (result.isSuccess) {
                                books = getBooksUseCase()
                                showDialog = false
                                editingBookId = null
                                bookTitle = ""
                                bookAuthor = ""
                                bookIsbn = ""
                                bookCoverImage = ""
                                errorMessage = null
                            } else {
                                errorMessage = result.exceptionOrNull()?.message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(if (editingBookId == null) "Simpan" else "Perbarui", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; editingBookId = null }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(), 
            horizontalArrangement = Arrangement.SpaceBetween, 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Books Registry", 
                    fontFamily = getMontserratFontFamily(), 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 32.sp,
                    color = InkNavy
                )
                Text(
                    text = "Kurasi naskah e-book dan publikasi resmi Sekota (${books.size} terdaftar)",
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = { 
                editingBookId = null
                bookTitle = ""
                bookAuthor = ""
                bookIsbn = ""
                bookCoverImage = ""
                errorMessage = null
                showDialog = true 
            },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(50)
            ) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Book", fontFamily = getDmSansFontFamily())
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(books, key = { it.id }) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                modifier = Modifier.size(60.dp, 80.dp),
                                color = if (book.coverImage != null) BrandTeal.copy(alpha = 0.15f) else InkNavy.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (book.coverImage != null) "🖼️" else "📖", fontSize = 24.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    book.title, 
                                    fontFamily = getMontserratFontFamily(), 
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 16.sp,
                                    color = InkNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Penulis: ${book.author}  •  ISBN: ${book.isbn}", 
                                    fontFamily = getDmSansFontFamily(), 
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "ID: ${book.id}", 
                                        fontFamily = getDmSansFontFamily(), 
                                        color = BrandTeal,
                                        fontSize = 11.sp
                                    )
                                    if (book.coverImage != null) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "• Cover Set", 
                                            fontFamily = getDmSansFontFamily(), 
                                            color = Color(0xFF60BD65),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                        Row {
                            TextButton(
                                onClick = {
                                    editingBookId = book.id
                                    bookTitle = book.title
                                    bookAuthor = book.author
                                    bookIsbn = book.isbn
                                    bookCoverImage = book.coverImage ?: ""
                                    errorMessage = null
                                    showDialog = true
                                }
                            ) { 
                                Text("✏️ Edit", color = BrandTeal, fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold) 
                            }
                            TextButton(
                                onClick = {
                                    books = books.filter { it.id != book.id }
                                    scope.launch {
                                        deleteBookUseCase(book.id)
                                    }
                                }
                            ) { 
                                Text("🗑 Delete", color = Color(0xFFE53E3E), fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold) 
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IntelligenceSuiteTab(
    getProductsUseCase: GetAdminProductsUseCase,
    saveProductUseCase: SaveAdminProductUseCase,
    deleteProductUseCase: DeleteAdminProductUseCase
) {
    var products by remember { mutableStateOf<List<AdminProduct>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<AdminProduct?>(null) }
    var prodCode by remember { mutableStateOf("") }
    var prodName by remember { mutableStateOf("") }
    var prodCategory by remember { mutableStateOf("Intelligence Suite") }
    var prodDesc by remember { mutableStateOf("") }
    var prodFeaturesText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        products = getProductsUseCase()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; editingProduct = null },
            title = {
                Text(
                    text = if (editingProduct == null) "Tambah Produk Intelligence Suite" else "Edit Modul: ${editingProduct?.name}",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = InkNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    errorMessage?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = prodCode,
                            onValueChange = { prodCode = it; errorMessage = null },
                            label = { Text("Kode (e.g. VRD, ASC)") },
                            singleLine = true,
                            modifier = Modifier.weight(0.4f)
                        )
                        OutlinedTextField(
                            value = prodName,
                            onValueChange = { prodName = it; errorMessage = null },
                            label = { Text("Nama Produk") },
                            singleLine = true,
                            modifier = Modifier.weight(0.6f)
                        )
                    }
                    OutlinedTextField(
                        value = prodCategory,
                        onValueChange = { prodCategory = it; errorMessage = null },
                        label = { Text("Kategori / Eyebrow") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodDesc,
                        onValueChange = { prodDesc = it; errorMessage = null },
                        label = { Text("Deskripsi Editorial") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodFeaturesText,
                        onValueChange = { prodFeaturesText = it; errorMessage = null },
                        label = { Text("Core Modules / Features (pisahkan dengan koma)") },
                        placeholder = { Text("e.g. Sentiment Analytics, ESG Metric Tracker") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (prodName.isBlank()) {
                            errorMessage = "Nama produk tidak boleh kosong"
                            return@Button
                        }
                        if (prodCode.isBlank()) {
                            errorMessage = "Kode produk tidak boleh kosong"
                            return@Button
                        }
                        scope.launch {
                            val id = editingProduct?.id ?: prodCode.lowercase().trim().ifBlank { "prod-${kotlin.random.Random.nextInt(1000, 9999)}" }
                            val features = prodFeaturesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            val productToSave = AdminProduct(
                                id = id,
                                code = prodCode.trim().uppercase(),
                                name = prodName.trim(),
                                categoryEyebrow = prodCategory.trim().ifBlank { "Intelligence Suite" },
                                description = prodDesc.trim(),
                                features = if (features.isNotEmpty()) features else listOf("Core Intelligence")
                            )
                            val result = saveProductUseCase(productToSave)
                            if (result.isSuccess) {
                                products = getProductsUseCase()
                                showDialog = false
                                editingProduct = null
                                prodCode = ""
                                prodName = ""
                                prodDesc = ""
                                prodFeaturesText = ""
                                errorMessage = null
                            } else {
                                errorMessage = result.exceptionOrNull()?.message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
                ) {
                    Text(if (editingProduct == null) "Simpan" else "Perbarui", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; editingProduct = null }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(), 
            horizontalArrangement = Arrangement.SpaceBetween, 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Intelligence Suite", 
                    fontFamily = getMontserratFontFamily(), 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 32.sp,
                    color = InkNavy
                )
                Text(
                    text = "Kelola produk kecerdasan bisnis Sekota (${products.size} modul terdaftar)",
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = { 
                    editingProduct = null
                    prodCode = ""
                    prodName = ""
                    prodCategory = "Intelligence Suite"
                    prodDesc = ""
                    prodFeaturesText = ""
                    errorMessage = null
                    showDialog = true 
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(50)
            ) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Product", fontFamily = getDmSansFontFamily())
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products, key = { it.id }) { prod ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = prod.name, 
                                fontFamily = getMontserratFontFamily(), 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 22.sp,
                                color = InkNavy
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BrandTeal.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = prod.code,
                                    color = BrandTeal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(prod.description, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Core Modules:", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        prod.features.forEach { feat ->
                            Text("• $feat", fontFamily = getDmSansFontFamily(), color = Color.DarkGray, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = {
                                    editingProduct = prod
                                    prodCode = prod.code
                                    prodName = prod.name
                                    prodCategory = prod.categoryEyebrow
                                    prodDesc = prod.description
                                    prodFeaturesText = prod.features.joinToString(", ")
                                    errorMessage = null
                                    showDialog = true
                                }
                            ) {
                                Text("✏️ Edit", color = BrandTeal, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    products = products.filter { it.id != prod.id }
                                    scope.launch {
                                        deleteProductUseCase(prod.id)
                                    }
                                }
                            ) {
                                Text("🗑 Delete", color = Color(0xFFE53E3E), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MerchandiseTab(
    getMerchUseCase: GetAdminMerchUseCase,
    saveMerchUseCase: SaveAdminMerchUseCase,
    deleteMerchUseCase: DeleteAdminMerchUseCase
) {
    var merchList by remember { mutableStateOf<List<AdminMerch>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingMerchId by remember { mutableStateOf<String?>(null) }
    var itemTitle by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf("Apparel") }
    var itemPrice by remember { mutableStateOf("") }
    var itemImageUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        merchList = getMerchUseCase()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = if (editingMerchId == null) "Tambah Merchandise Baru" else "Edit Merchandise",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = InkNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    errorMessage?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedTextField(
                        value = itemTitle,
                        onValueChange = { itemTitle = it; errorMessage = null },
                        label = { Text("Nama Item", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemCategory,
                        onValueChange = { itemCategory = it; errorMessage = null },
                        label = { Text("Kategori (e.g. Apparel, Accessories)", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemPrice,
                        onValueChange = { itemPrice = it; errorMessage = null },
                        label = { Text("Harga (USD / $)", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("Contoh: 45.0") },
                        singleLine = true,
                        isError = errorMessage != null && itemPrice.toDoubleOrNull() == null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemImageUrl,
                        onValueChange = { itemImageUrl = it; errorMessage = null },
                        label = { Text("Image URL / Path", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("e.g. /images/merch-hoodie.png") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Image thumbnail preview
                    if (itemImageUrl.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp, 40.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = BrandTeal.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🖼️", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Image Ready", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = InkNavy)
                                Text(itemImageUrl, fontFamily = getDmSansFontFamily(), fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = itemPrice.trim().toDoubleOrNull()
                        if (itemTitle.isBlank()) {
                            errorMessage = "Nama item tidak boleh kosong"
                            return@Button
                        }
                        if (parsedPrice == null || parsedPrice <= 0.0) {
                            errorMessage = "Harga harus berupa angka valid lebih dari 0 (contoh: 45.0)"
                            return@Button
                        }
                        scope.launch {
                            val id = editingMerchId ?: "m-${kotlin.random.Random.nextInt(1000, 9999)}"
                            val img = itemImageUrl.trim().ifBlank { null }
                            val result = saveMerchUseCase(AdminMerch(id, itemTitle.trim(), itemCategory.trim(), "The Urban Collaborator", parsedPrice, 5.0, img))
                            if (result.isSuccess) {
                                merchList = getMerchUseCase()
                                showDialog = false
                                editingMerchId = null
                                itemTitle = ""
                                itemCategory = "Apparel"
                                itemPrice = ""
                                itemImageUrl = ""
                                errorMessage = null
                            } else {
                                errorMessage = result.exceptionOrNull()?.message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(if (editingMerchId == null) "Simpan" else "Perbarui", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; editingMerchId = null }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(), 
            horizontalArrangement = Arrangement.SpaceBetween, 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Merchandise Store", 
                    fontFamily = getMontserratFontFamily(), 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 32.sp,
                    color = InkNavy
                )
                Text(
                    text = "Kelola inventaris suvenir dan pernak-pernik resmi (${merchList.size} item)",
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = { 
                    editingMerchId = null
                    itemTitle = ""
                    itemCategory = "Apparel"
                    itemPrice = ""
                    itemImageUrl = ""
                    errorMessage = null
                    showDialog = true 
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(50)
            ) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Item", fontFamily = getDmSansFontFamily())
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(merchList, key = { it.id }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            color = if (item.imageUrl != null) BrandTeal.copy(alpha = 0.1f) else InkNavy.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (item.imageUrl != null) "🖼️" else "👕", fontSize = 36.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(item.title, fontFamily = getMontserratFontFamily(), fontWeight = FontWeight.Bold, color = InkNavy, fontSize = 16.sp)
                        Text("$${item.price}", fontFamily = getDmSansFontFamily(), color = BrandTeal, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.category, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 12.sp)
                            Text("★ ${item.rating}", fontFamily = getDmSansFontFamily(), color = Color(0xFF60BD65), fontSize = 12.sp)
                        }
                        if (item.imageUrl != null) {
                            Text(
                                "Image: ${item.imageUrl}",
                                fontFamily = getDmSansFontFamily(),
                                color = Color.Gray,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = {
                                    editingMerchId = item.id
                                    itemTitle = item.title
                                    itemCategory = item.category
                                    itemPrice = item.price.toString()
                                    itemImageUrl = item.imageUrl ?: ""
                                    errorMessage = null
                                    showDialog = true
                                }
                            ) { 
                                Text("✏️ Edit", color = BrandTeal, fontSize = 13.sp, fontWeight = FontWeight.Bold) 
                            }
                            TextButton(
                                onClick = {
                                    merchList = merchList.filter { it.id != item.id }
                                    scope.launch {
                                        deleteMerchUseCase(item.id)
                                    }
                                }
                            ) { 
                                Text("🗑 Delete", color = Color(0xFFE53E3E), fontSize = 13.sp, fontWeight = FontWeight.Bold) 
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveMetricsTab(
    getLiveMetricsUseCase: GetAdminLiveMetricsUseCase,
    saveLiveMetricsUseCase: SaveAdminLiveMetricsUseCase
) {
    var accuracy by remember { mutableStateOf("99.8%") }
    var totalClients by remember { mutableStateOf("100+") }
    var establishedYear by remember { mutableStateOf("2021") }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val metrics = getLiveMetricsUseCase()
        accuracy = metrics.dataAccuracy
        totalClients = metrics.totalClients
        establishedYear = metrics.establishedYear
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Live Metrics Editor", 
            fontFamily = getMontserratFontFamily(), 
            fontWeight = FontWeight.Bold, 
            fontSize = 32.sp,
            color = InkNavy
        )
        Text(
            text = "Konfigurasi angka metrik yang tampil langsung pada Hero & Trust section Landing Page",
            fontFamily = getDmSansFontFamily(),
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        statusFeedback?.let {
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = it,
                    color = Color(0xFF2E7D32),
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCardEditor(
                label = "Data Accuracy",
                value = accuracy,
                onValueChange = { accuracy = it; statusFeedback = null },
                onSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = "✅ Data Accuracy diperbarui ke '$accuracy'"
                        }
                    }
                }
            )
            MetricCardEditor(
                label = "Total Clients",
                value = totalClients,
                onValueChange = { totalClients = it; statusFeedback = null },
                onSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = "✅ Total Clients diperbarui ke '$totalClients'"
                        }
                    }
                }
            )
            MetricCardEditor(
                label = "Established",
                value = establishedYear,
                onValueChange = { establishedYear = it; statusFeedback = null },
                onSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = "✅ Tahun Didirikan diperbarui ke '$establishedYear'"
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MetricCardEditor(
    label: String, 
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier.width(220.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BrandTeal,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Metrik", fontFamily = getDmSansFontFamily(), fontSize = 13.sp)
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun AdminDashboardPreview() {
    MaterialTheme {
        AdminDashboardScreen()
    }
}
