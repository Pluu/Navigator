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
import com.pluu.navigator.Direction
import com.pluu.navigator.DirectionParam
import com.pluu.navigator.DirectionWithParam

object Routes1 {
    object Feature1 : Direction()
}

object Routes2 {
    object Feature2 : DirectionWithParam<SampleParam>()
}

class SampleParam(val value: Int) : DirectionParam()
```

### Register Pattern#1 : Provider Interface

```kotlin
import com.pluu.navigator.provider.Provider

// Step1. Define Route
class SampleProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            Intent(starter.context, SampleActivity::class.java)
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
val sampleProvider = routeProvider(Routes2.Feature1) { starter ->
    Intent(starter.context, SampleActivity::class.java)
}

// Step2. Register route
sampleProvider.provide()
```

## Define DeepLink

The parameter value of deep link is created as follows.

- Query parameter placeholders in the form of `{placeholder_name}` match one or more characters
  - For example, http://www.example.com/search/id={id} matches http://www.example.com/search/id=4
- The delivered deep link delivers parameter value with each `{placeholder name}` as key
  - For example, http://www.example.com/search/arg1={arg_1}&arg2={arg_2} matches http://www.example.com/search/arg1=sample1=arg2=sample2
  - It is finally constructed in this map. arg_1 = sample1, arg_2 = sample2

### Register Pattern#1 : Provider Interface

```kotlin
// Provider Interface
import com.pluu.navigator.DeepLink
import com.pluu.navigator.provider.Provider

// Step1. Define DeepLink
class SampleProvider : Provider {
    override fun provide() {
        // Simple
        DeepLink("pluu://feature1").register { starter, deepLinkMatch ->
            val intent = Intent(starter.context, SampleActivity::class.java)
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
            val intent = Intent(starter.context, SampleActivity::class.java)
            starter.start(intent)
        }
    }
}

// Step2. Register route
val sampleProvider: Provider = /** Provider */
sampleProvider.provide()
```

### Register Pattern#2 : Functional

Default pattern

```kotlin
import com.pluu.navigator.provider.deepLinkProvider

// Step1. Define DeepLink

// Provider
val DeepLink_Simple: Provider = deepLinkProvider("pluu://feature1") { starter, deepLinkMatch ->
    val intent = Intent(starter.context, SampleActivity::class.java)
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
    val intent = Intent(starter.context, SampleActivity::class.java)
    starter.start(intent)
}

// Step2. Register route
val sampleProvider: Provider = /** Provider */
sampleProvider.provide()
```

### Register Pattern#3 : Command

- Instance the `Command` object using `Gson`

#### Provider Interface

```kotlin
import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.DeepLink
import com.pluu.navigator.provider.Provider
import com.pluu.navigator.starter.Starter

// Step1. Define DeepLink

// Provider 
class SampleCommandProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature2/command?type={type}").register(SampleCommand::class.java)
    }
}

class SampleCommand(
    private val type: Int
) : DeepLinkCommand {
    override fun execute(starter: Starter) {
        val intent = Intent(starter.context, SampleActivity::class.java)
        starter.start(intent)
    }
}

// Step2. Register route
val sampleProvider: Provider = /** Provider */
sampleProvider.provide()
```

#### Futional

```kotlin
import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.provider.deepLinkProvider
import com.pluu.navigator.provider.Provider
import com.pluu.navigator.starter.Starter

// Step1. Define DeepLink

// Provider : Command
val DeepLink_Command: Provider = deepLinkProvider<SampleCommand>("pluu://feature1/sample2?type={type}")

class SampleCommand(
    private val type: Int
) : DeepLinkCommand {
    override fun execute(starter: Starter) {
        val intent = Intent(starter.context, SampleActivity::class.java)
        starter.start(intent)
    }
}

// Step2. Register route
val sampleProvider: Provider = /** Provider */
sampleProvider.provide()
```

## Define Graph

### Register Pattern#1 : Builder Pattern

```kotlin
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.Navigator
import com.pluu.navigator.RouteGraph

// Step1. Define graph
val sampleGraph: RouteGraph.Builder = RouteGraph.Builder(
    graphName = "feature1",
    deepLinkConfig = DeepLinkConfig("feature1")
).apply {   
    // Add Route
    addDestination(/** */) { starter ->
        Intent(starter.context, SampleActivity::class.java)
    }
  
    // Add DeepLink
    addDeepLink(/** */) { starter, deepLinkMatch -> 
        val intent = Intent(starter.context, SampleActivity::class.java)
        starter.start(intent)
    }
}

// Step2. Register graph
Navigator.addDestinations(sampleGraph.build())
```

### Register Pattern#2 : Functional

```kotlin
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.Navigator
import com.pluu.navigator.routeGraph

// Step1. Define Graph
val sampleGraph: RouteGraph = routeGraph(
    graphName = "sample",
    deepLinkConfig = DeepLinkConfig("feature1") // prefix path
) {
    addDestination(Routes1.Feature1_Graph) { starter ->
        Intent(starter.context, SampleActivity::class.java)
    }

    // URL : pluu://feature1 
    // Base Scheme + DeepLink-config Prefix Path + Path
    addDeepLink("/") { starter, deepLinkMatch -> 
        val intent = Intent(starter.context, SampleActivity::class.java)
        starter.start(intent)
        // Feature1::aaa
        // arg -> gson or 
    }

    // URL : pluu://feature1/1
    // Base Scheme + DeepLink-config Prefix Path + Path
    addDeepLink("1") { starter, deepLinkMatch ->
        val intent = Intent(starter.context, SampleActivity::class.java)
        starter.start(intent)
    }

    // URL : luckystar://izumi/konata
    addDeepLink("luckystar://izumi/konata") { starter, deepLinkMatch -> 
        val intent = Intent(starter.context, SampleActivity::class.java)
        starter.start(intent)
    }
}

// Step2. Register graph
Navigator.addDestinations(sampleGraph)
```

## Extension

DeepLink#register

```kotlin
import com.pluu.navigator.util.register

class DeepLinkCommandProvider : Provider {
    override fun provide() {
        DeepLink("/** DeepLink */").register<SampleCommand>()
    }
}
```

RouteGraph.Builder#addDeepLink

```kotlin
import com.pluu.navigator.util.addDeepLink

val sampleGraph = routeGraph(
    /** define graph */
) {
    addDeepLink<SampleCommand>("/** DeepLink */")
}
```

