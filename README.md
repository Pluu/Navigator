## Multi module sample

```
project
│
├──app
│
├──RoutesConst : define navigation routes
│
├── features
│   ├──featureHome : feature Home + navigation provider
│   ├──feature1 : feature 1 + navigation/deeplink provider
│   └──feature2 : feature 2 + navigation/deeplink provider
│
└──core-android : Utility
```

##  Define  navigation routes

###  interface

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

### Register provider (creator)

```kotlin
class RouteProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            Intent(starter.context!!, Feature1Activity::class.java)
        }

        Routes2.Feature2.register { starter ->
            Intent(starter.context!!, Feature2Activity::class.java)
        }
    }
}
```

## Define deepLink

### Register routes & Provider (executor)

```kotlin
class DeepLinkProvider : Provider {
    override fun provide() {
        // Simple
        DeepLink("pluu://feature1").register { starter, deepLinkMatch ->
            val intent = Intent(starter.context!!, Feature1Activity::class.java)
            starter.start(intent)
        }
      
        // Query string
        DeepLink("pluu://feature2?type={type}").register { starter, deepLinkMatch ->
            // Sample : pluu://feature2?type=123
            // deepLinkMatch.args
            // +------+-------+
            // | Key  | Value |
            // +------+-------+
            // | type | 123   |
            // +------+-------+                                                       
            val intent = Intent(starter.context!!, Feature1Activity::class.java)
            starter.start(intent)
        }
    }
}
```

