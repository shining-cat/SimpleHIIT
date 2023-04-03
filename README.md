# SimpleHIIT
This is a simple HIIT Android App for Android TV and mobile devices

![image](https://user-images.githubusercontent.com/19568399/217567163-ca5502e0-60ce-406c-8835-71e41011d023.png)

## objectives: 
build a single-module (probably switch to multi-module later) app following clean-architecture principles
* Dagger-hilt for DI
* pure kotlin
* coroutines and flows / stateflows
* Jacoco for test coverage report generation
* Compose for UI
* handle device form-factor variation (TV / mobile) in the same project
* experiment with github actions

## This is a WIP: see current TODO list
https://github.com/shining-cat/SimpleHIIT/blob/master/TODO.md

## Note on thread choices
The current recommendation is to [inject dispatchers](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers), so tests can then inject their own and manipulate them as needed.

### Presentation layer - Main thread
I have chosen to inject the _main_ dispatcher into the viewmodels, as they have UI-related responsibilities.
The viewmodels will still rely on the _viewModelScope_, since it has the advantage of being lifecycle-aware. They do specify the injected dispatcher as the one that scope should use .
This strategy is made possible by following the other recommendation that [all suspend methods should be safe to call from the main thread](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#main-safe)

### Data layer - IO thread
Following this rule, I chose to make the _Data_ layer (through its only entry point, the repository implementation) responsible for all usages of the _IO_ thread, injecting this dispatcher in _SimpleHiitRepositoryImpl_. This choice is dictated by the fact that the data layer is the one aware of any io operations, if any.
Thus all suspend methods in the _SimpleHiitRepository_ include a thread switch towards the IO one. 

### Domain layer - Default thread
Lastly, the _Domain_ layer, as the central point of the clean architecture, also exposes some suspend methods to the _Presentation_ layer. Some of those methods do include some computation work, while others simply call and handle _Data_ layer suspend methods. As the _Data_ layer is responsible for picking the adequate thread to use in its own suspend methods, I feel like the _Domain_ layer has nothing to do with the _IO_ thread. Thus, I will only inject the _Default_ dispatcher in this layer, to be used by suspend methods in the usecases. 





