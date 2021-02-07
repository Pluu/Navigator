## Multi module sample

```
project
│
├──app
│
├──RoutesConst : define navigation routes
│
├── features
│   ├──featureHome
│   │  └──Sample : Navigator
│   ├──feature1
│   │  └──Sample : Navigator, DeepLink, Graph
│   └──feature2
│      └──Sample : Navigator, DeepLink
│
└──core-android : Utility
```

## Define

### Config

- DeepLink Base Scheme

```kotlin
import com.pluu.navigator.Navigator
import com.pluu.navigator.NavigatorController

// Define Config
val config = NavigatorController.Config(
    baseScheme = "pluu"
)
Navigator.registerConfig(config)
```

### Register Destination

Register route & deeplink

```kotlin
import com.pluu.navigator.provider.Provider

val sampleProvider: Provider = /** */
sampleProvider.provide()
```

Register Graph

```kotlin
import com.pluu.navigator.Navigator

// Graph provider pattern
val sampleGraph : RouteGraph = /** */ 
Navigator.addDestinations(sampleGraph)

// Simple register function
sampleGraph.register()
```

###  Navigation routes

Interface

```kotlin
import com.pluu.navigator.Route
import com.pluu.navigator.RouteParam
import com.pluu.navigator.RouteWithParam

object Routes1 {
    object Feature1 : Route()
}

object Routes2 {
    object Feature2 : RouteWithParam<SampleParam>()
}

class SampleParam(val value: Int) : RouteParam()
```

Register provider (creator)

```kotlin
// Provider Interface
import com.pluu.navigator.provider.Provider

class RouteProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            Intent(starter.context!!, SampleActivity::class.java)
        }
        // more ...
    }
}

// Simple funtion provider
import com.pluu.navigator.provider.routeProvider

val Feature1_Route_1 = routeProvider(Routes2.Feature1) { starter ->
    Intent(starter.context!!, SampleActivity::class.java)
}
```

### DeepLink

Register routes & Provider (executor)

```kotlin
// Provider Interface
import com.pluu.navigator.provider.Provider

class DeepLinkProvider : Provider {
    override fun provide() {
        // Simple
        DeepLink("pluu://feature1").register { starter, deepLinkMatch ->
            val intent = Intent(starter.context!!, SampleActivity::class.java)
            starter.start(intent)
        }
      
        // Base Scheme + Path
        DeepLink("feature1?type={type}").register { starter, deepLinkMatch ->
            // Sample : pluu://feature1?type=123
            // deepLinkMatch.args
            // +------+-------+
            // | Key  | Value |
            // +------+-------+
            // | type | 123   |
            // +------+-------+                                                       
            val intent = Intent(starter.context!!, SampleActivity::class.java)
            starter.start(intent)
        }
    }
}

// Simple function provider
import com.pluu.navigator.provider.deepLinkProvider

val Feature1_DeepLink_1 = deepLinkProvider("pluu://feature1") { starter, deepLinkMatch ->
    val intent = Intent(starter.context!!, SampleActivity::class.java)
    starter.start(intent)
}

// Base Scheme + Path
val Feature1_DeepLink_2 = deepLinkProvider("feature1/sample1?type={type}") { starter, deepLinkMatch ->
    // Sample : pluu://feature1?type=123
    // deepLinkMatch.args
    // +------+-------+
    // | Key  | Value |
    // +------+-------+
    // | type | 123   |
    // +------+-------+
    val intent = Intent(starter.context!!, SampleActivity::class.java)
    starter.start(intent)
}
```

### Graph

Register route & deeplink in graph 

```kotlin
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.routeGraph

// Define Graph
val Feature1Graph = routeGraph(
    graphName = "sample",
    deepLinkConfig = DeepLinkConfig("feature1") // prefix path
) {
    addRoute(Routes1.Feature1_Graph) { starter ->
        Intent(starter.context!!, SampleActivity::class.java)
    }

    // URL : pluu://feature1 
    // Base Scheme + DeepLink-config Prefix Path + Path
    addDeepLink("/") { starter, _ -> 
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
    }

    // URL : pluu://feature1/1
    // Base Scheme + DeepLink-config Prefix Path + Path
    addDeepLink("1") { starter, _ ->
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
    }

    // URL : luckystar://izumi/konata
    addDeepLink("luckystar://izumi/konata") { starter, _ -> 
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
    }
}
```

