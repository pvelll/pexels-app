# Pexels App

This project serves as a demonstration of my skills and capabilities as an Android developer.

## Project Overview

The Pexels App is a photo browsing application that leverages the Pexels API to provide users with a vast collection of high-quality images. The app is designed with a clean and intuitive user interface, ensuring a seamless and enjoyable user experience.

## Technologies Used

This project utilizes a range of modern technologies and architectural patterns:

- **Kotlin**: The project is entirely written in Kotlin.

- **XML**: XML is used for designing the layouts in a way that allows for a high degree of customization and flexibility.

- **Koin**: A pragmatic lightweight dependency injection framework for Kotlin. It's used in this project for dependency injection, which helps to manage dependencies in a clean and easy way.

- **Jetpack Navigation**: This component is used to implement effective navigation across different screens of the app.

- **Clean Architecture**: The project follows the principles of Clean Architecture, which promotes separation of concerns and makes the codebase more understandable, flexible, and maintainable.

- **MVVM (Model-View-ViewModel)**: This architectural pattern enables a decoupled and testable codebase. It ensures that UI components are kept away from the business logic.

- **Single Activity**: The app is built around a single activity architecture, where each screen is implemented as a fragment. This modern navigation design simplifies complex UI flows.

- **Coroutines**: Kotlin Coroutines handle long-running tasks that might otherwise block the main thread and impact the UI performance.

- **ViewModel**: ViewModel is used to manage and store UI-related data in a lifecycle conscious way, enabling data to survive configuration changes.

- **Room**: Room persistence library provides an abstraction layer over SQLite, allowing for more robust database access while harnessing the full power of SQLite.

- **Retrofit + OkHttp**: These are used for handling HTTP requests, providing the ability to interact with APIs and retrieve data over the network.

- **ViewBinding**: ViewBinding is used for more efficient view access, improving app performance by avoiding costly findViewById calls.

- **LiveData**: LiveData, an observable data holder, is used to update the UI automatically when the data updates.

