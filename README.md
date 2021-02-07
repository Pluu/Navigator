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
// Define Config
val config = NavigatorController.Config(
    baseScheme = "pluu"
)
Navigator.registerConfig(config)
```

###  Navigation routes

Interface

```kotlin
object Routes1 {
    object Feature1 : Route()
}

object Routes2 {
    object Feature2 : RouteWithParam<SampleParam>()
}

class SampleParam(
    val value: Int
) : RouteParam()
```

Register provider (creator)

```kotlin
// Provider Interface
class RouteProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            Intent(starter.context!!, SampleActivity::class.java)
        }
        // more ...
    }
}

// Simple funtion provider
val Feature1_Route_1 = routeProvider(Routes2.Feature1) { starter ->
    Intent(starter.context!!, SampleActivity::class.java)
}
```

### DeepLink

Register routes & Provider (executor)

```kotlin
// Provider Interface
class DeepLinkProvider : Provider {
    override fun provide() {
        // Simple
        DeepLink("pluu://feature1").register { starter, deepLinkMatch ->
            val intent = Intent(starter.context!!, SampleActivity::class.java)
            starter.start(intent)
        }
      
        // Query string
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
val Feature1_DeepLink_1 = deepLinkProvider("pluu://feature1") { starter, deepLinkMatch ->
    val intent = Intent(starter.context!!, SampleActivity::class.java)
    starter.start(intent)
}

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
// Define Config
val config = NavigatorController.Config(
    baseScheme = "pluu"
)
Navigator.registerConfig(config)

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

