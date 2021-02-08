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

## Define Configuration

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

## Define Route

Define navigation routes

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

### Register Pattern#1 : Provider

```kotlin
import com.pluu.navigator.provider.Provider

// Step1. Define Route
class SampleProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            Intent(starter.context!!, SampleActivity::class.java)
        }
        // more ...
    }
}

// Step2. Register route
val sampleProvider: Provider = /** Provider */
sampleProvider.provide()
```

### Register Pattern#2 : Functional

```kotlin
// Simple funtion provider
import com.pluu.navigator.provider.routeProvider

// Step1. Define Route
val Feature1_Route_1 = routeProvider(Routes2.Feature1) { starter ->
    Intent(starter.context!!, SampleActivity::class.java)
}

// Step2. Register route
Feature1_Route_1.provide()
```

## Define DeepLink

### Register Pattern#1 : Provider

```kotlin
// Provider Interface
import com.pluu.navigator.provider.Provider

// Step1. Define DeepLink
class SampleProvider : Provider {
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

// Step2. Register route
val sampleProvider: Provider = /** Provider */
sampleProvider.provide()
```

### Register Pattern#2 : Functional

```kotlin
// Simple function provider
import com.pluu.navigator.provider.deepLinkProvider

// Step1. Define DeepLink

// Provider
val DeepLink_Simple: Provider = deepLinkProvider("pluu://feature1") { starter, deepLinkMatch ->
    val intent = Intent(starter.context!!, SampleActivity::class.java)
    starter.start(intent)
}

// Provider : Base Scheme + Path
val DeepLink_Relative_Path: Provider = deepLinkProvider("feature1/sample1?type={type}") { starter, deepLinkMatch ->
    // Sample : pluu://feature1/sample1?type=123
    // deepLinkMatch.args
    // +------+-------+
    // | Key  | Value |
    // +------+-------+
    // | type | 123   |
    // +------+-------+
    val intent = Intent(starter.context!!, SampleActivity::class.java)
    starter.start(intent)
}

// Provider : Command
val DeepLink_Command: Provider = deepLinkProvider<SampleCommand>("pluu://feature1/sample2?type={type}")

class SampleCommand(
    private val type: Int
) : Command {
    override fun execute(starter: Starter) {
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
    }
}

// Step2. Register route
val sampleProvider: Provider = /** Provider */
sampleProvider.provide()
```

## Define Graph

### Register Pattern#1 : Provider

```kotlin
import com.pluu.navigator.Navigator

// Step1. Define graph
val sampleGraph: RouteGraph.Builder = RouteGraph.Builder(
    graphName = "feature1",
    deepLinkConfig = DeepLinkConfig("feature1")
).apply { 
    addRoute(/** */) { starter ->
        Intent(starter.context!!, SampleActivity::class.java)
    }
  
    addDeepLink(/** */) { starter, deepLinkMatch -> 
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
    }
}

// Step2. Register graph
Navigator.addDestinations(sampleGraph.build())
```

### Register Pattern#2 : Functional

```kotlin
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.routeGraph

// Step1. Define Graph
val sampleGraph: RouteGraph = routeGraph(
    graphName = "sample",
    deepLinkConfig = DeepLinkConfig("feature1") // prefix path
) {
    addRoute(Routes1.Feature1_Graph) { starter ->
        Intent(starter.context!!, SampleActivity::class.java)
    }

    // URL : pluu://feature1 
    // Base Scheme + DeepLink-config Prefix Path + Path
    addDeepLink("/") { starter, deepLinkMatch -> 
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
        // Feature1::aaa
        // arg -> gson or 
    }

    // URL : pluu://feature1/1
    // Base Scheme + DeepLink-config Prefix Path + Path
    addDeepLink("1") { starter, deepLinkMatch ->
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
    }

    // URL : luckystar://izumi/konata
    addDeepLink("luckystar://izumi/konata") { starter, deepLinkMatch -> 
        val intent = Intent(starter.context!!, SampleActivity::class.java)
        starter.start(intent)
    }
}

// Step2. Register graph
Navigator.addDestinations(sampleGraph)
```

