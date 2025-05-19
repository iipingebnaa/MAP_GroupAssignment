import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.namibiahockeyapp.R
import com.example.namibiahockeyapp.navigation.Routes
import com.example.namibiahockeyapp.navigation.Screen
import com.example.namibiahockeyapp.navigation.Screen.Announcements.route
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeOriginalScreen(navController: NavController) {
    val greenColor = Color(0xFF2E4F33)
    val whiteColor = Color(0xFFF6F8F6)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val loading = remember { mutableStateOf(true) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                SideDrawerContent(navController)
            }
        },
        gesturesEnabled = false
    )
    {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding(),
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Namibia Hockey Union", color = Color.White)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    activeColor = greenColor)
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(greenColor),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = {
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                                loadUrl("https://namibiahockey.org/")
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )



                    /*if (loading.value) {
                        CircularProgressIndicator(
                            color = Color.Blue,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(48.dp)
                        )
                    }*/
                }
            }
        }
    }
}



@Composable
fun SideDrawerContent(navController: NavController) {
    val drawerItems = listOf(
        "Player Registration" to Icons.Default.Person,
        "Player Management" to Icons.Default.ManageAccounts,
        "Team Registration" to Icons.Default.SportsHockey,
        "Players" to Icons.Default.Person,
        "Event Entry" to Icons.Default.Event,
        "Logout" to Icons.Default.ExitToApp
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(1.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // User Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Nina", style = MaterialTheme.typography.titleMedium)
        }

        drawerItems.forEachIndexed { index, (label, icon) ->
            NavigationDrawerItem(
                label = { Text(text = label) },
                selected = false,
                onClick = {
                    when (label) {
                        "Player Registration" -> navController.navigate(Screen.PlayerRegistration.route)
                        "Player Management" -> navController.navigate(Screen.PlayerManagement.route)
                        "Team Registration" -> navController.navigate(Screen.TeamRegistration.route)
                        "Players" -> navController.navigate(Screen.PlayerManagement.route) // Reuse PlayerManagement
                        "Event Entry" -> navController.navigate(Screen.EventEntry.route)
                        "Logout" -> {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                },
                icon = {
                    Icon(icon, contentDescription = label)
                }
            )
            // Add space between items except after the last one
            if (index < drawerItems.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    navController: NavController,
    activeColor: Color
) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val greenColor = Color(0xFF3C5B46)

    NavigationBar(containerColor = greenColor) {
        NavigationBarItem(
            selected = false,
            onClick = {
                openUrl("https://www.instagram.com/p/DJeIBzgsHOs/?igsh=Z2phc2VzaG1zM2s3")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_instagram),
                    contentDescription = "Instagram",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Unspecified,
                unselectedIconColor = Color.Unspecified,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                openUrl("https://www.facebook.com/NamibiaHockey/")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Unspecified,
                unselectedIconColor = Color.Unspecified,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                openUrl("https://twitter.com/yourpage")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "X (Twitter)",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Unspecified,
                unselectedIconColor = Color.Unspecified,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.EventList.route)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Events",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}


