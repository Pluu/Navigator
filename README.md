## Multi module sample

```
project
 |
 \---app
 |
 \---feature1 : feature 1 + navigation/deeplink provider
 |
 \---feature2 : feature 2 + navigation/deeplink provider
 |
 \---RoutesConst : define navigation routes
 |
 \---core-android : Utility
```

##  Define  navigation routes

###  interface

```kotlin
object Routes1 {
    object Feature1 : Route()
}

object Routes2 {
    object Feature2 : RouteWithParam()
}
```

### provider

```kotlin
class RouteProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            Intent(starter.context!!, Feature1Activity::class.java)
        }
    }
}
```

## Define deepLink

### routes & Provider

```kotlin
class DeepLinkProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature1").register { starter, deepLinkMatch ->
            Intent(starter.context!!, Feature1Activity::class.java)
        }
    }
}
```

